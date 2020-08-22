package com.muffin.web.asset;

import com.muffin.web.util.Box;
import com.muffin.web.util.Pagination;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> master
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
    public void readCsv() {assetService.readCSV();}

    @GetMapping("/test")
    public void getData() {
        logger.info("/asset/test AssetController");
    }

    @GetMapping("/pagination/{page}/{range}")
    public Map<?,?> pagination(@PathVariable int page, @PathVariable int range) {
        System.out.println(page+", "+range);
        pagination.pageInfo(page, range, assetService.count());
        Map<String, Object> box = new HashMap<>();
        box.put("pagination", pagination);
        box.put("list", assetService.pagination(pagination));
        return box;
    }

//    @GetMapping("/transactionlog/{userId}")
//    public List<TranscationLogVO> getTransacDetail(@PathVariable Long userId) {
//        logger.info("/transaction_list");
//        List<TranscationLogVO> logs = assetService.transacList(userId);
//        return logs;
//    }

    @GetMapping("/total/{userId}")
    public HashMap totalAsset(@PathVariable Long userId) {
        logger.info("/total");
        box.clear();
        box.put("totalAsset", assetService.getOnesTotal(userId));
        box.put("tatalRatio", 999);
        box.put("totalProfit", 999);
        return box.get();
    }

    @GetMapping("/holdingCount/{userId}")
    public HashMap getHoling(@PathVariable Long userId) {
        logger.info("/holingCount");
        box.clear();
        box.put("holdingCount", assetService.getOnesHoldings(userId));
        return box.get();
    }

<<<<<<< HEAD
    @PostMapping("/sell")
    public void letSellStock(Map<String, Object> receipt){
        assetService.sellStock(receipt);
    }
}
=======

}
>>>>>>> master
