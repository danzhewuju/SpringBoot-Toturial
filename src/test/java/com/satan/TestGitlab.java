package com.satan;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest()
public class TestGitlab {

    private static final Set<Integer> SUCCESS_CODES = Sets.newHashSet(
            HttpStatus.SC_OK,
            HttpStatus.SC_CREATED,
            HttpStatus.SC_ACCEPTED);

    public static <T> T sendGet(String url, List<NameValuePair> nameValuePairList, Map<String, String> headerInfo,
                                boolean returnIsString) throws Exception {
        JSONObject jsonObject = null;
        String result;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {

            client = HttpClients.createDefault();
            URIBuilder uriBuilder = new URIBuilder(url);

            if (CollectionUtils.isNotEmpty(nameValuePairList)) {
                uriBuilder.addParameters(nameValuePairList);
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
            httpGet.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
            if (headerInfo != null && !headerInfo.isEmpty()) {
                headerInfo.forEach((k, v) -> httpGet.setHeader(new BasicHeader(k, v)));
            }
            response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            if (SUCCESS_CODES.contains(statusCode)) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, "UTF-8");
                if (returnIsString) {
                    return (T) result;
                }
                try {
                    jsonObject = JSONObject.parseObject(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            } else {
                throw new RuntimeException("Request " + url + " error, error code: " + statusCode);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
        return (T) jsonObject;
    }

    @Test
    public void getTagInfo() throws Exception {
        String url = "https://git.bilibili.co/api/v4/projects/11719/repository/tags/sqlhint-for-latency-join-5";
        Map<String, String> headerInfo = ImmutableMap.of("PRIVATE-TOKEN", "N-Gjr1ohLwMAQMt8XCym");
        JSONObject object = sendGet(url, null, headerInfo, false);
        log.info(object.toString());
        log.info("create time : {}", object.getObject("commit", JSONObject.class).getLong("committed_date"));

    }
}
