package com.santeut.party.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QParty is a Querydsl query type for Party
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QParty extends EntityPathBase<Party> {

    private static final long serialVersionUID = -717343346L;

    public static final QParty party = new QParty("party");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Integer> courseId = createNumber("courseId", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final DateTimePath<java.time.LocalDateTime> finished_at = createDateTime("finished_at", java.time.LocalDateTime.class);

    public final NumberPath<Integer> guildId = createNumber("guildId", Integer.class);

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final BooleanPath isLinked = createBoolean("isLinked");

    public final NumberPath<Integer> maxParticipants = createNumber("maxParticipants", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> mountainId = createNumber("mountainId", Integer.class);

    public final StringPath mountainName = createString("mountainName");

    public final NumberPath<Integer> participants = createNumber("participants", Integer.class);

    public final NumberPath<Integer> partyId = createNumber("partyId", Integer.class);

    public final StringPath partyName = createString("partyName");

    public final StringPath place = createString("place");

    public final DateTimePath<java.time.LocalDateTime> schedule = createDateTime("schedule", java.time.LocalDateTime.class);

    public final StringPath selectedCourse = createString("selectedCourse");

    public final DateTimePath<java.time.LocalDateTime> started_at = createDateTime("started_at", java.time.LocalDateTime.class);

    public final ComparablePath<Character> status = createComparable("status", Character.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QParty(String variable) {
        super(Party.class, forVariable(variable));
    }

    public QParty(Path<? extends Party> path) {
        super(path.getType(), path.getMetadata());
    }

    public QParty(PathMetadata metadata) {
        super(Party.class, metadata);
    }

}

