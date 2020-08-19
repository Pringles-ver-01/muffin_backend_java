package com.muffin.web.asset;

import lombok.*;

@Getter @Setter @ToString
public class TranscationLogVO {
    private String transactionDate, transactionType, stockName, symbol;
    private int purchasePrice, shareCount, totalAsset, profitLoss, profitRatio, evaluatedSum;
    private boolean hasAsset;
}