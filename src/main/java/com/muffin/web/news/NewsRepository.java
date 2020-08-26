package com.muffin.web.news;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long>, INewsRepository {
    @Query ("select e from News e where e.newsTitle like %:newsSearch%")
    List<News> selectByNewsTitleLikeSearchWord(@Param("newsSearch") String newsSearch);
}