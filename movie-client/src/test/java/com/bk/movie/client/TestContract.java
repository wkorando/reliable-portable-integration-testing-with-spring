package com.bk.movie.client;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestTemplate;

@SpringJUnitConfig(MovieClientServiceApplication.class)
@AutoConfigureStubRunner(ids = "com.bk.movie:movie-service:+:8081", workOffline=true)
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
