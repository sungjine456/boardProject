package kr.co.person.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1105988919L;

    public static final QUser user = new QUser("user");

    public final QCommonEntity _super = new QCommonEntity(this);

    public final StringPath access = createString("access");

    public final StringPath admin = createString("admin");

    public final StringPath email = createString("email");

    public final StringPath id = createString("id");

    public final NumberPath<Integer> idx = createNumber("idx", Integer.class);

    public final StringPath img = createString("img");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    //inherited
    public final DateTimePath<org.joda.time.DateTime> regDate = _super.regDate;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updateDate = _super.updateDate;

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

