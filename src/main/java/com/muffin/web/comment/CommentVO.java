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
    private String commentContent, commentRegdate;
    private User user;
    private Board board;
}
