package com.muffin.web.asset;

import com.muffin.web.util.Pagination;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.List;

import static com.muffin.web.asset.QAsset.asset;
import static com.muffin.web.stock.QStock.stock;
import static com.muffin.web.user.QUser.user;

interface IAssetRepository{
    Asset showOneData(Long userId);

//    List<Asset> getTransacList(Long userId);

    List<Integer> getRecentTotal(Long userId);

    List<Asset> findOnesAllAsset(Long userid); // 로그인한 유저의 모든 에셋

    List<Asset> pagination(Pagination pagination);

    Integer getOwnedShareCount(Long userId, String symbol); // 유저아이디와 주식도 같을 때, 해당 보유한 주식의 shareCount수 리턴

    Integer getOwnedStockCount(Long userId); // 유저가 가진 종목의 갯수

    void deleteAsset(Long assetId);

    void updateAsset(Asset update);
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
    public Asset showOneData(Long userId) {
        logger.info("AssetRepositoryImpl : public List<Integer> showOneData()");
        return queryFactory.select(Projections.fields(Asset.class,
                asset.shareCount))
                .from(asset)
                .where(asset.user.userId.eq(userId))
                .fetchOne();
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
    public List<Asset> findOnesAllAsset(Long userId){
        logger.info("AssetRepositoryImpl : findOnesAllAsset()");
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

    @Override
    public Integer getOwnedShareCount(Long userId, String symbol){
        return queryFactory.select(asset.shareCount)
                .from(asset)
                .innerJoin(user).on(asset.user.userId.eq(user.userId))
                .innerJoin(stock).on(asset.stock.stockId.eq(stock.stockId))
                .fetchJoin()
                .where(asset.user.userId.eq(userId))
                .where(asset.stock.symbol.eq(symbol))
                .fetchOne();
    }

    @Override
    public Integer getOwnedStockCount(Long userId) {
        return (int)queryFactory.select(asset.stock.stockName)
                .from(asset)
                .innerJoin(user).on(asset.user.userId.eq(user.userId))
                .innerJoin(stock).on(asset.stock.stockId.eq(stock.stockId))
                .fetchJoin()
                .where(asset.user.userId.eq(userId))
                .fetchCount();
    }

    @Override @Modifying @Transactional
    public void deleteAsset(Long assetId) {
        queryFactory.delete(asset)
                .where(asset.assetId.eq(assetId))
                .execute();
    }

    @Override @Modifying @Transactional
    public void updateAsset(Asset update) {
        queryFactory.update(asset)
                .where(asset.assetId.eq(update.getAssetId()))
//                .set(asset.assetId, update.getAssetId())
                .set(asset.purchasePrice, update.getPurchasePrice())
                .set(asset.shareCount, update.getShareCount())
                .set(asset.totalAsset, update.getTotalAsset())
                .set(asset.transactionDate, update.getTransactionDate())
                .set(asset.transactionType, update.getTransactionType())
//                .set(asset.user, update.getUser().getUserId())
//                .set(asset.stock, update.getStock().getStockId())
                .execute();
    }

}