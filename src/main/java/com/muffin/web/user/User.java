package com.muffin.web.user;

import com.muffin.web.asset.Asset;
<<<<<<< HEAD
import com.muffin.web.board.Board;
import com.muffin.web.comment.Comment;
import com.muffin.web.investProfile.InvestProfile;
import com.muffin.web.news.News;
=======
>>>>>>> origin/yerimm
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< HEAD
    @Column(name = "user_id")
    private Long userId;
=======
    @Column(name="user_id")
    private Long userId;

>>>>>>> yoonjung
    @Column(name = "email_id")
    private String emailId;
    @Column(name = "password")
    private String password;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return String.format("User[email_id=%s, password='%s', name='%s', nickname='%s]", emailId, password, name, nickname);
    }

    @Builder
    public User(String emailId, String password, String nickname, String name) {
        this.emailId = emailId;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Asset> assetList;

<<<<<<< HEAD
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private InvestProfile investProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<News> news;

=======
>>>>>>> origin/yerimm
}