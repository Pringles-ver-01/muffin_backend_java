package com.muffin.web.stock;
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
import java.util.Optional;

interface StockService extends GenericService<Stock> {

    void readCSV();
}

@Service
class StockServiceImpl implements StockService{
    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);
    private final StockRepository repository;


    StockServiceImpl(StockRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Stock stock) {

    }

    @Override
    public Optional<Stock> findById(String id) {
        return Optional.empty();
    }

    public Optional<Stock> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Iterable<Stock> findAll() {
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

    @Override
    public void readCSV() {
        logger.info("StockServiceImpl : readCSV()");
        InputStream is = getClass().getResourceAsStream("/static/stocks.csv");

        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for(CSVRecord csvRecord : csvRecords){
                repository.save(new Stock(
                        csvRecord.get(1),
                        csvRecord.get(2),
                        new ArrayList<>()
                ));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}