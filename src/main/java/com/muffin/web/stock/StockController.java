
package com.muffin.web.stock;

import com.muffin.web.util.Pagination;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/stocks")
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);
    private StockService stockService;
    private final Pagination pagination;


    @GetMapping("/csv")
    public void readCsv() {stockService.readCSV();}

    @GetMapping("/pagination/{page}/{range}")
    public Map<?,?> pagination(@PathVariable int page, @PathVariable int range) {
        System.out.println(page+", "+range);
        pagination.pageInfo(page, range, stockService.count());
        Map<String, Object> box = new HashMap<>();
        box.put("pagination", pagination);
        box.put("list", stockService.pagination(pagination));
        return box;
    }

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