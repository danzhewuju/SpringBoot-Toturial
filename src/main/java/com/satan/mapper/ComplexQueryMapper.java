package com.satan.mapper;

import com.satan.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ComplexQueryMapper {
  List<User> QueryGame();
}
