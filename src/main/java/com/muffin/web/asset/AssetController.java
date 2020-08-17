package com.muffin.web.asset;

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
@RequestMapping("/assets")
public class AssetController {

    private static final Logger logger = LoggerFactory.getLogger(AssetController.class);
    private AssetService assetService;
    private Box box;

    @GetMapping("/csv")
    public void readCsv() {assetService.readCSV();}

    @GetMapping("/test")
    public Asset getData() {
        logger.info("/asset/test AssetController");
        return assetService.showData();
    }

    @GetMapping("/transactionlog")
    public List<TranscationLogVO> getTransacDetail() {
        logger.info("/transaction_list");
        List<TranscationLogVO> logs = assetService.transacList();
        return logs;
    }

    @GetMapping("/total")
    public HashMap totalAsset() {
        logger.info("/total");
        box.clear();
        box.put("totalAsset", assetService.getOnesTotal());
        box.put("tatalRatio", 999);
        box.put("totalProfit", 999);
        return box.get();
    }

}
