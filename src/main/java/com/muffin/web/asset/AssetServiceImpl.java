package com.muffin.web.asset;

import com.muffin.web.util.Box;
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
import java.util.List;
import java.util.Optional;

interface AssetService extends GenericService<Asset> {

    Asset showData();

    public void readCSV();

    List<Asset> transacList();
}

@Service
public class AssetServiceImpl implements AssetService {

    private static final Logger logger = LoggerFactory.getLogger(AssetServiceImpl.class);
    private final AssetRepository repository;
    private final Box<List<Asset>> assets;

    public AssetServiceImpl(AssetRepository repository, Box<List<Asset>> assets) {
        this.repository = repository;
        this.assets = assets;
    }

    @Override
    public Asset showData() {
        logger.info("AssetServiceImpl : public List<Integer> showData()");
        return repository.showOneData();
    }

    @Override
    public void readCSV(){
        logger.info("AssetServiceImpl : readCSV()");
        InputStream is = getClass().getResourceAsStream("/static/assets.csv");

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
                        csvRecord.get(5),
                        csvRecord.get(6)
                ));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Asset> transacList() {
        return repository.getTransacList();
    }


    @Override
    public void save(Asset asset) {

    }

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
    public void delete(String id) {

    }

    @Override
    public boolean exists(String id) {
        return false;
    }
}
