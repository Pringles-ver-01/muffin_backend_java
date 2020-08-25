package com.muffin.web.asset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffin.web.board.Board;
import com.muffin.web.stock.Stock;
import com.muffin.web.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@ToString
@Table(name = "asset")
@NoArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id") private Long assetId;
    @Column(name = "purchase_price") private int purchasePrice;
    @Column(name = "share_count") private int shareCount;
    @Column(name = "total_asset") private int totalAsset;
    @Column(name = "total_profit") private int totalProfit;
    @Column(name = "total_profit_ratio") private double totalProfitRatio;
    @Column(name = "transaction_date") private String transactionDate;
    @Column(name = "transaction_type") private String transactionType;

    @Builder
    public Asset(int purchasePrice,
                 int shareCount,
                 int totalAsset,
                 int totalProfit,
                 double totalProfitRatio,
                 String transactionDate,
                 String transactionType, User user, Stock stock) {
        this.totalAsset = totalAsset;
        this.totalProfit = totalProfit;
        this.totalProfitRatio = totalProfitRatio;
        this.transactionDate = transactionDate;
        this.shareCount = shareCount;
        this.transactionType = transactionType;
        this.purchasePrice = purchasePrice;
        this.user = user;
        this.stock = stock;
    }

    @JsonIgnore @ManyToOne @JoinColumn(name="stock_id")
    private Stock stock;

    @JsonIgnore @ManyToOne @JoinColumn(name="user_id")
    public User user;

}