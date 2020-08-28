package com.muffin.web.asset;

import com.muffin.web.util.Pagination;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.muffin.web.asset.QTransaction.transaction;

interface ITransactionRepository {

    List<Transaction> pagination(Pagination pagination, Long userId);
}

@Repository
public class TransactionRepositoryImpl extends QuerydslRepositorySupport implements ITransactionRepository {

    private static final Logger logger = LoggerFactory.getLogger(TransactionRepositoryImpl.class);
    private final JPAQueryFactory queryFactory;
    private final DataSource dataSource;


    public TransactionRepositoryImpl(JPAQueryFactory queryFactory, DataSource dataSource) {
        super(Transaction.class);
        this.queryFactory = queryFactory;
        this.dataSource = dataSource;
    }

    @Override
    public List<Transaction> pagination(Pagination pagination, Long userId) {
        return queryFactory.selectFrom(transaction).where(transaction.userId.eq(userId)).orderBy(transaction.transactionId.desc())
                .offset(pagination.getStartList()).limit(pagination.getListSize()).fetch();
    }
}
