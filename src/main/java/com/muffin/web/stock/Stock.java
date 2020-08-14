package com.muffin.web.stock;

import com.muffin.web.asset.Asset;
import com.muffin.web.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id") private Long stockId;
    @Column(name = "symbol") private int symbol;
    @Column(name = "market") private String market;
    @Column(name = "stock_name") private String stockName;
    @Column(name = "category_code") private int categoryCode;
    @Column(name = "category") private String category;

    @Builder
    public Stock(int symbol, String market, String stockName, int categoryCode, String category) {
        this.symbol = symbol;
        this.market = market;
        this.stockName = stockName;
        this.categoryCode = categoryCode;
        this.category = category;
    }

    @ManyToOne @JoinColumn(name="asset_id")
    private Asset asset;
}
