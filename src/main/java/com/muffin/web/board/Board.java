package com.muffin.web.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffin.web.comment.Comment;
import com.muffin.web.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Entity
@Table(name="board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="board_id")
    private Long boardId;

    @Column(name = "board_title")
    private String boardTitle;
    @Column(name="board_content", length= 9000)
    private String boardContent;
    @Column(name="board_regdate")
    private String boardRegdate;
    @Column(name="view_cnt")
    private int viewCnt;

    @Override
    public String toString() {
        return String.format("Board[boardTitle=%s, boardContent='%s', boardRegdate='%s']", boardTitle, boardContent, boardRegdate);
    }

    @Builder
    public Board(String boardTitle, String boardContent, String boardRegdate, int viewCnt, User user, List<Comment> commentList) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardRegdate = boardRegdate;
        this.viewCnt = viewCnt;
        this.user = user;
        this.commentList.addAll(commentList);
    }

    @ManyToOne @JoinColumn(name="user_id") @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();
}
