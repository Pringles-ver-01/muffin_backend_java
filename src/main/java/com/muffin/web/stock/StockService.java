package com.muffin.web.stock;
import com.muffin.web.util.GenericService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.nodes.Element;
import org.mapstruct.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

interface StockService extends GenericService<Stock> {

    Optional<Stock> findById(String id);

    void save(Stock stock);

    void readCSV();

    CrawledStockVO stockCrawling(String stockCode);

    List<CrawledStockVO> allStock();

    CrawledStockVO getOneStock(String symbol);
}

@Service
class StockServiceImpl implements StockService{
    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);
    private final StockRepository repository;

    StockServiceImpl(StockRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Stock stock) { }

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
    public void delete(Stock stock) { }


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


    @Override
    public List<CrawledStockVO> allStock() {
        logger.info("StockServiceImpl : allStock()");
        List<CrawledStockVO> t =  new ArrayList<>();
        List<String> listedSymbols = repository.findAllSymbol();

        for(String stockCode: listedSymbols){
            t.add(stockCrawling(stockCode)) ;
        }
        return t;
    }

    @Override
    public CrawledStockVO getOneStock(String symbol) {
        logger.info("StockServiceImpl : CrawledStockVO getOneStock(String " + symbol +" )");
        logger.info("~~~~~~~~~~"+symbol+"~~~~~~~~~~");
        CrawledStockVO vo = stockCrawling(symbol);
        return vo;
    }


    @Override
    public CrawledStockVO stockCrawling(String stockCode) {
        logger.info("StockServiceImpl : stockCrawling( "+ stockCode +" )");
        CrawledStockVO vo = null;
            try {
                String url = "https://finance.naver.com/item/main.nhn?code=" + stockCode;
                Connection.Response homepage = Jsoup.connect(url).method(Connection.Method.GET)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                        .execute();
                Document d = homepage.parse();

                Elements symbol = d.select("span.code");

                Elements table = d.select("table.no_info");
                Elements trs = table.select("tr");
                Element first_tr = trs.get(0);
                Elements first_tr_tds = first_tr.select("td");
                Element first_tr_tds_first_td = first_tr_tds.get(0);
                Element first_tr_tds_second_td = first_tr_tds.get(1);
                Element first_tr_tds_third_td = first_tr_tds.get(2);

                Element second_tr = trs.get(1);
                Elements second_tr_tds = second_tr.select("td");
                Element second_tr_tds_first_td = second_tr_tds.get(0);
                Element second_tr_tds_second_td = second_tr_tds.get(1);
                Element second_tr_tds_third_td = second_tr_tds.get(2);

                Elements now = d.select("p.no_today");
                Elements nowBlind = now.select("span.blind");
                Elements openBlind = second_tr_tds_first_td.select("span.blind");
                Elements highBlind = first_tr_tds_second_td.select("span.blind");
                Elements lowBlind = second_tr_tds_second_td.select("span.blind");
                Elements closeBlind = first_tr_tds_first_td.select("span.blind");
                Elements volume = first_tr_tds_third_td.select("span.blind");
                Elements transacAmount = second_tr_tds_third_td.select("span.blind");
                Elements crawledDate = d.select("#time");

                Elements dod = d.select("p.no_exday");
                Elements dodblind = dod.select("span.blind");

                Elements capital = d.select("#_market_sum");

                String stockName = repository.findBySymbol(stockCode);
                logger.info(stockName);
                for(int i = 0; i < symbol.size(); i++) {
                    vo = new CrawledStockVO();
                    vo.setStockName(stockName);
                    vo.setSymbol(symbol.get(i).text());
                    vo.setNow(nowBlind.get(i).text());
                    vo.setHigh(highBlind.get(i).text());
                    vo.setLow(lowBlind.get(i).text());
                    vo.setOpen(openBlind.get(i).text());
                    vo.setClose(closeBlind.get(i).text());
                    vo.setVolume(volume.get(i).text());
                    vo.setDate(crawledDate.get(i).text());
                    vo.setTransacAmount(transacAmount.get(i).text());
                    vo.setDod(dodblind.get(i).text());
                    vo.setCapital(capital.get(i).text());
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

        return vo;
    }

}