package com.wtz.java.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author tiezhu@dtstack.com
 * @since 2022/2/8 星期二
 */
public class _01_HttpClient {

    public static void main(String[] args) throws IOException {
        final ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Charset.defaultCharset()).build();
        HttpClientBuilder.create().setDefaultConnectionConfig(connectionConfig).build();
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultConnectionConfig(connectionConfig).build();) {
            HttpPut httpPut = new HttpPut("http://172.16.21.49:8040/api/tiezhu/xiaohe_clas1/_stream_load?");
            httpPut.setHeader("Authorization", "Basic " + "cm9vdDo=");
            httpPut.setHeader("Content-Type", "text/html; charset=UTF-8");
            httpPut.setHeader("Expect", "100-continue");
            httpPut.setHeader("columns", "id,name,stu_name");
            httpPut.setHeader("merge_type", "MERGE");
            httpPut.setHeader("delete", "id<=>'1' and name<=>'sai' and stu_name<=>'cccc'");
            httpPut.setHeader("column_separator", ",");
            httpPut.setEntity(new ByteArrayEntity("1,sai,cccc".getBytes()));
            CloseableHttpResponse httpResponse = httpclient.execute(httpPut);
            int status = httpResponse.getStatusLine().getStatusCode();
            HttpEntity entity = httpResponse.getEntity();
            System.out.println("status: " + status);
            System.out.println("entity: " + (entity != null ? EntityUtils.toString(entity) : ""));
        }
    }
}
