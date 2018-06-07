package com.bk.movie.client;

import org.springframework.cloud.contract.stubrunner.server.EnableStubRunnerServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!dev,!prod")
@Configuration
@EnableStubRunnerServer
public class LocalWireMockServerConfig {
}

