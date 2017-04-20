package kr.co.person.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAutoLogin is a Querydsl query type for AutoLogin
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAutoLogin extends EntityPathBase<AutoLogin> {

    private static final long serialVersionUID = -241551090L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAutoLogin autoLogin = new QAutoLogin("autoLogin");

    public final StringPath loginId = createString("loginId");

    public final NumberPath<Integer> loginIdx = createNumber("loginIdx", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final QUser user;

    public QAutoLogin(String variable) {
        this(AutoLogin.class, forVariable(variable), INITS);
    }

    public QAutoLogin(Path<? extends AutoLogin> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAutoLogin(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAutoLogin(PathMetadata metadata, PathInits inits) {
        this(AutoLogin.class, metadata, inits);
    }

    public QAutoLogin(Class<? extends AutoLogin> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

