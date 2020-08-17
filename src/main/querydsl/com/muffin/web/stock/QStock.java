package com.muffin.web.stock;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStock is a Querydsl query type for Stock
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStock extends EntityPathBase<Stock> {

    private static final long serialVersionUID = -894905040L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStock stock = new QStock("stock");

    public final com.muffin.web.asset.QAsset asset;

    public final StringPath category = createString("category");

    public final NumberPath<Integer> categoryCode = createNumber("categoryCode", Integer.class);

    public final StringPath market = createString("market");

    public final NumberPath<Long> stockId = createNumber("stockId", Long.class);

    public final StringPath stockName = createString("stockName");

    public final NumberPath<Integer> symbol = createNumber("symbol", Integer.class);

    public QStock(String variable) {
        this(Stock.class, forVariable(variable), INITS);
    }

    public QStock(Path<? extends Stock> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStock(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStock(PathMetadata metadata, PathInits inits) {
        this(Stock.class, metadata, inits);
    }

    public QStock(Class<? extends Stock> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.asset = inits.isInitialized("asset") ? new com.muffin.web.asset.QAsset(forProperty("asset"), inits.get("asset")) : null;
    }

}

