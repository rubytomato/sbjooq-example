package com.example.jooq.service;

import static com.example.jooq.db.Tables.*;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jooq.db.tables.records.PrefectureRecord;

@Service
public class PrefectureService {
  private static Logger logger = LoggerFactory.getLogger(PrefectureService.class);

  private final DSLContext create;

  @Autowired
  public PrefectureService(DSLContext dslContext) {
    this.create = dslContext;
  }

  public Result<PrefectureRecord> findAll() {
    Result<PrefectureRecord> result = create.selectFrom(PREFECTURE).fetch();
    return result;
  }

}
