package com.muffin.web.asset;

import com.muffin.web.stock.StockRepository;
import com.muffin.web.stock.StockService;
import com.muffin.web.user.User;
import com.muffin.web.user.UserRepository;
import com.muffin.web.util.Box;
import com.muffin.web.util.GenericService;
import com.muffin.web.util.Pagination;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

interface AssetService extends GenericService<Asset> {

    public void readCSV();  // csv 파일 읽기

//    List<TransactionLogVO> transacList(Long userId);

    Integer getOnesTotal(Long userId); // 총액

    List<TransactionLogVO> getOnesHoldings(Long userId); // 주식목록

    List<TransactionLogVO> pagination(Pagination pagination); // 페이징 목록

    void addStock(TransactionLogVO asset); // update holdingShare 매수

    void buyStock(TransactionLogVO asset); // save list 매수

    void updateStock(Asset asset); // 총자산, 보유한 종목, 수량 업데이트

    void sellStock(TransactionLogVO sellOption); // 매도

    boolean existStock(Asset asset); // 종목 존재여부

    void saveAsset(TransactionLogVO update);
}

@AllArgsConstructor
@Service
public class AssetServiceImpl implements AssetService {

    private static final Logger logger = LoggerFactory.getLogger(AssetServiceImpl.class);
    private final AssetRepository repository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final StockService stockService;
    private Box box;


