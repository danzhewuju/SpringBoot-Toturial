package com.satan.mapper;

import com.satan.mode.ComplexQuery;
import com.satan.mode.ComplexQueryResult;
import com.satan.mode.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ComplexQueryMapper {
  List<User> QueryGame();

  List<User> QueryUsers(@Param("CQ") ComplexQuery complexQuery);

  List<ComplexQueryResult> QueryComplex(@Param("CQ") ComplexQuery complexQuery);
}
