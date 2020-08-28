package com.muffin.web.asset;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTransaction is a Querydsl query type for Transaction
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTransaction extends EntityPathBase<Transaction> {

    private static final long serialVersionUID = 16986706L;

    public static final QTransaction transaction = new QTransaction("transaction");

    public final NumberPath<Integer> money = createNumber("money", Integer.class);

    public final StringPath stockName = createString("stockName");

    public final NumberPath<Integer> totalAsset = createNumber("totalAsset", Integer.class);

    public final StringPath transactionDate = createString("transactionDate");

    public final NumberPath<Long> transactionId = createNumber("transactionId", Long.class);

    public final StringPath transactionType = createString("transactionType");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QTransaction(String variable) {
        super(Transaction.class, forVariable(variable));
    }

    public QTransaction(Path<? extends Transaction> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTransaction(PathMetadata metadata) {
        super(Transaction.class, metadata);
    }

}

