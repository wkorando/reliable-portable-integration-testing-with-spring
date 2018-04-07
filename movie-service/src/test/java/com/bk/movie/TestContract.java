package com.bk.movie;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestTemplate;

@SpringJUnitConfig
@SpringBootTest
@AutoConfigureStubRunner(ids = "com.bk.movie:movie-service:*:8081", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class TestContract {

	private RestTemplate restTemplate = new RestTemplate();

	@Test
	public void testCallFindAllMovies() {
		Movie[] movies = restTemplate.getForObject("http://localhost:8081/movies", Movie[].class);

		for (Movie movie : movies) {
			System.out.println(movie.toString());
		}
	}

}
