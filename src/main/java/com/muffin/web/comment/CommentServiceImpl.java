package com.muffin.web.comment;

import com.muffin.web.board.BoardRepository;
import com.muffin.web.user.UserRepository;
import com.muffin.web.util.GenericService;
import com.muffin.web.util.Pagination;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

interface CommentService extends GenericService<Comment> {

    void save(CommentVO comment);

    List<Comment> findByUserId(Long id);

    Optional<Comment> findByCommentId(Long id);

    void readCsv();

    List<CommentVO> findByUserIdPagination(Long id, Pagination pagination);

    List<CommentVO> findByBoardId(Long id);
}

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Override
    public Optional<Comment> findByCommentId(Long id) {
        return repository.findById(id);
    }

    @Override
    public void readCsv() {
        InputStream is = getClass().getResourceAsStream("/static/comments.csv");
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for(CSVRecord csvRecord : csvRecords) {
                repository.save(new Comment(csvRecord.get(0), csvRecord.get(1),
                        userRepository.findById(Long.parseLong(csvRecord.get(2))).get(),
                        boardRepository.findById(Long.parseLong(csvRecord.get(3))).get()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CommentVO> findByUserIdPagination(Long id, Pagination pagination) {
        List<CommentVO> result = new ArrayList<>();
        Iterable<Comment> myComment = repository.findAllCommentByUserIdPagination(id, pagination);
        return getCommentVOS(result, myComment);
    }

    @Override
    public List<CommentVO> findByBoardId(Long id) {
        List<CommentVO> result = new ArrayList<>();
        Iterable<Comment> comments = repository.findByBoardId(id);
        return getCommentVOS(result, comments);
    }

    private List<CommentVO> getCommentVOS(List<CommentVO> result, Iterable<Comment> myBoard) {
        myBoard.forEach(comment -> {
            CommentVO vo = new CommentVO();
            vo.setCommentId(comment.getCommentId());
            vo.setCommentContent(comment.getCommentContent());
            vo.setCommentRegdate(comment.getCommentRegdate());
            vo.setUser(comment.getUser());
            vo.setBoard(comment.getBoard());
            vo.setNickname(comment.getUser().getNickname());
            result.add(vo);
        });
        return result;
    }

    @Override
    public Iterable<Comment> findAll() {
        return null;
    }

    @Override
    public int count() {
        return (int)repository.count();
    }

    @Override
    public void delete(Comment comment) {
        repository.delete(comment);
    }

    @Override
    public boolean exists(String id) {
        return repository.existsById(Long.parseLong(id));
    }

    @Override
    public void save(CommentVO comment) {
        Comment c = new Comment(comment.getCommentContent(), comment.getCommentRegdate(), comment.getUser(), comment.getBoard());
        repository.save(c);
    }

    @Override
    public List<Comment> findByUserId(Long id) {
        return repository.findAllCommentByUserId(id);
    }
}
