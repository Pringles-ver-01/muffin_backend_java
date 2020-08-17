package com.muffin.web.investProfile;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInvestProfile is a Querydsl query type for InvestProfile
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInvestProfile extends EntityPathBase<InvestProfile> {

    private static final long serialVersionUID = -1823193492L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInvestProfile investProfile = new QInvestProfile("investProfile");

    public final StringPath investmentPeriod = createString("investmentPeriod");

    public final StringPath investmentPropensity = createString("investmentPropensity");

    public final NumberPath<Long> investProfileId = createNumber("investProfileId", Long.class);

    public final com.muffin.web.user.QUser user;

    public QInvestProfile(String variable) {
        this(InvestProfile.class, forVariable(variable), INITS);
    }

    public QInvestProfile(Path<? extends InvestProfile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInvestProfile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInvestProfile(PathMetadata metadata, PathInits inits) {
        this(InvestProfile.class, metadata, inits);
    }

    public QInvestProfile(Class<? extends InvestProfile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.muffin.web.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

