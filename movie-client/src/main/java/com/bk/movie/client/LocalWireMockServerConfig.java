package com.bk.movie.client;

import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.head;
import static com.github.tomakehurst.wiremock.client.WireMock.options;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.trace;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import javax.annotation.PostConstruct;

import org.springframework.cloud.contract.stubrunner.server.EnableStubRunnerServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

@Profile("local")
@Configuration
@EnableStubRunnerServer
public class LocalWireMockServerConfig {

	@PostConstruct
	public void allowCors() throws InterruptedException {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				WireMock.configureFor("localhost", 8081);

				for (StubMapping stub : WireMock.listAllStubMappings().getMappings()) {
					if (stub.getResponse() != null) {
						HttpHeaders httpHeaders = stub.getResponse().getHeaders()
								.plus(new HttpHeader("Access-Control-Allow-Origin", "*"));
						ResponseDefinition originalResponse = stub.getResponse();
						ResponseDefinition responseDefinition = new ResponseDefinition(//
								originalResponse.getStatus(), //
								originalResponse.getStatusMessage(), //
								originalResponse.getBody(), //
								null, //
								null, //
								null, //
								httpHeaders, //
								originalResponse.getAdditionalProxyRequestHeaders(), //
								originalResponse.getFixedDelayMilliseconds(), //
								originalResponse.getDelayDistribution(), //
								originalResponse.getChunkedDribbleDelay(), //
								originalResponse.getProxyBaseUrl(), //
								originalResponse.getFault(), //
								originalResponse.getTransformers(), //
								originalResponse.getTransformerParameters(), //
								originalResponse.isFromConfiguredStub());

						switch (stub.getRequest().getMethod().getName()) {
						case "GET":
							WireMock.stubFor(get(urlMatching(stub.getRequest().getUrl()))
									.willReturn(ResponseDefinitionBuilder.like(responseDefinition)));
						case "POST":
							WireMock.stubFor(post(urlMatching(stub.getRequest().getUrl()))
									.willReturn(ResponseDefinitionBuilder.like(responseDefinition)));
						case "PUT":
							WireMock.stubFor(put(urlMatching(stub.getRequest().getUrl()))
									.willReturn(ResponseDefinitionBuilder.like(responseDefinition)));
						case "DELETE":
							WireMock.stubFor(delete(urlMatching(stub.getRequest().getUrl()))
									.willReturn(ResponseDefinitionBuilder.like(responseDefinition)));
						case "PATCH":
							WireMock.stubFor(patch(urlMatching(stub.getRequest().getUrl()))
									.willReturn(ResponseDefinitionBuilder.like(responseDefinition)));
						case "OPTIONS":
							WireMock.stubFor(options(urlMatching(stub.getRequest().getUrl()))
									.willReturn(ResponseDefinitionBuilder.like(responseDefinition)));
						case "HEAD":
							WireMock.stubFor(head(urlMatching(stub.getRequest().getUrl()))
									.willReturn(ResponseDefinitionBuilder.like(responseDefinition)));
						case "TRACE":
							WireMock.stubFor(trace(urlMatching(stub.getRequest().getUrl()))
									.willReturn(ResponseDefinitionBuilder.like(responseDefinition)));
						}
					}
				}
			}
		};

		Thread thread = new Thread(runnable);
		thread.start();
	}
}
