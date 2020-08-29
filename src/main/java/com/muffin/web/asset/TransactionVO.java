package com.muffin.web.asset;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class TransactionVO {
    private Long userId;
    private int money, totalAsset;
    private String stockName, transactionDate, transactionType;
}
