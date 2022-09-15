package com.satan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.satan.entity.SaberJobRunHistoryBean;
import com.satan.mapper.SaberJobRunHistoryMapper;
import com.satan.service.SaberJobRunHistoryService;
import org.springframework.stereotype.Service;

@Service
public class SaberJobRunHistoryServiceImp extends ServiceImpl<SaberJobRunHistoryMapper, SaberJobRunHistoryBean>
        implements SaberJobRunHistoryService {
}
