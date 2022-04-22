package com.satan.service.impl;

import com.satan.common.Result;
import com.satan.entity.CreateDirDo;
import com.satan.service.HdfsService;
import org.springframework.stereotype.Service;

@Service
public class ServiceImpl implements HdfsService {
    @Override
    public Result<String> hdfsCreateDir(CreateDirDo createDirDo) {
        return null;
    }
}
