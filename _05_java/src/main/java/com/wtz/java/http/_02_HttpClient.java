package com.wtz.java.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class _02_HttpClient {

    public void get() throws IOException {
        try (final CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet get = new HttpGet("http://localhost");
            HttpResponse response = null;
            try {
                response = client.execute(get);
            } catch (IOException e) {
                log.warn("The get failed.", e);
            }

            BufferedReader reader = null;
            try {
                assert response != null;
                reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            } catch (IOException e) {
                log.warn("The get failed.", e);
            }

            String line;
            while (true) {
                try {
                    if (!((line = reader.readLine()) != null)) {
                        break;
                    }
                } catch (IOException e) {
                    log.warn("The get failed.", e);
                }
            }
            log.info(line);
        }
    }
}
