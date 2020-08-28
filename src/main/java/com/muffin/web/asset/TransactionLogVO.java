package com.muffin.web.asset;

import lombok.*;

@Getter @Setter @ToString
public class TransactionLogVO {
    private Long userId, stockId, assetId;
    private String transactionDate, transactionType, stockName, symbol;
    private int purchasePrice, shareCount, totalAsset, profitLoss, evaluatedSum, nowPrice, totalProfit;
    private boolean hasAsset;
    private double profitRatio, totalProfitRatio;


}
