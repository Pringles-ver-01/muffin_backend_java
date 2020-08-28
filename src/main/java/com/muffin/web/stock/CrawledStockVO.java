package com.muffin.web.stock;

import lombok.*;

import java.util.Date;

@Getter @Setter @ToString
public class CrawledStockVO {
    private String stockName, symbol, date, now, open, high, low, close, volume, transacAmount, dod, capital;

}