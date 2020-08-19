package com.muffin.web.stock;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.muffin.web.stock.QStock.stock;

interface IStockRepository {

    List<String> findAllSymbol();

    String findBySymbol(String symbol);
}

@Repository
public class StockRepositoryImpl extends QuerydslRepositorySupport implements IStockRepository {

    private static final Logger logger = LoggerFactory.getLogger(StockRepositoryImpl.class);
    private final JPAQueryFactory queryFactory;
    private final DataSource dataSource;

    public StockRepositoryImpl(JPAQueryFactory queryFactory, DataSource dataSource) {
        super(Stock.class);
        this.queryFactory = queryFactory;
        this.dataSource = dataSource;
    }


    @Override
    public List<String> findAllSymbol() {
        logger.info("StockRepositoryImpl  : findAllSymbol()");
        return queryFactory.select(stock.symbol)
                .from(stock)
                .fetch();
    }

    @Override
    public String findBySymbol(String symbol) {
        return queryFactory.select(stock.stockName)
                .from(stock)
                .fetchFirst();
    }
}
//035720
