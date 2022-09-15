package com.satan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("saber_job_run_history")
@Data
public class SaberJobRunHistoryBean{
    private int id;
    private String jobId;
    private String attempId;
    private byte status;
    private String startTime;
    private String endTime;
    private LocalDateTime ctime;
    private LocalDateTime mtime;
    private String version;
    private int operator;
    private byte runType;
    private String runDuration;
    private int retryTimes;
    private String fromSavePoint;
    private byte deleted;
}
