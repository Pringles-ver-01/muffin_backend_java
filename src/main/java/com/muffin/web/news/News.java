package com.muffin.web.news;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffin.web.stock.Stock;
import com.muffin.web.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "news")
@NoArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id") private long newsId;
    @Column(name = "news_regdate") private String newsRegDate;
    @Column(name = "news_title") private String newsTitle;
    @Column(name="news_content", length= 8000) private String newsContent;
    @Column(name = "news_link", length= 8000) private String newsLink;
    @Column(name="news_thumbnail", length= 8000) private String newsThumbnail;

    @Builder
    News( String newsRegDate, String newsTitle,
          String newsContent, String newsLink, String newsThumbnail){
        this.newsRegDate = newsRegDate;
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.newsLink = newsLink;
        this.newsThumbnail = newsThumbnail;
    }

}