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

    public final StringPath emailId = createString("emailId");

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

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

