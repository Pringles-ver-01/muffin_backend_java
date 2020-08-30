package com.muffin.web.asset;

import com.muffin.web.util.Box;
import com.muffin.web.util.Pagination;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


import java.util.List;

import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/assets")
public class AssetController {

    private static final Logger logger = LoggerFactory.getLogger(AssetController.class);
    private AssetService assetService;
    private Box box;
    private final Pagination pagination;

    @GetMapping("/csv")
    public void readCsv() {
        assetService.readCSV();
    }

    @GetMapping("/test")
    public void getData() {
        logger.info("/asset/test AssetController");
    }

    @GetMapping("/pagination/{page}/{range}/{userId}")
    public Map<?, ?> pagination(@PathVariable int page, @PathVariable int range, @PathVariable Long userId) {
        pagination.pageInfo(page, range, assetService.historyCount(userId));
        Map<String, Object> box = new HashMap<>();
        box.put("pagination", pagination);
        box.put("list", assetService.paginationHistory(pagination, userId));
        return box;
    }

//    @GetMapping("/transactionlog/{userId}")
//    public List<TransactionLogVO> getTransacDetail(@PathVariable Long userId) {
//        logger.info("/transaction_list");
//        List<TransactionLogVO> logs = assetService.transacList(userId);
//        return logs;
//    }

    @GetMapping("/total/{userId}")
    public HashMap<String, Object> totalAsset(@PathVariable Long userId) {
        box.clear();
        box.put("totalAmount", assetService.getOnesTotal(userId));
        return box.get();
    }

    @GetMapping("/holdingCount/{userId}")
    public HashMap getHoling(@PathVariable Long userId) {
        box.clear();
        box.put("holdingCount", assetService.getOnesHoldings(userId));
        return box.get();
    }

    @PostMapping("/ownedStock/{userId}")
    public List<TransactionLogVO> letBuyStock(@PathVariable Long userId, @RequestBody TransactionLogVO invoice) {
        assetService.addStock(invoice);
        return assetService.getOnesHoldings(userId);
    }

    @PostMapping("/newStock/{userId}")
    public List<TransactionLogVO> letBuyNewStock(@PathVariable Long userId, @RequestBody TransactionLogVO invoice) {
        assetService.buyStock(invoice);
        return assetService.getOnesHoldings(userId);
    }

    @PostMapping("/sell/{userId}")
    public List<TransactionLogVO> letSellStock(@PathVariable Long userId, @RequestBody TransactionLogVO invoice) {
        assetService.sellStock(invoice);
        return assetService.getOnesHoldings(userId);
    }

}
