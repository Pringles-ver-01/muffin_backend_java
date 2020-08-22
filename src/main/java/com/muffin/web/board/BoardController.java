package com.muffin.web.board;

import com.muffin.web.user.User;
import com.muffin.web.util.Pagination;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping("/boards")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BoardController {

    private final BoardService boardService;
    private final Pagination pagination;

    @GetMapping("/pagination/{page}/{range}")
    public Map<?,?> pagination(@PathVariable int page, @PathVariable int range) {
        System.out.println(page+", "+range);
        pagination.pageInfo(page, range, boardService.count());
        Map<String, Object> box = new HashMap<>();
        box.put("pagination", pagination);
        box.put("list", boardService.pagination(pagination));
        return box;
    }

    @GetMapping("/recentBoard")
    public List<BoardVO> recentBoard() {
        return boardService.recentBoard();
    }

    @GetMapping("/csv")
    public void csvRead() {
        boardService.readCsv();
    }

    @PostMapping("/insert")
    public void insert(@RequestBody BoardVO board) {
        boardService.save(board);
    }

    @GetMapping("/findAll")
    public Iterable<Board> findAll(){
        return boardService.findAll();
    }

    @GetMapping("/findOne/{id}")
    public Optional<Board> findOne(@PathVariable Long id) {
        return boardService.findByBoardId(id);
    }

    @GetMapping("/search/{searchWord}/{condition}/{page}/{range}")
    public Map<?,?> search(@PathVariable String searchWord,@PathVariable String condition, @PathVariable int page, @PathVariable int range) {
        System.out.println(searchWord);
        System.out.println(condition);
        System.out.println(page);
        System.out.println(range);
        pagination.pageInfo(page, range, boardService.findBySearchWord(searchWord, condition).size());
        Map<String, Object> box = new HashMap<>();
        box.put("pagination", pagination);
        box.put("list", boardService.findBySearchWordPage(searchWord, condition, pagination));
        return box;
    }

    @PostMapping("/myBoard/{page}/{range}")
    public Map<?,?> myBoard(@RequestBody User user, @PathVariable int page, @PathVariable int range) {
        pagination.pageInfo(page, range, boardService.findAllBoardsByUserId(user.getUserId()).size());
        Map<String, Object> box = new HashMap<>();
        box.put("pagination", pagination);
        box.put("list", boardService.findByEmailId(user.getUserId(), pagination));
        return box;
    }

    @PostMapping("/update")
    public void update(@RequestBody BoardVO board) {
        boardService.update(board);
    }

    @GetMapping("/delete/{boardId}")
    public void delete(@PathVariable Long boardId) {
        boardService.delete(boardService.findByBoardId(boardId).get());
    }
}
