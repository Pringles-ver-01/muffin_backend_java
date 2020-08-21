package com.muffin.web.board;

import com.muffin.web.comment.CommentRepository;
import com.muffin.web.user.UserRepository;
import com.muffin.web.util.GenericService;
import com.muffin.web.util.Pagination;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

interface BoardService extends GenericService<Board> {

    void readCsv();

    void save(BoardVO board);

    List<Board> findBySearchWord(String searchWord, String condition);

    List<BoardVO> findByEmailId(long id, Pagination pagination);

    List<BoardVO> recentBoard();

    List<BoardVO> pagination(Pagination pagination);

    List<Board> findAllBoardsByUserId(long id);

    Optional<Board> findByBoardId(Long id);

    void update(BoardVO board);

    Object findBySearchWordPage(String searchWord, String condition, Pagination pagination);
}

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public Optional<Board> findByBoardId(Long id) {
        return repository.findById(id);
    }

    @Override
    public void update(BoardVO board) {
        Board b = null;
        Optional<Board> findBoard = repository.findById(board.getBoardId());
        b = findBoard.orElseGet(Board::new);
        b.setUser(userRepository.findById(board.getUserId()).get());
        b.setBoardContent(board.getBoardContent());
        b.setBoardRegdate(board.getBoardRegdate());
        b.setBoardTitle(board.getBoardTitle());
        b.setViewCnt(board.getViewCnt());
        repository.save(b);
    }

    @Override
    public List<BoardVO> findBySearchWordPage(String searchWord, String condition, Pagination pagination) {
        List<BoardVO> result = new ArrayList<>();
        Iterable<Board> findBoard = null;
        switch(condition) {
            case "boardTitle": findBoard = repository.selectByBoardTitleLikeSearchWordPage(searchWord, pagination);
                break;
            case "nickname": findBoard =  repository.findByNicknameLikeSearchWordPage(searchWord, pagination);
                break;
            default: return null;
        }
        return getBoardVOS(result, findBoard);
    }

    private List<BoardVO> getBoardVOS(List<BoardVO> result, Iterable<Board> findBoard) {
        findBoard.forEach(board -> {
            BoardVO vo = new BoardVO();
            vo.setBoardId(board.getBoardId());
            vo.setBoardTitle(board.getBoardTitle());
            vo.setBoardContent(board.getBoardContent());
            vo.setBoardRegdate(board.getBoardRegdate());
            vo.setViewCnt(board.getViewCnt());
            vo.setNickname(board.getUser().getNickname());
            vo.setUserId(board.getUser().getUserId());
            if(board.getCommentList().size() == 0 || board.getCommentList() == null) {
                vo.setCommentList(new ArrayList<>());
            } else {
                vo.setCommentList(board.getCommentList());
            }
            result.add(vo);
        });
        return result;
    }

    @Override
    public Iterable<Board> findAll() {
        return repository.findAll();
    }

    @Override
    public int count() {
        return (int)repository.count();
    }

    @Override
    public void delete(Board board) {
        repository.delete(board);
    }

    @Override
    public boolean exists(String id) {
        return repository.existsById(Long.parseLong(id));
    }

    @Override
    public void readCsv() {
        InputStream is = getClass().getResourceAsStream("/static/opinion.csv");
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for(CSVRecord csvRecord : csvRecords){
                long i = 1;
                repository.save(new Board(
                        csvRecord.get(1),
                        csvRecord.get(2),
                        csvRecord.get(3),
                        Integer.parseInt(csvRecord.get(4)),
                                userRepository.findById(Long.parseLong(csvRecord.get(5))+1).get(),
                                commentRepository.findByBoardId(i)
                                )
                         );
                i = i + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void save(BoardVO board) {
        Board b = new Board(board.getBoardTitle(), board.getBoardContent(), board.getBoardRegdate(), board.getViewCnt(), board.getUser(), new ArrayList<>());
        repository.save(b);
    }

    @Override
    public List<Board> findBySearchWord(String searchWord, String condition) {
        switch(condition) {
            case "boardTitle": return repository.selectByBoardTitleLikeSearchWord(searchWord);
            case "nickname": return repository.findByNicknameLikeSearchWord(searchWord);
            default: return null;
        }
    }

    @Override
    public List<BoardVO> findByEmailId(long id, Pagination pagination) {
        List<BoardVO> result = new ArrayList<>();
        Iterable<Board> myBoard = repository.findAllBoardsByUserIdPagination(id, pagination);
        return getBoardVOS(result, myBoard);
    }

    @Override
    public List<BoardVO> recentBoard() {
        List<BoardVO> result = new ArrayList<>();
        Iterable<Board> findBoard = repository.findByBoardIdGreaterThan(0L, PageRequest.of(0, 5, Sort.Direction.DESC, "boardId"));
        return getBoardVOS(result, findBoard);
    }

    @Override
    public List<BoardVO> pagination(Pagination pagination) {
        List<BoardVO> result = new ArrayList<>();
        List<Board> findBoard = repository.pagination(pagination);
        return getBoardVOS(result, findBoard);
    }

    @Override
    public List<Board> findAllBoardsByUserId(long id) {
        return repository.findAllBoardsByUserId(id);
    }
}
