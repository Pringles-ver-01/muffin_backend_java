package com.muffin.web.asset;

import com.amazonaws.services.alexaforbusiness.model.BusinessReportRecurrence;
import com.muffin.web.util.Pagination;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

import static com.muffin.web.asset.QAsset.asset;
import static com.muffin.web.stock.QStock.stock;
import static com.muffin.web.user.QUser.user;

interface IAssetRepository{
    Asset showOneData();

//    List<Asset> getTransacList(Long userId);

    List<Integer> getRecentTotal(Long userId);

    List<Asset> getHolingStocks(Long userId);

    List<Asset> pagination(Pagination pagination);
}

@Repository
public class AssetRepositoryImpl extends QuerydslRepositorySupport implements IAssetRepository {

    private static final Logger logger = LoggerFactory.getLogger(AssetRepositoryImpl.class);
    private final JPAQueryFactory queryFactory;
    private final DataSource dataSource;

    public AssetRepositoryImpl(JPAQueryFactory queryFactory, DataSource dataSource) {
        super(Asset.class);
        this.queryFactory = queryFactory;
        this.dataSource = dataSource;
    }


    @Override
    public Asset showOneData() {
        logger.info("AssetRepositoryImpl : public List<Integer> showOneData()");
        return queryFactory.select(Projections.fields(Asset.class,
                asset.shareCount, asset.totalAsset)).from(asset).fetchOne();
    }


//    @Override
//    public List<Asset> getTransacList(Long userId) {
//        logger.info("AssetRepositoryImpl : getTransacList()");
//        return queryFactory.select(Projections.fields(Asset.class,
//                asset.purchasePrice,
//                asset.shareCount,
//                asset.totalAsset,
//                asset.assetId,
//                asset.transactionDate,
//                asset.transactionType,
//                stock,
//                user))
//                .from(asset)
//                .innerJoin(user).on(asset.user.userId.eq(user.userId))
//                .innerJoin(stock).on(asset.stock.stockId.eq(stock.stockId))
//                .fetchJoin()
//                .where(asset.user.userId.eq(userId))
//                .fetch();
//    }

    @Override
    public List<Asset> getHolingStocks(Long userId){
        logger.info("AssetRepositoryImpl : getHolingStocks()");
        return queryFactory.select(Projections.fields(Asset.class,
                asset.purchasePrice,
                asset.shareCount,
                asset.totalAsset,
                asset.assetId,
                asset.transactionDate,
                asset.transactionType,
                stock,
                user)).from(asset)
                .innerJoin(user).on(asset.user.userId.eq(user.userId))
                .innerJoin(stock).on(asset.stock.stockId.eq(stock.stockId))
                .fetchJoin()
                .where(asset.user.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<Asset> pagination(Pagination pagination) {
        System.out.println(pagination);
        return queryFactory.selectFrom(asset).orderBy(asset.transactionDate.desc())
                .offset(pagination.getStartList()).limit(pagination.getListSize()).fetch();
    }

    @Override
    public List<Integer> getRecentTotal(Long userId) {
        logger.info("AssetRepositoryImpl : getRecentTotal()");
        return queryFactory.select(asset.totalAsset)
                .from(asset)
                .where(asset.user.userId.eq(user.userId))
                .orderBy(asset.transactionDate.asc())
                .limit(1)
                .fetch();
    }

}