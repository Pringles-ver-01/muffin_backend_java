package com.muffin.web.news;

import com.muffin.web.board.Board;
import com.muffin.web.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "news")
public class News {
    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    public String getNewsRegDate() {
        return newsRegDate;
    }

    public void setNewsRegDate(String newsRegDate) {
        this.newsRegDate = newsRegDate;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }

    public String getNewsThumbnail() {
        return newsThumbnail;
    }

    public void setNewsThumbnail(String newsThumbnail) {
        this.newsThumbnail = newsThumbnail;
    }

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


    public News() { }
}
