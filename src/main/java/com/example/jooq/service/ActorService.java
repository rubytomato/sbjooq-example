package com.example.jooq.service;

import static com.example.jooq.db.Tables.*;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jooq.db.tables.records.ActorRecord;

@Service
public class ActorService {
  private static Logger logger = LoggerFactory.getLogger(ActorService.class);

  private final DSLContext create;

  @Autowired
  public ActorService(DSLContext dslContext) {
    this.create = dslContext;
  }

  public Result<ActorRecord> findAll() {
    Result<ActorRecord> result = create.selectFrom(ACTOR).fetch();
    return result;
  }

  public ActorRecord findOne(Integer id){
    return create.selectFrom(ACTOR).where(ACTOR.ID.eq(id)).fetchOne();
  }

  public ActorRecord insert(ActorRecord actor) {
    return create.insertInto(ACTOR, ACTOR.NAME, ACTOR.HEIGHT, ACTOR.BLOOD, ACTOR.BIRTHDAY, ACTOR.BIRTHPLACE_ID)
                 .values(actor.getName(), actor.getHeight(), actor.getBlood(), actor.getBirthday(), actor.getBirthplaceId())
                 .returning()
                 .fetchOne();
  }

  public ActorRecord save(ActorRecord actor) {
    return create.update(ACTOR)
                 .set(actor)
                 .returning()
                 .fetchOne();
  }

  public int delete(Integer id) {
    return create.deleteFrom(ACTOR).where(ACTOR.ID.eq(id)).execute();
  }

}
