package com.wtz.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.ssl.KeyStoresFactory;
import org.apache.hadoop.security.ssl.SSLFactory;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.KerberosCredentials;
import org.apache.http.client.CredentialsProvider;
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
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class _01_SSLFactory_2 {
    private static final Logger LOG = LoggerFactory.getLogger(_01_SSLFactory_2.class);

    private static final int CONNECTION_TIMEOUT_MILLI_SECOND = 3000;

    private static final String SP = "/";

    private static final String KEY_SFTP_CONF = "sftpConf";

    private static final String KEY_PATH = "path";

    private static final String KEY_USE_LOCAL_FILE = "useLocalFile";

    private static final String KEY_PKCS12 = "pkcs12";

    private static final String KEY_CA = "ca";

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        CloseableHttpClient client = buildHttpsClient();

        RequestConfig requestConfig =
                RequestConfig.custom()
                        .setSocketTimeout(CONNECTION_TIMEOUT_MILLI_SECOND)
                        .setConnectTimeout(CONNECTION_TIMEOUT_MILLI_SECOND)
                        .setCircularRedirectsAllowed(true)
                        .setRelativeRedirectsAllowed(true)
                        .build();

        HttpGet get = new HttpGet("https://lt-cdp-node3:8443/gateway/cdp-proxy/yarn/cluster/apps/RUNNING");
        get.setConfig(requestConfig);

        CloseableHttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        System.out.println(response);
        System.out.println("------------------------------------------------------------");
        System.out.println(EntityUtils.toString(entity));
    }

    private static CloseableHttpClient buildHttpsClient()
            throws IOException, GeneralSecurityException {
        Configuration hadoopConfiguration = new Configuration();
        SSLFactory sslFactory = new SSLFactory(SSLFactory.Mode.CLIENT, hadoopConfiguration);

        sslFactory.init();
        final KeyStoresFactory kf = sslFactory.getKeystoresFactory();
        final HostnameVerifier hv = sslFactory.getHostnameVerifier();

        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        ConnectionSocketFactory plainSf = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSf);
        SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial((chain, authType) -> true).build();
            sslContext.init(kf.getKeyManagers(), kf.getTrustManagers(), null);

            LayeredConnectionSocketFactory sslSf =
                    new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            registryBuilder.register("https", sslSf);
        } catch (KeyStoreException | KeyManagementException | NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new KerberosCredentials(null));
        ConnectionConfig config = ConnectionConfig.custom().build();
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        PoolingHttpClientConnectionManager connManager =
                new PoolingHttpClientConnectionManager(registry);
        connManager.setDefaultConnectionConfig(config);
        return HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(hv)
                .setDefaultCredentialsProvider(credsProvider)
                .build();
    }
}
