package com.muffin.web.asset;

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

interface IAssetRepository{
    Asset showOneData();

    List<Asset> findTransacInfoList();

    List<Asset> getTransacList();

    List<Integer> getRecentTotal();
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

    @Override
    public List<Asset> findTransacInfoList() {
        logger.info("findTransacInfoList()");
        return queryFactory.selectFrom(asset).fetch();
    }

    @Override
    public List<Asset> getTransacList() {
        logger.info("AssetRepositoryImpl : getTransacList()");
        List<Asset> result = new ArrayList<>();
        result = queryFactory.selectFrom(asset)
                .where(asset.user.userId.eq(Long.valueOf(1)))
                .fetch();
        return result;

    }

    @Override
    public List<Integer> getRecentTotal() {
        logger.info("AssetRepositoryImpl : getRecentTotal()");
        return queryFactory.select(asset.totalAsset)
                .from(asset)
                .where(asset.user.userId.eq(Long.valueOf(1)))
                .orderBy(asset.transactionDate.asc())
                .fetch();
    }
}