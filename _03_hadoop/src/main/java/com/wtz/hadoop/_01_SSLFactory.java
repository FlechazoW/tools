package com.wtz.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.ssl.KeyStoresFactory;
import org.apache.hadoop.security.ssl.SSLFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.GeneralSecurityException;


/**
 * @author tiezhu@dtstack.com
 * @since 2022/1/20 星期四
 */
public class _01_SSLFactory {

    private static final int CONNECTION_TIMEOUT_MILLI_SECOND = 3000;

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        Configuration conf = new Configuration();
        SSLFactory sslFactory = new SSLFactory(SSLFactory.Mode.CLIENT, conf);

        sslFactory.init();
        final KeyStoresFactory kf = sslFactory.getKeystoresFactory();
        final HostnameVerifier hv = sslFactory.getHostnameVerifier();

        RequestConfig requestConfig =
                RequestConfig.custom()
                        .setSocketTimeout(CONNECTION_TIMEOUT_MILLI_SECOND)
                        .setConnectTimeout(CONNECTION_TIMEOUT_MILLI_SECOND)
                        .build();

        ConnectionConfig config = ConnectionConfig.custom().build();

        RegistryBuilder registryBuilder = RegistryBuilder.create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        SSLContext sslContext =
                SSLContexts.custom().loadTrustMaterial((chain, authType) -> true).build();
        sslContext.init(kf.getKeyManagers(), kf.getTrustManagers(), null);

        LayeredConnectionSocketFactory sslSF =
                new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        registryBuilder.register("https", sslSF);

        Registry registry = registryBuilder.build();

        PoolingHttpClientConnectionManager connManager =
                new PoolingHttpClientConnectionManager(registry);
        connManager.setDefaultConnectionConfig(config);

        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient client =
                builder.setDefaultRequestConfig(requestConfig)
                        .setSSLSocketFactory(sslSF)
                        .setSSLHostnameVerifier(hv)
                        .setConnectionManager(connManager)
                        .build();

        HttpGet get = new HttpGet("https://cdp-dt-01:8090/ws/v1/cluster/info");
        get.setConfig(requestConfig);

        CloseableHttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        System.out.println(response);
    }
}
