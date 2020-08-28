package com.muffin.web.investProfile;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.sql.DataSource;
import java.util.Optional;

interface IInvestProfileRepository {

    InvestProfile findByUserId(Long userId);
}

public class InvestProfileRepositoryImpl extends QuerydslRepositorySupport implements IInvestProfileRepository {

    private final JPAQueryFactory queryFactory;
    private final DataSource dataSource;

    public InvestProfileRepositoryImpl(JPAQueryFactory queryFactory, DataSource dataSource) {
        super(InvestProfile.class);
        this.queryFactory = queryFactory;
        this.dataSource = dataSource;
    }

    @Override
    public InvestProfile findByUserId(Long userId) {
        QInvestProfile qi = QInvestProfile.investProfile;
        return queryFactory.selectFrom(qi).where(qi.user.userId.eq(userId)).fetchOne();
    }
}
