package kr.co.person.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommonEntity is a Querydsl query type for CommonEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QCommonEntity extends EntityPathBase<CommonEntity> {

    private static final long serialVersionUID = 179999514L;

    public static final QCommonEntity commonEntity = new QCommonEntity("commonEntity");

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> updateDate = createDateTime("updateDate", java.time.LocalDateTime.class);

    public QCommonEntity(String variable) {
        super(CommonEntity.class, forVariable(variable));
    }

    public QCommonEntity(Path<? extends CommonEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommonEntity(PathMetadata metadata) {
        super(CommonEntity.class, metadata);
    }

}

