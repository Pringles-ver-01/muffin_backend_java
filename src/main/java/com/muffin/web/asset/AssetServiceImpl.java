package com.muffin.web.asset;

import com.muffin.web.stock.StockRepository;
import com.muffin.web.user.UserRepository;
import com.muffin.web.util.GenericService;
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

    List<TranscationLogVO> transacList();

    List<Integer> getOnesTotal();
}

@Service
public class AssetServiceImpl implements AssetService {

    private static final Logger logger = LoggerFactory.getLogger(AssetServiceImpl.class);
    private final AssetRepository repository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    public AssetServiceImpl(AssetRepository repository, UserRepository userRepository, StockRepository stockRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public Asset showData() {
        logger.info("AssetServiceImpl : public List<Integer> showData()");
        return repository.showOneData();
    }

    @Override
    public void readCSV(){
        logger.info("AssetServiceImpl : readCSV()");
        InputStream is = getClass().getResourceAsStream("/static/assets_log.csv");
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

    @Override
    public List<TranscationLogVO> transacList() {
        List<TranscationLogVO> result = new ArrayList<>();
        List<Asset> list = repository.getTransacList();
        TranscationLogVO vo = null;
        for(Asset l : list){
            vo = new TranscationLogVO();
            vo.setTransactionDate(l.getTransactionDate());
            vo.setTransactionType(l.getTransactionType());
            vo.setPurchasePrice(l.getPurchasePrice());
            vo.setShareCount(l.getShareCount());
            vo.setTotalAsset(l.getTotalAsset());
            vo.setStockName(l.getStock().getStockName());
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<Integer> getOnesTotal() {
        return repository.getRecentTotal();
    }

    @Override
    public void save(Asset asset) { }

    @Override
    public Optional<Asset> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Iterable<Asset> findAll() {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public void delete(String id) { }

    @Override
    public boolean exists(String id) {
        return false;
    }
}