    @Override
    public void readCSV() {
        logger.info("AssetServiceImpl : readCSV()");
        InputStream is = getClass().getResourceAsStream("/static/거래내역1 - 시트1 (6).csv");
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                repository.save(new Asset(
                        Integer.parseInt(csvRecord.get(0)),
                        Integer.parseInt(csvRecord.get(1)),
                        Integer.parseInt(csvRecord.get(2)),
                        Integer.parseInt(csvRecord.get(3)),
                        Double.parseDouble(csvRecord.get(4)),
                        csvRecord.get(5),
                        csvRecord.get(6),
                        userRepository.findById(Long.parseLong(csvRecord.get(7))).get(),
                        stockRepository.findById(Long.parseLong(csvRecord.get(8))).get()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void calculrateTotalProfit(Long userId, String symbol) {
        calculrateProfit(userId, symbol).get(1);
    }

    @Override
    public Integer getOnesTotal(Long userId) {
        return repository.getRecentProfit(userId).getTotalAsset();
    }



    // 보유한 주식 하나의 손익 계산
    private List calculrateProfit(Long userId, String symbol) {
        List result = new ArrayList();

        Integer nowPrice = Integer.parseInt(stockService.getOneStock(symbol).getNow().replaceAll(",", ""));
        List<Integer> purchaseShares = new ArrayList<>();
        List<Asset> list = repository.findOnesAllAsset(userId);
        for (Asset l : list) {
            purchaseShares.add(l.getPurchasePrice());
            purchaseShares.add(l.getShareCount());
        }

        int resultEvaluatedSum = nowPrice * purchaseShares.get(1);
        int myShares = purchaseShares.get(0) * purchaseShares.get(1);
        int resultProfitLoss = nowPrice * purchaseShares.get(1) - myShares;
        double resultProfitRatio = (double) Math.round((double) resultProfitLoss / (double) myShares * 10000) / 100;

        result.add(resultEvaluatedSum);
        result.add(resultProfitLoss);
        result.add(resultProfitRatio);
        result.add(nowPrice);
        return result;
    }


    @Override
    public List<TransactionLogVO> getOnesHoldings(Long userId) {
        List<TransactionLogVO> result = new ArrayList<>();
        List<Asset> list = repository.findOnesAllAsset(userId);
        TransactionLogVO vo = null;

        for (Asset l : list) {
            if (l.getShareCount() > 0) {
                vo = new TransactionLogVO();
                vo.setAssetId(l.getAssetId());
                vo.setStockId(l.getStock().getStockId());
                vo.setStockName(l.getStock().getStockName());
                vo.setTotalAsset(repository.getRecentTotal(userId));
                vo.setTotalProfit(repository.getRecentProfit(userId).getTotalProfit());
                vo.setTotalProfitRatio(repository.getRecentProfit(userId).getTotalProfitRatio());
                vo.setTransactionType(l.getTransactionType());
                vo.setTransactionDate(l.getTransactionDate());
                vo.setSymbol(l.getStock().getSymbol());
                vo.setStockId(l.getStock().getStockId());
                vo.setShareCount(l.getShareCount());
                vo.setPurchasePrice(l.getPurchasePrice());
                vo.setEvaluatedSum((Integer) calculrateProfit(userId, l.getStock().getSymbol()).get(0));
                vo.setProfitLoss((Integer) calculrateProfit(userId, l.getStock().getSymbol()).get(1));
                vo.setProfitRatio((Double) calculrateProfit(userId, l.getStock().getSymbol()).get(2));
                vo.setNowPrice((Integer) calculrateProfit(userId, l.getStock().getSymbol()).get(3));
                vo.setHasAsset(true);
                result.add(vo);
            } else {
                vo = new TransactionLogVO();
                vo.setHasAsset(false);
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    public List<TransactionLogVO> pagination(Pagination pagination) {
        List<TransactionLogVO> result = new ArrayList<>();
        List<Asset> findLogs = repository.pagination(pagination);
        return getTransactionLogVOS(result, findLogs);
    }

    @Override // update 매수
    public void addStock(TransactionLogVO invoice) {
        Asset asset = new Asset();
        int recentTotal = repository.getRecentTotal(invoice.getUserId());
        int buyCount = invoice.getShareCount();
        int buyAmount = invoice.getPurchasePrice();
        int newAmount = recentTotal - buyAmount;
        int recentShareCount = repository.getOwnedShareCount(invoice.getSymbol());
        int newShareCount = recentShareCount + buyCount;
        int totalProfit = newAmount - recentTotal;
        double totalProfitRatio = (double) Math.round((double) totalProfit / (double) recentTotal * 10000) / 100;
        asset.setTransactionType(invoice.getTransactionType());
        asset.setTransactionDate(invoice.getTransactionDate());
        asset.setPurchasePrice(invoice.getPurchasePrice());
        asset.setShareCount(newShareCount);
        asset.setTotalAsset(newAmount);
        asset.setTotalProfit(totalProfit);
        asset.setTotalProfitRatio(totalProfitRatio);
        asset.setUser(userRepository.findById(invoice.getUserId()).get());
        asset.setAssetId(invoice.getAssetId());
        updateStock(asset);
    }

    @Override // 신규 매수
    public void buyStock(TransactionLogVO invoice) {
        Asset asset = new Asset();
        int recentTotal = repository.getRecentTotal(invoice.getUserId());
        int buyAmount = invoice.getPurchasePrice();
        int newAmount = recentTotal - buyAmount;
        int totalProfit = newAmount - recentTotal;
        double totalProfitRatio = (double) Math.round((double) totalProfit / (double) recentTotal * 10000) / 100;
        asset.setTransactionType(invoice.getTransactionType());
        asset.setTransactionDate(invoice.getTransactionDate());
        asset.setPurchasePrice(invoice.getPurchasePrice());
        asset.setShareCount(invoice.getShareCount());
        asset.setTotalAsset(newAmount);
        asset.setTotalProfit(totalProfit);
        asset.setTotalProfitRatio(totalProfitRatio);
        asset.setStock(stockRepository.findByStockName(invoice.getStockName()).get());
        asset.setUser(userRepository.findById(invoice.getUserId()).get());
        repository.save(asset);
    }

    @Override // 총자산, 보유한 종목, 수량 업데이트
    public void updateStock(Asset update) {
        repository.updateAsset(update);
    }

    @Override
    public void saveAsset(TransactionLogVO invoice) {
        Asset asset;
        Optional<Asset> findAsset = repository.findById(invoice.getAssetId());
        asset = findAsset.orElseGet(Asset::new);
        asset.setStock(stockRepository.findById(invoice.getStockId()).get());
        asset.setUser(userRepository.findById(invoice.getUserId()).get());
        asset.setAssetId(asset.getAssetId());
        asset.setTotalProfitRatio(asset.getTotalProfitRatio());
        asset.setTotalAsset(asset.getTotalAsset());
        asset.setShareCount(asset.getShareCount());
        asset.setPurchasePrice(asset.getPurchasePrice());
        asset.setTransactionDate(asset.getTransactionDate());
        asset.setTransactionType(asset.getTransactionType());
        repository.save(asset);
    }


    @Override // 매도
    public void sellStock(TransactionLogVO invoice) {
        logger.info("void sellStock..." + invoice);
        TransactionLogVO asset = new TransactionLogVO();
        int recentTotal = repository.getRecentTotal(invoice.getUserId());
        int recentShareCount = repository.getOwnedShareCount(invoice.getSymbol());
        int sellCount = invoice.getShareCount();
        int money = invoice.getPurchasePrice();
        int newAmount = recentTotal + money;
        int newShareCount = recentShareCount - sellCount;
        asset.setAssetId(invoice.getAssetId());
        asset.setTotalAsset(newAmount);
        asset.setShareCount(newShareCount);
        asset.setPurchasePrice(invoice.getPurchasePrice());
        asset.setTransactionDate(invoice.getTransactionDate());
        asset.setTransactionType(invoice.getTransactionType());
        asset.setSymbol(invoice.getSymbol());
        asset.setStockName(invoice.getStockName());
        asset.setUserId(invoice.getUserId());

        int totalProfit = newAmount - recentTotal;
        double totalProfitRatio = (double) Math.round((double) totalProfit / (double) recentTotal * 10000) / 100;
        asset.setTotalProfit(totalProfit);
        asset.setTotalProfitRatio(totalProfitRatio);

        if (newShareCount == 0) {
            logger.info("전량 매도오오" + newShareCount);
            repository.deleteAsset(invoice.getShareCount());
        } else {
//            updateStock(asset);
        }
    }

    @Override
    public boolean existStock(Asset asset) {
        return repository.existsById(asset.getAssetId());
    }


    private List<TransactionLogVO> getTransactionLogVOS(List<TransactionLogVO> result, Iterable<Asset> findLogs) {
        findLogs.forEach(asset -> {
            TransactionLogVO vo = new TransactionLogVO();
            vo.setTransactionDate(asset.getTransactionDate());
            vo.setTransactionType(asset.getTransactionType());
            vo.setStockName(asset.getStock().getStockName());
            vo.setPurchasePrice(asset.getPurchasePrice());
            vo.setTotalAsset(asset.getTotalAsset());
            result.add(vo);
        });
        return result;
    }

    @Override
    public Iterable<Asset> findAll() {
        return null;
    }

    @Override
    public int count() {
        return (int) repository.count();
    }

    @Override
    public void delete(Asset asset) {
    }


    @Override
    public boolean exists(String id) {
        return false;
    }
}
