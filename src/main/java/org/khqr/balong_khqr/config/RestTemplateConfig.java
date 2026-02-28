package org.khqr.balong_khqr.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import javax.net.ssl.SSLContext;

@Configuration
public class RestTemplateConfig {

    @Value("${bakong.api.token}")
    private String token;

    @Value("${bakong.api.insecure:false}")
    private boolean insecure;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Add default headers like Authorization
        ClientHttpRequestInterceptor authInterceptor = (request, body, execution) -> {
            request.getHeaders().setBearerAuth(token);
            request.getHeaders().set("Content-Type", "application/json");
            return execution.execute(request, body);
        };

        if (insecure) {
            try {
                SSLContext sslContext = SSLContextBuilder.create()
                        .loadTrustMaterial(new TrustAllStrategy())
                        .build();

                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();

                HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                requestFactory.setHttpClient(httpClient);

                RestTemplate rt = new RestTemplate(requestFactory);
                rt.setInterceptors(java.util.Collections.singletonList(authInterceptor));
                rt.setRequestFactory(requestFactory);
                return rt;
            } catch (Exception ex) {
                // if SSL setup fails, fall back to default builder
                ex.printStackTrace();
            }
        }

        return builder
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .additionalInterceptors(authInterceptor)
                .build();
    }
}
