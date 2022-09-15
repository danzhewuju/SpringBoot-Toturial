package com.satan.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.satan.entity.SaberJobRunHistoryBean;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SaberJobRunHistoryMapper extends BaseMapper<SaberJobRunHistoryBean> {
}
