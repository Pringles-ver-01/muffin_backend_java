package com.muffin.web.comment;

import com.muffin.web.util.Box;
import com.muffin.web.util.GenericService;
import org.springframework.stereotype.Service;

import java.util.Optional;

interface CommentService extends GenericService<Comment> {

    void save(CommentVO comment);

    Iterable<Comment> findByUserId(Long id);

    Optional<Comment> update(CommentVO comment);
}

@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Iterable<Comment> findAll() {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public void delete(Comment comment) {

    }

    @Override
    public boolean exists(String id) {
        return false;
    }

    @Override
    public void save(CommentVO comment) {

    }

    @Override
    public Iterable<Comment> findByUserId(Long id) {
        return null;
    }

    @Override
    public Optional<Comment> update(CommentVO comment) {
        return Optional.empty();
    }
}
