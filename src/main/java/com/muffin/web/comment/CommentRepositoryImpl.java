package com.muffin.web.comment;

import com.muffin.web.user.QUser;
import com.muffin.web.util.Pagination;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

interface ICommentRepository {

    List<Comment> findAllCommentByUserId(Long id);

    Iterable<Comment> findAllCommentByUserIdPagination(Long id, Pagination pagination);

    List<Comment> findByBoardId(Long id);
}

@Repository
public class CommentRepositoryImpl extends QuerydslRepositorySupport implements ICommentRepository {

    private final JPAQueryFactory queryFactory;
    private final DataSource dataSource;

    public CommentRepositoryImpl(JPAQueryFactory queryFactory, DataSource dataSource) {
        super(Comment.class);
        this.queryFactory = queryFactory;
        this.dataSource = dataSource;
    }

    @Override
    public List<Comment> findAllCommentByUserId(Long id) {
        QComment qc = QComment.comment;
        return queryFactory.selectFrom(qc)
                .where(qc.user.userId.eq(id)).fetch();
    }

    @Override
    public Iterable<Comment> findAllCommentByUserIdPagination(Long id, Pagination pagination) {
        QComment qc = QComment.comment;
        return queryFactory.selectFrom(qc).where(qc.user.userId.eq(id))
                .orderBy(qc.commentId.desc()).offset(pagination.getStartList())
                .limit(pagination.getListSize()).fetch();
    }

    @Override
    public List<Comment> findByBoardId(Long id) {
        QComment qc = QComment.comment;
        return queryFactory.selectFrom(qc).where(qc.board.boardId.eq(id)).fetch();
    }


}
