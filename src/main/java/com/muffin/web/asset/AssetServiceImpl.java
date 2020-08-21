package com.muffin.web.asset;

import com.muffin.web.stock.StockRepository;
import com.muffin.web.stock.StockService;
import com.muffin.web.user.UserRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

interface AssetService extends GenericService<Asset> {

    Asset showData();

    public void readCSV();

//    List<TranscationLogVO> transacList(Long userId);

    List<Integer> getOnesTotal(Long userId);

    List<TranscationLogVO> getOnesHoldings(Long userId);

    List<TranscationLogVO> pagination(Pagination pagination);

    Optional<Asset> findById(Long id);
}
@AllArgsConstructor
@Service
public class AssetServiceImpl implements AssetService {

    private static final Logger logger = LoggerFactory.getLogger(AssetServiceImpl.class);
    private final AssetRepository repository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final StockService stockService;


    @Override
    public Asset showData() {
        logger.info("AssetServiceImpl : public List<Integer> showData()");
        return repository.showOneData();
    }

    @Override
    public void readCSV(){
        logger.info("AssetServiceImpl : readCSV()");
        InputStream is = getClass().getResourceAsStream("/static/거래내역1 - 시트1 (6).csv");
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for(CSVRecord csvRecord : csvRecords){
                repository.save(new Asset(
                        Integer.parseInt(csvRecord.get(0)),
                        Integer.parseInt(csvRecord.get(1)),
                        Integer.parseInt(csvRecord.get(2)),
                        csvRecord.get(3),
                        csvRecord.get(4),
                        userRepository.findById(Long.parseLong(csvRecord.get(5))).get(),
                        stockRepository.findById(Long.parseLong(csvRecord.get(6))).get()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public List<TranscationLogVO> transacList(Long userId) {
//        List<TranscationLogVO> result = new ArrayList<>();
//        List<Asset> list = repository.getTransacList(userId);
//        TranscationLogVO vo = null;
//
//        for(Asset l : list){
//            vo = new TranscationLogVO();
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
    public List getOnesTotal(Long userId) {
        List result = new ArrayList();

        result.add(repository.getRecentTotal(userId));
        return result;
    }

    private List calculrateProfit(Long userId, String symbol) {
        List result = new ArrayList();
        logger.info("...calculrating... oneStock... profit...");

        Integer nowPrice = Integer.parseInt(stockService.getOneStock(symbol).getNow().replaceAll(",",""));
        List<Integer> purchaseShares = new ArrayList<>();
        List<Asset> list = repository.getHolingStocks(userId);
        for(Asset l : list) {
            purchaseShares.add(l.getPurchasePrice());
            purchaseShares.add(l.getShareCount());
        }

        int resultEvaluatedSum = nowPrice * purchaseShares.get(1);
        int myShares = purchaseShares.get(0) * purchaseShares.get(1);
        int resultProfitLoss = nowPrice * purchaseShares.get(1)  - myShares;
        double resultProfitRatio =  (double)Math.round((double)resultProfitLoss / (double)myShares * 10000) / 100;

        result.add(resultEvaluatedSum);
        result.add(resultProfitLoss);
        result.add(resultProfitRatio);
        result.add(nowPrice);
        return result;
    }


    @Override
    public List<TranscationLogVO> getOnesHoldings(Long userId) {
        //shareCount가 0이면 보이지 않는다!!!
        List<TranscationLogVO> result = new ArrayList<>();
        List<Asset> list = repository.getHolingStocks(userId);
        TranscationLogVO vo = null;

        for(Asset l : list) {
            if(l.getShareCount() > 0) {
                vo = new TranscationLogVO();
                vo.setStockName(l.getStock().getStockName());
                vo.setTotalAsset(l.getTotalAsset());
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
                vo = new TranscationLogVO();
                vo.setHasAsset(false);
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    public List<TranscationLogVO> pagination(Pagination pagination) {
        List<TranscationLogVO> result = new ArrayList<>();
        List<Asset> findLogs = repository.pagination(pagination);
        return getTranscationLogVOS(result, findLogs);
    }

    private List<TranscationLogVO> getTranscationLogVOS(List<TranscationLogVO> result, Iterable<Asset> findLogs){
        findLogs.forEach( asset -> {
            TranscationLogVO vo = new TranscationLogVO();
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
    public Optional<Asset> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Iterable<Asset> findAll() {
        return null;
    }

    @Override
    public int count() {
        return (int)repository.count();
    }

    @Override
    public void delete(Asset asset) { }

    @Override
    public boolean exists(String id) {
        return false;
    }
}