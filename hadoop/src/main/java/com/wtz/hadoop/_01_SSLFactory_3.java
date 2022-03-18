package com.wtz.hadoop;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class _01_SSLFactory_3 {

    public static void main(String[] args) throws Exception {
        test1();
    }

    public static void test1() throws Exception {
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(
//                AuthScope.ANY,
//                new UsernamePasswordCredentials(
//                        "admin","admin"));

        String url = "https://lt-cdp-node3:8443/gateway/cdp-proxy/yarn/proxy/application_1646813310255_0050/jobs/c11a6a74da00a0e1aefeeb77f973c673/accumulators";
        HttpGet httpGet = new HttpGet(url);
        String auth = "admin" + ":" + "admin";
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth);
        // 将验证信息放入到 Header
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);


        CloseableHttpClient httpClient = null;
        if (url.startsWith("https")) {
            File cert = new File("/Users/wtz/work_place/project_place/tools/hadoop/src/main/resources/cm-auto-global_truststore.jks");
            String keystore = "JAdpyw95cavPwYr5gxBJAcPSlohcrXgdPbv6Fg9hc4E";
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(cert, keystore.toCharArray(), new TrustSelfSignedStrategy()).build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1.2"},
                    null, NoopHostnameVerifier.INSTANCE);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();


        } else {
            httpClient = HttpClients.createDefault();
        }

        try (CloseableHttpClient _httpClient = httpClient;
             CloseableHttpResponse res = _httpClient.execute(httpGet);) {
            if (res != null) {
                StatusLine sl = res.getStatusLine();
                if (sl != null) {
                    System.out.println(sl.getStatusCode());
                    StringBuilder builder = new StringBuilder();
                    try (InputStream is = res.getEntity().getContent();
                         InputStreamReader isr = new InputStreamReader(is);
                         BufferedReader br = new BufferedReader(isr);) {
                        String line = br.readLine();
                        while (line != null) {
                            builder.append(line);
                            line = br.readLine();
                        }
                        System.out.println("响应结果：" + builder);
                    }
                }
            }
        }

    }
}
