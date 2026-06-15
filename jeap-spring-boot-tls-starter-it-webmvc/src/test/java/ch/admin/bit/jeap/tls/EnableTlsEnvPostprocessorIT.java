package ch.admin.bit.jeap.tls;

import lombok.SneakyThrows;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.HostnameVerificationPolicy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApplication.class)
class EnableTlsEnvPostprocessorIT {

    @LocalServerPort
    private int randomServerPort;

    @Test
    void testHttpFails() {
        RestClient.ResponseSpec responseSpec = getRestClient("http").get().retrieve();
        assertThatThrownBy(() -> responseSpec.body(String.class))
                .isInstanceOf(RestClientException.class)
                // TLS needed
                .hasMessageContaining("This combination of host and port requires TLS");
    }

    @Test
    void testHttpsSucceeds() {
        String response = getRestClient("https").get().retrieve().body(String.class);
        assertThat(response).isEqualTo("Hello World!");
    }


    private RestClient getRestClient(String scheme) {
        String baseUrl = UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host("localhost")
                .path("/hello")
                .port(randomServerPort)
                .build()
                .toUriString();
        return RestClient.builder()
                .requestFactory(createRequestFactoryAcceptingSelfSignedCertificates())
                .baseUrl(baseUrl)
                .build();
    }

    @SneakyThrows
    private ClientHttpRequestFactory createRequestFactoryAcceptingSelfSignedCertificates() {
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, (cert, authType) -> true)
                .build();
        TlsSocketStrategy tlsStrategy = ClientTlsStrategyBuilder.create()
                .setSslContext(sslContext)
                .setHostVerificationPolicy(HostnameVerificationPolicy.CLIENT)
                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .buildClassic();
        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setTlsSocketStrategy(tlsStrategy)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

}
