package com.muffin.web.comment;

import com.muffin.web.board.Board;
import com.muffin.web.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommentVO {
    private Long commentId;
    private String commentContent, commentRegdate, nickname;
    private User user;
    private Board board;
}
