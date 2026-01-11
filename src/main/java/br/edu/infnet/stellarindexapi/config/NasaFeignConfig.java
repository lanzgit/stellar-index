package br.edu.infnet.stellarindexapi.config;

import feign.Logger;
import feign.Request;
import feign.codec.ErrorDecoder;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NasaFeignConfig {

  @Bean
  public Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }

  @Bean
  public Request.Options requestOptions() {
    return new Request.Options(10, TimeUnit.SECONDS, 30, TimeUnit.SECONDS, true);
  }

  @Bean
  public ErrorDecoder errorDecoder() {
    return new NasaErrorDecoder();
  }

  public static class NasaErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, feign.Response response) {
      if (response.status() == 404) {
        return new RuntimeException("Corpo celeste não encontrado na base da NASA");
      }
      if (response.status() >= 400 && response.status() <= 499) {
        return new RuntimeException("Erro na requisição para a API da NASA: " + response.reason());
      }
      if (response.status() >= 500) {
        return new RuntimeException("Erro interno da API da NASA: " + response.reason());
      }
      return defaultErrorDecoder.decode(methodKey, response);
    }
  }
}
