package com.satan.service;

import com.satan.common.Result;
import com.satan.entity.CreateDirDo;

import java.sql.ResultSet;

public interface HdfsService {
    public Result<String> hdfsCreateDir(CreateDirDo createDirDo);
}
