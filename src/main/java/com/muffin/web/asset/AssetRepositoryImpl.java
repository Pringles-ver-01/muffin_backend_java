package com.muffin.web.asset;

import com.muffin.web.stock.QStock;
import com.muffin.web.user.QUser;
import com.muffin.web.util.Box;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.List;

import static com.muffin.web.asset.QAsset.asset;

interface IAssetRepository{
    Asset showOneData();

    List<Asset> findTransacInfoList();

    List<Asset> getTransacList();
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
        QUser user = QUser.user;
        return queryFactory.select(Projections.fields(Asset.class,
                asset.assetId,
                asset.totalAsset,
                asset.transactionType,
                asset.transactionDate,
                asset.purchasePrice,
                user.id
                )).from(asset)
                .innerJoin(user).on(user.id.eq(asset.user.id))
                .fetchJoin()
                .fetch();
    }
}
