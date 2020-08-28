package com.muffin.web.asset;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "transaction")
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id") private Long transactionId;
    @Column(name = "stock_name") private String stockName;
    @Column(name = "money") private int money;
    @Column(name = "transaction_date") private String transactionDate;
    @Column(name = "transaction_type") private String transactionType;
    @Column(name = "userId") private Long userId;
    @Column(name = "total_asset") private int totalAsset;

    @Builder
    public Transaction(String stockName,
                       int money,
                       String transactionDate,
                       String transactionType,
                       Long userId,
                       int totalAsset
    ) {
        this.stockName = stockName;
        this.money = money;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.userId = userId;
        this.totalAsset = totalAsset;
    }
}
