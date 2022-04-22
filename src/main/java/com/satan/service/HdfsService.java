package com.satan.service;

import com.satan.common.Result;
import com.satan.entity.CreateHdfsDirDo;
import com.satan.entity.DelHdfsDirDo;

import java.io.IOException;
import java.net.URISyntaxException;

public interface HdfsService {
    Result<String> hdfsCreateDir(CreateHdfsDirDo createHdfsDirDo) throws URISyntaxException, IOException;

    Result<String> hdfsDeleteDir(DelHdfsDirDo delHdfsDirDo);
}
