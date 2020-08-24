package com.muffin.web.board;

import com.muffin.web.user.QUser;
import com.muffin.web.util.Pagination;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

interface IBoardRepository {

    List<Board> pagination(Pagination pagination);

    Iterable<Board> findAllBoardsByUserIdPagination(long id, Pagination pagination);

    List<Board> findAllBoardsByUserId(long id);

    List<Board> findByNicknameLikeSearchWord(String searchWord);

    Iterable<Board> selectByBoardTitleLikeSearchWordPage(String searchWord, Pagination pagination);

    Iterable<Board> findByNicknameLikeSearchWordPage(String searchWord, Pagination pagination);
}

@Repository
public class BoardRepositoryImpl extends QuerydslRepositorySupport implements IBoardRepository {

    private final JPAQueryFactory queryFactory;
    private final DataSource dataSource;


    public BoardRepositoryImpl(JPAQueryFactory queryFactory, DataSource dataSource) {
        super(Board.class);
        this.queryFactory = queryFactory;
        this.dataSource = dataSource;
    }

    @Override
    public List<Board> pagination(Pagination pagination) {
        QBoard qb = QBoard.board;
        return queryFactory.selectFrom(qb).orderBy(qb.boardId.desc())
                .offset(pagination.getStartList()).limit(pagination.getListSize()).fetch();
    }

    @Override
    public Iterable<Board> findAllBoardsByUserIdPagination(long id, Pagination pagination) {
        QBoard qb = QBoard.board;
        return queryFactory.selectFrom(qb).where(qb.user.userId.eq(id)).orderBy(qb.boardId.desc())
                .offset(pagination.getStartList()).limit(pagination.getListSize()).fetch();
    }

    @Override
    public List<Board> findAllBoardsByUserId(long id) {
        QBoard qb = QBoard.board;
        return queryFactory.selectFrom(qb).where(qb.user.userId.eq(id)).fetch();
    }

    @Override
    public List<Board> findByNicknameLikeSearchWord(String searchWord) {
        QBoard qb = QBoard.board;
        QUser qu = QUser.user;
        return queryFactory.selectFrom(qb)
                .join(qb.user, qu).fetchJoin()
                .where(qb.user.nickname.like("%"+searchWord+"%"))
                .fetch();
    }

    @Override
    public Iterable<Board> selectByBoardTitleLikeSearchWordPage(String searchWord, Pagination pagination) {
        QBoard qb = QBoard.board;
        return queryFactory.selectFrom(qb).where(qb.boardTitle.like("%"+searchWord+"%")).orderBy(qb.boardId.desc())
                .offset(pagination.getStartList()).limit(pagination.getListSize()).fetch();
    }

    @Override
    public Iterable<Board> findByNicknameLikeSearchWordPage(String searchWord, Pagination pagination) {
        QBoard qb = QBoard.board;
        QUser qu = QUser.user;
        return queryFactory.selectFrom(qb)
                .join(qb.user, qu).fetchJoin()
                .where(qb.user.nickname.like("%"+searchWord+"%"))
                .orderBy(qb.boardId.desc())
                .offset(pagination.getStartList()).limit(pagination.getListSize()).fetch();
    }
}
