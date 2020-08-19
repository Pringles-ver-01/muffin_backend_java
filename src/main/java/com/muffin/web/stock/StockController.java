package com.muffin.web.stock;

import com.muffin.web.util.Box;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/stocks")
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);
    private StockService stockService;
    private Box box;

    @GetMapping("/csv")
    public void readCsv() {stockService.readCSV();}

    @GetMapping("/marketprices")
    public List<CrawledStockVO> getStockPrice() {
        logger.info("/stockCrawling");
        return  stockService.allStock();
    }

    @GetMapping("/{symbol}")
    public CrawledStockVO getStockDetail(@PathVariable String symbol) {
        logger.info("/stocks/{stockId}");
        return stockService.getOneStock(symbol);
    }

//    @GetMapping("/candles")
//    public List<CrawledStockVO> getCandle() {
//        logger.info("/candle");
//        return stockService.candleCarts();
//    }
}
