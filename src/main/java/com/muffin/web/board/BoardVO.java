package com.muffin.web.board;

import com.muffin.web.comment.Comment;
import com.muffin.web.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardVO {
    private Long boardId, userId;
    private String boardTitle, boardContent, boardRegdate, nickname;
    private int viewCnt;
    private User user;
    private List<Comment> commentList;
}
