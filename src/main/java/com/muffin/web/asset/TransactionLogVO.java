
package com.muffin.web.asset;

import lombok.*;

@Getter @Setter @ToString
public class TransactionLogVO {
    private Long userId, stockId, assetId;
    private String transactionDate, transactionType, stockName, symbol;
    private int purchasePrice, shareCount, totalAsset, profitLoss, evaluatedSum, nowPrice, totalProfit;
    private boolean hasAsset;
<<<<<<< HEAD:src/main/java/com/muffin/web/asset/TransactionLogVO.java
    private double profitRatio, totalProfitRatio;


}

=======
    private double profitRatio;
}
>>>>>>> master:src/main/java/com/muffin/web/asset/TranscationLogVO.java
