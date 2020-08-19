package com.muffin.web.board;

import com.muffin.web.user.UserRepository;
import com.muffin.web.util.GenericService;
import com.muffin.web.util.Pagination;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
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

    Optional<Board> update(BoardVO board);

    List<Board> findBySearchWord(String searchWord, String condition);

    List<BoardVO> findByUserId(long id, Pagination pagination);

    Page<Board> recentBoard();

    List<BoardVO> pagination(Pagination pagination);

    List<Board> findAllBoardsByUserId(long id);
}

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;
    private final UserRepository userRepository;

    public BoardServiceImpl(BoardRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Board> findById(Long id) {
        return repository.findById(id);
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
                repository.save(new Board(
                        csvRecord.get(1),
                        csvRecord.get(2),
                        csvRecord.get(3),
                        Integer.parseInt(csvRecord.get(4)),
                                userRepository.findById(Long.parseLong(csvRecord.get(5))+1).get(),
                                new ArrayList<>())
                         );
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
    public Optional<Board> update(BoardVO board) {
        return Optional.empty();
    }

    @Override
    public List<Board> findBySearchWord(String searchWord, String condition) {
        switch(condition) {
            case "boardTitle": return repository.selectTByBoardTitleLikeSearchWord(searchWord);
            case "nickname": return repository.findByNicknameLikeSearchWord(searchWord);
            default: return null;
        }
    }

    @Override
    public List<BoardVO> findByUserId(long id, Pagination pagination) {
        List<BoardVO> result = new ArrayList<>();
        Iterable<Board> myBoard = repository.findAllBoardsByUserIdPagination(id, pagination);
        myBoard.forEach(board -> {
            BoardVO vo = new BoardVO();
            vo.setId(board.getId());
            vo.setBoardTitle(board.getBoardTitle());
            vo.setBoardContent(board.getBoardContent());
            vo.setBoardRegdate(board.getBoardRegdate());
            vo.setViewCnt(board.getViewCnt());
            vo.setNickname(board.getUser().getNickname());
            vo.setUserId(board.getUser().getUserId());
            if(vo.getCommentList() == null) {
                vo.setCommentList(new ArrayList<>());
            } else {
                vo.getCommentList().addAll(board.getCommentList());
            }
            result.add(vo);
        });
        return result;
    }

    @Override
    public Page<Board> recentBoard() {
        return repository.findByIdGreaterThan(0L, PageRequest.of(0, 5, Sort.Direction.DESC, "id"));
    }

    @Override
    public List<BoardVO> pagination(Pagination pagination) {
        List<BoardVO> result = new ArrayList<>();
        List<Board> list = repository.pagination(pagination);
        BoardVO vo = null;
        for(Board b : list){
            vo = new BoardVO();
            vo.setId(b.getId());
            vo.setBoardTitle(b.getBoardTitle());
            vo.setBoardContent(b.getBoardContent());
            vo.setBoardRegdate(b.getBoardRegdate());
            vo.setViewCnt(b.getViewCnt());
            vo.setNickname(b.getUser().getNickname());
            vo.setUserId(b.getUser().getUserId());
            if(vo.getCommentList() == null) {
                vo.setCommentList(new ArrayList<>());
            } else {
                vo.getCommentList().addAll(b.getCommentList());
            }
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<Board> findAllBoardsByUserId(long id) {
        return repository.findAllBoardsByUserId(id);
    }
}
