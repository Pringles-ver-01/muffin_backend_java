package com.muffin.web.stock;

import com.muffin.web.util.GenericService;
import com.muffin.web.util.Pagination;
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

public interface StockService extends GenericService<Stock> {

    Optional<Stock> findById(String id);

    void save(Stock stock);

    void readCSV();

    List<CrawledStockVO> allStock();

    CrawledStockVO getOneStock(String symbol);

    List<CrawledStockVO> pagination(Pagination pagination);

    Object findByStockSearchWordPage(String stockSearch);


}

@Service
class StockServiceImpl implements StockService {
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
        return (int) repository.count();
    }

    @Override
    public void delete(Stock stock) {
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
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
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
        List<CrawledStockVO> cralwedResults = new ArrayList<>();
//        List<String> listedSymbols = repository.findAllSymbol();
//        for(String stockCode: listedSymbols){
//            t.add(stockCrawling(stockCode)) ;
//        }
        List<String> miniListed = repository.findMiniListed();
        for (String stockCode : miniListed) {
            cralwedResults.add(stockCrawling(stockCode));
        }

        return cralwedResults;
    }

    @Override
    public CrawledStockVO getOneStock(String symbol) {
        CrawledStockVO vo = stockCrawling(symbol);
        return vo;
    }


    private CrawledStockVO stockCrawling(String stockCode) {
        CrawledStockVO vo = null;
        try {
            String url = "https://finance.naver.com/item/main.nhn?code=" + stockCode;
            Connection.Response homepage = Jsoup.connect(url).method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .execute();
            Document d = homepage.parse();

            Elements getName = d.select("div.wrap_company");
            Elements getH2 = getName.select("h2");
            Elements stockName = getH2.select("a");

            Elements symbol = d.select("span.code");

            Elements table = d.select("table.no_info");
            Elements trs = table.select("tr");
            Element firstTr = trs.get(0);
            Elements firstTrTds = firstTr.select("td");
            Element high = firstTrTds.get(0);
            Element close = firstTrTds.get(1);
            Element volume = firstTrTds.get(2);

            Element secondTr = trs.get(1);
            Elements secondTrTds = secondTr.select("td");
            Element open = secondTrTds.get(0);
            Element low = secondTrTds.get(1);
            Element transacAmount = secondTrTds.get(2);

            Elements now = d.select("p.no_today");
            Elements nowBlind = now.select("span.blind");
            Elements openBlind = open.select("span.blind");
            Elements highBlind = high.select("span.blind");
            Elements lowBlind = low.select("span.blind");
            Elements closeBlind = close.select("span.blind");
            Elements volumeBlind = volume.select("span.blind");
            Elements transacAmountBlind = transacAmount.select("span.blind");
            Elements crawledDate = d.select("#time");

            Elements dod = d.select("p.no_exday");
            Elements dodblind = dod.select("span.blind");

            Elements capital = d.select("#_market_sum");

            for (int i = 0; i < symbol.size(); i++) {
                vo = new CrawledStockVO();
                vo.setStockName(stockName.get(i).text());
                vo.setSymbol(symbol.get(i).text());
                vo.setNow(nowBlind.get(i).text());
                vo.setHigh(highBlind.get(i).text());
                vo.setLow(lowBlind.get(i).text());
                vo.setOpen(openBlind.get(i).text());
                vo.setClose(closeBlind.get(i).text());
                vo.setVolume(volumeBlind.get(i).text());
                vo.setDate(crawledDate.get(i).text());
                vo.setTransacAmount(transacAmountBlind.get(i).text());
                vo.setDod(dodblind.get(i).text());
                vo.setCapital(capital.get(i).text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vo;
    }

    @Override
    public List<CrawledStockVO> pagination(Pagination pagination) {
        List<CrawledStockVO> result = new ArrayList<>();
        List<Stock> crawledStock = repository.pagination(pagination);
        return getStocksVOS(result, crawledStock);
    }

    @Override
    public Object findByStockSearchWordPage(String stockSearch) {
        return repository.selectByStockNameLikeSearchWordPage(stockSearch);
    }

    private List<CrawledStockVO> getStocksVOS(List<CrawledStockVO> result, Iterable<Stock> crawledStock) {
        List<String> miniListed = repository.findMiniListed();
        for (String stockCode : miniListed) {
            result.add(stockCrawling(stockCode));
        }
        return result;
    }

}