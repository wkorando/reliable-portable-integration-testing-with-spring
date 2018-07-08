package com.bk.movie.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestTemplate;

@SpringJUnitConfig(MovieClientServiceApplication.class)
@AutoConfigureStubRunner
public class ITConsumeMovieService {

	private RestTemplate restTemplate = new RestTemplate();

	@Test
	public void testCallFindAllMovies() {
		Movie[] movies = restTemplate.getForObject("http://localhost:8081/movies", Movie[].class);

		assertThat(movies.length).isEqualTo(3);
		assertThat(movies).extracting("id", "title", "genre", "releaseYear", "runTimeMins").contains(
				tuple(1, "Predator", "Action", 1986, 127), //
				tuple(2, "John Wick", "Action", 2014, 104), //
				tuple(3, "Tropic Thunder", "Comedy", 2009, 97));

	}

	@Test
	public void testAddMovie() {
		Movie newMovie = new Movie(1, "Predator", "Action", 1986, 127);
		ResponseEntity<Movie> movie = restTemplate.postForEntity("http://localhost:8081/movies", newMovie, Movie.class);

		assertThat(movie.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(movie.getBody()).isEqualTo(newMovie);

	}	
}