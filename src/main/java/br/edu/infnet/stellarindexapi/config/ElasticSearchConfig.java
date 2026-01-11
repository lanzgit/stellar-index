package br.edu.infnet.stellarindexapi.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

  @Value("${spring.elasticsearch.rest.uris:http://localhost:9200}")
  private String elasticUri;

  @Bean
  public ElasticsearchClient elasticsearchClient() {
    String host = elasticUri.replace("http://", "").replace("https://", "").split(":")[0];
    int port = Integer.parseInt(elasticUri.split(":")[2]);
    RestClient restClient = RestClient.builder(new HttpHost(host, port, "http")).build();

    JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper();
    RestClientTransport restClientTransport =
        new RestClientTransport(restClient, jacksonJsonpMapper);

    return new ElasticsearchClient(restClientTransport);
  }
}
