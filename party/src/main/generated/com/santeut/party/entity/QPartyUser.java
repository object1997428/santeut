package com.santeut.party.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPartyUser is a Querydsl query type for PartyUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPartyUser extends EntityPathBase<PartyUser> {

    private static final long serialVersionUID = -116056455L;

    public static final QPartyUser partyUser = new QPartyUser("partyUser");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Integer> bestHeight = createNumber("bestHeight", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Integer> distance = createNumber("distance", Integer.class);

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final BooleanPath isSuccess = createBoolean("isSuccess");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> mountainId = createNumber("mountainId", Integer.class);

    public final NumberPath<Integer> moveTime = createNumber("moveTime", Integer.class);

    public final NumberPath<Integer> partyId = createNumber("partyId", Integer.class);

    public final NumberPath<Integer> partyUserId = createNumber("partyUserId", Integer.class);

    public final ComparablePath<org.locationtech.jts.geom.Geometry> points = createComparable("points", org.locationtech.jts.geom.Geometry.class);

    public final DateTimePath<java.time.LocalDateTime> started_at = createDateTime("started_at", java.time.LocalDateTime.class);

    public final ComparablePath<Character> status = createComparable("status", Character.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QPartyUser(String variable) {
        super(PartyUser.class, forVariable(variable));
    }

    public QPartyUser(Path<? extends PartyUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPartyUser(PathMetadata metadata) {
        super(PartyUser.class, metadata);
    }

}

