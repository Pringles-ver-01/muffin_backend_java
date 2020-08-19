package com.muffin.web.stock;

import com.muffin.web.asset.Asset;
import com.muffin.web.board.Board;
import com.muffin.web.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "symbol") private String symbol;
    @Column(name = "stock_name") private String stockName;

    @Builder
    public Stock(String symbol, String stockName, List<Asset> assetList) {
        this.symbol = symbol;
        this.stockName = stockName;
        this.assetList.addAll(assetList);
    }

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL)
    private List<Asset> assetList = new ArrayList<>();

}