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
<<<<<<< HEAD
    @Column(name = "asset_id") private long assetId;
    @Column(name = "total_asset") private int totalAsset;
    @Column(name = "transaction_date") private Date transactionDate;
=======
    @Column(name = "asset_id") private Long assetId;
>>>>>>> origin/yerimm
    @Column(name = "purchase_price") private int purchasePrice;
    @Column(name = "share_count") private int shareCount;
    @Column(name = "total_asset") private int totalAsset;
    @Column(name = "transaction_date") private String transactionDate;
    @Column(name = "transaction_type") private String transactionType;

    @Builder
<<<<<<< HEAD
    public Asset(int totalAsset, Date transactionDate, int purchasePrice, int shareCount, boolean transactionType) {
        this.totalAsset = totalAsset;
        this.transactionDate = transactionDate;
        this.purchasePrice = purchasePrice;
=======
    public Asset(int purchasePrice,
                 int shareCount,
                 int totalAsset,
                 String transactionDate,
                 String transactionType, User user, Stock stock) {
        this.totalAsset = totalAsset;
        this.transactionDate = transactionDate;
>>>>>>> origin/yerimm
        this.shareCount = shareCount;
        this.transactionType = transactionType;
        this.purchasePrice = purchasePrice;
        this.user = user;
        this.stock = stock;
    }

    @JsonIgnore @ManyToOne @JoinColumn(name="stock_id")
    private Stock stock;

    @ManyToOne @JoinColumn(name="user_id")
    private User user;

}

