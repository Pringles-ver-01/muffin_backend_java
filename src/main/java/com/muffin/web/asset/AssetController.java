package com.muffin.web.asset;

import com.muffin.web.util.Box;
import com.querydsl.core.Tuple;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/transaction_list")
    public List<Asset> getTransacDetail() {
        logger.info("/transaction_list");
        return assetService.transacList();
    }

}
