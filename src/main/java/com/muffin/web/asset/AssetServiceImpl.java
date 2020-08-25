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

    void buyStock(TransactionLogVO asset); // 신규 매수

    void updateStock(TransactionLogVO asset); // 총자산, 보유한 종목, 수량 업데이트

    void sellStock(TransactionLogVO sellOption); // 매도

    boolean existStock(Asset asset); // 종목 존재여부

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

//    @Override
//    public List<TransactionLogVO> transacList(Long userId) {
//        List<TransactionLogVO> result = new ArrayList<>();
//        List<Asset> list = repository.getTransacList(userId);
//        TransactionLogVO vo = null;
//
//        for(Asset l : list){
//            vo = new TransactionLogVO();
//            vo.setTransactionDate(l.getTransactionDate());
//            vo.setTransactionType(l.getTransactionType());
//            vo.setPurchasePrice(l.getPurchasePrice());
//            vo.setShareCount(l.getShareCount());
//            vo.setTotalAsset(l.getTotalAsset());
//            vo.setStockName(l.getStock().getStockName());
//            vo.setUserId(l.getUser().getUserId());
//            result.add(vo);
//        }
//        return result;
//    }

    private void calculrateTotalProfit(Long userId, String symbol) {
        calculrateProfit(userId, symbol).get(1);
    }

    @Override
    public Integer getOnesTotal(Long userId) { return repository.getRecentProfit(userId).getTotalAsset(); }

    // 사용자의 전체 에셋에 대한 손익 계산
    private void calculrateTotalProfit(Long userId) {
        //전체 수익금 = profit[0] + ... + profit[n] : 가지고 있는 주식의 갯수 만큼
        int ownedStockCount = repository.getOwnedStockCount(userId);
        for (int i = 0; i < ownedStockCount; i++) {
//            calculrateProfit(userId, symbol);
        }
        //전체 수익률 = (전체 수익금 / 총 매입 금액) * 100

    }

    // 보유한 주식 하나의 손익 계산
    private List calculrateProfit(Long userId, String symbol) {
        List result = new ArrayList();
        logger.info("...calculrating... oneStock... profit...");

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
                vo.setStockName(l.getStock().getStockName());
                vo.setTotalAsset(repository.getRecentTotal(userId));
                vo.setTotalProfit(repository.getRecentProfit(userId).getTotalProfit());
                vo.setTotalProfitRatio(repository.getRecentProfit(userId).getTotalProfitRatio());
                vo.setTransactionType(l.getTransactionType());
                vo.setTransactionDate(l.getTransactionDate());
                vo.setSymbol(l.getStock().getSymbol());
                vo.setShareCount(l.getShareCount());
                vo.setPurchasePrice(l.getPurchasePrice());
                vo.setEvaluatedSum((Integer) calculrateProfit(userId, l.getStock().getSymbol()).get(0));
                vo.setProfitLoss((Integer) calculrateProfit(userId, l.getStock().getSymbol()).get(1));
                vo.setProfitRatio((Double) calculrateProfit(userId, l.getStock().getSymbol()).get(2));
                vo.setNowPrice((Integer) calculrateProfit(userId, l.getStock().getSymbol()).get(3));
                vo.setHasAsset(true);
                result.add(vo);
                logger.info(String.valueOf(vo));
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

    @Override //신규 매수
    public void buyStock(TransactionLogVO invoice) {
        logger.info("buyStock.... " + invoice);
        TransactionLogVO asset = new TransactionLogVO();
        int recentTotal = repository.getRecentTotal(invoice.getUserId());
        int buyCount = invoice.getShareCount();
        int buyAmount = invoice.getPurchasePrice();
        int newAmount = recentTotal - buyAmount;

        logger.info("**********  " + invoice.getSymbol() );
        logger.info("~~~~~~~~ " + repository.getOwnedShareCount(invoice.getSymbol()));
        if ( repository.getOwnedShareCount(invoice.getSymbol()) == null ) {
            int newShareCount = buyCount;
            int totalProfit = newAmount - recentTotal;
            double totalProfitRatio = (double) Math.round((double) totalProfit / (double) recentTotal * 10000) / 100;

            asset.setAssetId(invoice.getAssetId());
            asset.setTransactionType(invoice.getTransactionType());
            asset.setTransactionDate(invoice.getTransactionDate());
            asset.setPurchasePrice(invoice.getPurchasePrice());
            asset.setShareCount(newShareCount);
            asset.setTotalAsset(newAmount);
            asset.setTotalProfit(totalProfit);
            asset.setTotalProfitRatio(totalProfitRatio);
            asset.setStockId(invoice.getStockId());
            asset.setUserId(invoice.getUserId());

            logger.info("no exist stock.... " + asset);
//            repository.save(asset);

        } else {
            int recentShareCount = repository.getOwnedShareCount(invoice.getSymbol());
            int newShareCount = recentShareCount + buyCount;
            int totalProfit = newAmount - recentTotal;
            double totalProfitRatio = (double) Math.round((double) totalProfit / (double) recentTotal * 10000) / 100;

            asset.setAssetId(invoice.getAssetId());
            asset.setTransactionType(invoice.getTransactionType());
            asset.setTransactionDate(invoice.getTransactionDate());
            asset.setPurchasePrice(invoice.getPurchasePrice());
            asset.setShareCount(newShareCount);
            asset.setTotalAsset(newAmount);
            asset.setTotalProfit(totalProfit);
            asset.setTotalProfitRatio(totalProfitRatio);
            asset.setStockId(invoice.getStockId());
            asset.setUserId(invoice.getUserId());

            logger.info("exist stock...... " + asset);
            updateStock(asset);

        }
    }

    @Override // 총자산, 보유한 종목, 수량 업데이트
    public void updateStock(TransactionLogVO update) {
        repository.updateAsset(update);
    }

    @Override // 매도
    public void sellStock(TransactionLogVO invoice) {
        logger.info("void sellStock...");
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

        int totalProfit = newAmount - recentTotal;
        double totalProfitRatio = (double) Math.round((double) totalProfit / (double) recentTotal * 10000) / 100;
        asset.setTotalProfit(totalProfit);
        asset.setTotalProfitRatio(totalProfitRatio);

        if (newShareCount == 0) {
            logger.info("전량 매도오오" + newShareCount);
            repository.deleteAsset(invoice.getShareCount());
        } else {
            updateStock(asset);
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
