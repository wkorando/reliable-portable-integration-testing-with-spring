package com.bk.movie.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.cloud.contract.stubrunner.server.EnableStubRunnerServer;
import org.springframework.cloud.contract.verifier.dsl.wiremock.WireMockExtensions;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.tomakehurst.wiremock.extension.Extension;

@Profile("local")
@Configuration
@EnableStubRunnerServer
public class LocalWireMockServerConfig {

	public static class TestWireMockExtensions implements WireMockExtensions {
		@Override
		public List<Extension> extensions() {
			return Arrays.asList(new CORSExtension());
		}
	}
}
