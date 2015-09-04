package org.coursera.metrics.datadog.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.util.Duration;
import org.apache.http.HttpHost;
import org.eclipse.jetty.util.StringUtil;

import javax.validation.constraints.NotNull;

@JsonTypeName("http")
public class HttpTransportFactory implements AbstractTransportFactory {

  @NotNull
  @JsonProperty
  private String apiKey = null;

  @JsonProperty
  private Duration connectTimeout = Duration.seconds(5);

  @JsonProperty
  private Duration socketTimeout = Duration.seconds(5);

  @JsonProperty
  private boolean useSystemProxy = false;

  @JsonProperty
  private String proxyHost = null;

  @JsonProperty
  private String proxyPort = null;

  public HttpTransport build() {
    return new HttpTransport.Builder()
        .withApiKey(apiKey)
        .withConnectTimeout((int) connectTimeout.toMilliseconds())
        .withSocketTimeout((int) socketTimeout.toMilliseconds())
        .withProxy(proxyHttpHost())
        .build();
  }

  private HttpHost proxyHttpHost() {
    if (useSystemProxy) {
      return httpHost(System.getProperty("http.proxyHost"), System.getProperty("http.proxyPort"));
    } else {
      return httpHost(this.proxyHost, this.proxyPort);
    }
  }

  private HttpHost httpHost(String host, String port) {
    if (StringUtil.isNotBlank(host)) {
      if (StringUtil.isNotBlank(port)) {
        return new HttpHost(host, Integer.valueOf(port));
      }
      return new HttpHost(host);
    }
    return null;
  }
}
