package com.muffin.web.comment;

import com.muffin.web.user.User;
import com.muffin.web.util.Pagination;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/comments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController {

    private final CommentService commentService;
    private final Pagination pagination;

    @PostMapping("/insert")
    public void insert(@RequestBody CommentVO comment) {
        commentService.save(comment);
    }

    @PostMapping("/myComment/{page}/{range}")
    public Map<?,?> myComment(@RequestBody User user, @PathVariable int page, @PathVariable int range ) {
        pagination.pageInfo(page, range, commentService.findByUserId(user.getUserId()).size());
        Map<String, Object> box = new HashMap<>();
        box.put("pagination", pagination);
        box.put("list", commentService.findByUserIdPagination(user.getUserId(), pagination));
        return box;
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(commentService.findByCommentId(id).get());
    }

    @GetMapping("/csv")
    public void csvRead() {
        commentService.readCsv();
    }

    @GetMapping("/detail/{id}")
    public List<CommentVO> boardDetail(@PathVariable Long id) {
        System.out.println(id);
        return commentService.findByBoardId(id);
    }

}
