package com.muffin.web.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffin.web.board.Board;
import com.muffin.web.user.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Entity
@Table(name="comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long commentId;

    @Column(name="comment_content")
    private String commentContent;

    @Column(name="commnet_regdate")
    private String commentRegdate;

    @Override
    public String toString() {
        return String.format("Comment[commentContent=%s, commentRegdate=%s]", commentContent, commentRegdate);
    }

    @Builder
    public Comment(String commentContent, String commentRegdate, User user, Board board) {
        this.commentContent = commentContent;
        this.commentRegdate = commentRegdate;
        this.user = user;
        this.board = board;
    }

    @ManyToOne @JoinColumn(name="user_id") @JsonIgnore
    private User user;

    @ManyToOne @JoinColumn(name="board_id") @JsonIgnore
    private Board board;
}
