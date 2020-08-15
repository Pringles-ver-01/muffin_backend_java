package com.muffin.web.asset;

import com.muffin.web.board.Board;
import com.muffin.web.stock.Stock;
import com.muffin.web.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "asset")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id") private Long assetId;
    @Column(name = "purchase_price") private int purchasePrice;
    @Column(name = "share_count") private int shareCount;
    @Column(name = "total_asset") private int totalAsset;
    @Column(name = "transaction_date") private String transactionDate;
    @Column(name = "transaction_type") private String transactionType;

    public Asset() {}

    @Builder
    public Asset(int purchasePrice,
                 int shareCount,
                 int totalAsset,
                 String transactionDate,
                 String transactionType, String s, String s1) {
        this.totalAsset = totalAsset;
        this.transactionDate = transactionDate;
        this.shareCount = shareCount;
        this.transactionType = transactionType;
        this.purchasePrice = purchasePrice;
    }

    @ManyToOne @JoinColumn(name="stock_name")
    private Stock stock;

    @ManyToOne @JoinColumn(name="user_id")
    private User user;

//    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
//    private List<Stock> stockList;

}
