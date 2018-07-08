package com.bk.movie;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@ContextConfiguration(classes = { MovieServiceApplication.class }, initializers = ITMovieRepo.Initializer.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ITMovieRepo {

	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			TestPropertyValues.of("spring.datasource.url=" + postgres.getJdbcUrl(), //
					"spring.datasource.username=" + postgres.getUsername(), //
					"spring.datasource.password=" + postgres.getPassword(),
					"spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect") //
					.applyTo(applicationContext);
		}
	}

	private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("reliable-movies-db:latest");
	
	@RegisterExtension
	static SpringTestContainersExtension extension = new SpringTestContainersExtension(postgres, true);
	@Autowired
	private MovieRepo repo;
	@MockBean
	private MovieService service;
	@MockBean
	private MovieController controller;

	@Test
	public void testRetrieveCustomerFromDatabase() {
		Movie movie = repo.findById(1L).get();

		assertAll(() -> assertEquals("Action", movie.getGenre()), //
				() -> assertEquals("John Wick", movie.getTitle()), //
				() -> assertEquals(2014, movie.getReleaseYear()), //
				() -> assertEquals(112, movie.getRunTimeMins()));
	}
	
	@Test
	public void testAddCustomerToDatabase() {

		Movie movie = new Movie(2L, "Predator", "Action", 1986, 127);
		
		Movie savedMovie = repo.save(movie);

		assertAll(() -> assertEquals("Action", savedMovie.getGenre()), //
				() -> assertEquals("Predator", savedMovie.getTitle()), //
				() -> assertEquals(1986, savedMovie.getReleaseYear()), //
				() -> assertEquals(127, savedMovie.getRunTimeMins()));
	}
}
