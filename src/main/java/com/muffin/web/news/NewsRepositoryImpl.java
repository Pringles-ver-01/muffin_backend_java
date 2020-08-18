package com.muffin.web.news;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static com.muffin.web.news.QNews.news;

interface INewsRepository {

    List<News> showAllNews();

    News showNewsDetail(Long newsId);
}

@Repository
public class NewsRepositoryImpl extends QuerydslRepositorySupport implements INewsRepository {
    private final JPAQueryFactory queryFactory;
    private final DataSource dataSource;


    public NewsRepositoryImpl(JPAQueryFactory queryFactory, DataSource dataSource) {
        super(News.class);
        this.queryFactory = queryFactory;
        this.dataSource = dataSource;
    }


    @Override
    public List<News> showAllNews() {
        List<News> result = new ArrayList<>();
        result = queryFactory.select(Projections.fields(News.class,
                news.newsId, news.newsTitle, news.newsRegDate, news.newsThumbnail))
                .from(news)
                .fetch();
        System.out.println(result);
        return result;

    }

    @Override
    public News showNewsDetail(Long newsId) {
        return queryFactory.select(Projections.fields(News.class,
                news.newsTitle, news.newsRegDate, news.newsThumbnail, news.newsLink, news.newsContent))
                .where(news.newsId.eq(newsId))
                .from(news)
                .fetchOne();

    }
}
