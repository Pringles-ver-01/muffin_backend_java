package com.muffin.web.asset;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAsset is a Querydsl query type for Asset
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAsset extends EntityPathBase<Asset> {

    private static final long serialVersionUID = 1513681252L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAsset asset = new QAsset("asset");

    public final NumberPath<Long> assetId = createNumber("assetId", Long.class);

    public final NumberPath<Integer> purchasePrice = createNumber("purchasePrice", Integer.class);

    public final NumberPath<Integer> shareCount = createNumber("shareCount", Integer.class);

    public final com.muffin.web.stock.QStock stock;

    public final NumberPath<Integer> totalAsset = createNumber("totalAsset", Integer.class);

    public final NumberPath<Integer> totalProfit = createNumber("totalProfit", Integer.class);

    public final NumberPath<Double> totalProfitRatio = createNumber("totalProfitRatio", Double.class);

    public final StringPath transactionDate = createString("transactionDate");

    public final StringPath transactionType = createString("transactionType");

    public final com.muffin.web.user.QUser user;

    public QAsset(String variable) {
        this(Asset.class, forVariable(variable), INITS);
    }

    public QAsset(Path<? extends Asset> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAsset(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAsset(PathMetadata metadata, PathInits inits) {
        this(Asset.class, metadata, inits);
    }

    public QAsset(Class<? extends Asset> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.stock = inits.isInitialized("stock") ? new com.muffin.web.stock.QStock(forProperty("stock")) : null;
        this.user = inits.isInitialized("user") ? new com.muffin.web.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

