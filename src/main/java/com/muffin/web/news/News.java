package com.muffin.web.news;

import com.muffin.web.board.Board;
import com.muffin.web.user.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id") private long newsId;
    @Column(name = "news_title") private String newsTitle;
    @Column(name = "news_regDate") private String newsRegDate;
    @Column(name = "news_content") private String newsContent;
    @Column(name = "saved") private String saved;
    @Column(name = "link") private String link;



    @Override
    public String toString() {
        return "News{" +
                "newsId=" + newsId +
                ", newsTitle='" + newsTitle + '\'' +
                ", newsRegDate='" + newsRegDate + '\'' +
                ", newsContent='" + newsContent + '\'' +
                ", saved='" + saved + '\'' +
                ", link='"+link+'\'' +
                '}';
    }

    @Builder
    News(String newsTitle, String newsRegDate, String newsContent, String saved, String link){
        this.newsTitle = newsTitle;
        this.newsRegDate = newsRegDate;
        this.newsContent = newsContent;
        this.saved = saved;
        this.link = link;
    }

    @ManyToOne @JoinColumn(name="user_id")
    private User user;
}
