package com.muffin.web.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -733150272L;

    public static final QUser user = new QUser("user");

    public final ListPath<com.muffin.web.asset.Asset, com.muffin.web.asset.QAsset> assetList = this.<com.muffin.web.asset.Asset, com.muffin.web.asset.QAsset>createList("assetList", com.muffin.web.asset.Asset.class, com.muffin.web.asset.QAsset.class, PathInits.DIRECT2);

    public final ListPath<com.muffin.web.board.Board, com.muffin.web.board.QBoard> boardList = this.<com.muffin.web.board.Board, com.muffin.web.board.QBoard>createList("boardList", com.muffin.web.board.Board.class, com.muffin.web.board.QBoard.class, PathInits.DIRECT2);

    public final ListPath<com.muffin.web.comment.Comment, com.muffin.web.comment.QComment> commentList = this.<com.muffin.web.comment.Comment, com.muffin.web.comment.QComment>createList("commentList", com.muffin.web.comment.Comment.class, com.muffin.web.comment.QComment.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath userid = createString("userid");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}
