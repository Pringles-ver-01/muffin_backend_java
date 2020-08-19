package com.muffin.web.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, IBoardRepository {
    Page<Board> findByIdGreaterThan(Long id, Pageable paging);

    @Query("select e from Board e where e.boardRegdate like %:searchWord%")
    List<Board> findByNicknameLikeSearchWord(@Param("searchWord")String searchWord); //미완성

    @Query("select e from Board e where e.boardTitle like %:searchWord%")
    List<Board> selectTByBoardTitleLikeSearchWord(@Param("searchWord")String searchWord);
}
