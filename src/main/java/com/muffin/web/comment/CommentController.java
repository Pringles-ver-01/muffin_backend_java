package com.muffin.web.comment;

import com.muffin.web.user.User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/insert")
    public void insert(@RequestBody CommentVO comment) {
        commentService.save(comment);
    }

    @PostMapping("/myComment")
    public Iterable<Comment> myComment(@RequestBody User user) {
        return commentService.findByUserId(user.getUserId());
    }

    @PostMapping("/update")
    public Optional<Comment> update(@RequestBody CommentVO comment) {
        return commentService.update(comment);
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(commentService.findById(id).get());
    }

}
