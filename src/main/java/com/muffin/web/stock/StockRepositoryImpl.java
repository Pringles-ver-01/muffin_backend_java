package com.muffin.web.stock;

import com.muffin.web.util.Pagination;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.muffin.web.stock.QStock.stock;

interface IStockRepository {

    List<Stock> pagination(Pagination pagination);

    List<String> findAllSymbol();

    String findBySymbol(String symbol);

    List<String> findMiniListed();

<<<<<<< HEAD
    Long findLongBySymbol(String symbol);

    List<String> findSymbolByName(String stockName); // 키워드로 심볼 찾기



//    List<String> paginationStock(Pagination pagination);
=======
    List<Stock> selectByStockNameLikeSearchWord(String stockSearch);

    Iterable<Stock> selectByStockNameLikeSearchWordPage(String stockSearch);
>>>>>>> master
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
        return queryFactory.select(stock.symbol)
                .from(stock)
                .fetch();
    }

    @Override
    public List<String> findMiniListed() {
        return queryFactory.select(stock.symbol)
                .from(stock)
                .limit(32)
                .fetch();
    }

    @Override
<<<<<<< HEAD
    public List<String> findSymbolByName(String stockName) {
        return queryFactory.select(stock.symbol)
                .where(stock.stockName.eq(stockName))
                .from(stock)
                .fetch();
=======
    public List<Stock> selectByStockNameLikeSearchWord(String stockSearch) {
        return queryFactory.selectFrom(stock)
                .where(stock.stockName.like("%"+stockSearch+"%"))
                .fetch();
    }

    @Override
    public Iterable<Stock> selectByStockNameLikeSearchWordPage(String stockSearch) {
        QStock qs = stock;
        List<Stock> result = new ArrayList<>();
        result = queryFactory.selectDistinct(Projections.fields(Stock.class,
                stock.stockId, stock.symbol, stock.stockName))
                .where(stock.stockName.like("%"+stockSearch+"%"))
                .from(stock)
                .orderBy(stock.stockId.desc())
                .limit(8)
                .fetch();
        System.out.println("stock result: "+result);
        System.out.println("stock result: "+result.size());
        return result;
>>>>>>> master
    }

    @Override
    public List<Stock> pagination(Pagination pagination) {
        return queryFactory.selectFrom(stock).orderBy(stock.stockId.asc())
                .offset(pagination.getStartList()).limit(pagination.getListSize()).fetch();
    }


    @Override
    public String findBySymbol(String symbol) {
        return queryFactory.select(stock.stockName)
                .from(stock)
                .fetchFirst();
    }

    @Override
    public Long findLongBySymbol(String symbol) {
        return queryFactory.select(stock.stockId)
                .where(stock.symbol.eq(symbol))
                .from(stock)
                .fetchOne();
    }


}




<<<<<<< HEAD
=======
}


>>>>>>> master
