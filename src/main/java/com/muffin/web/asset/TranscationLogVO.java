package com.muffin.web.asset;

import lombok.*;

@Getter @Setter @ToString
public class TranscationLogVO {
    private String transactionDate, transactionType, stockName;
    private int purchasePrice, shareCount, totalAsset;
}
