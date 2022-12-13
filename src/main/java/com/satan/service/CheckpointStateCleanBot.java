package com.satan.service;

import com.google.common.collect.ImmutableMap;
import com.satan.utils.HttpClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CheckpointStateCleanBot {
    private static Logger LOG = LoggerFactory.getLogger(CheckpointStateCleanBot.class);

    public static void sendDataToBot(String data) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=e2f7a04a-f133-4468-86c9-e3bc5fc4e57e";
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("msgtype", "text");
        requestData.put("text", ImmutableMap.of("content", data));
        try {
            HttpClientService.sendPost(url, requestData);
        } catch (Exception e) {
            LOG.info("Send data to Bot failed, error message : {} ", e.getMessage());
        }

    }

    public static void main(String[] args) {
        CheckpointStateCleanBot.sendDataToBot("发送测试~");

    }

}
