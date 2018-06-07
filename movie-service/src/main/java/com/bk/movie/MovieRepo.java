package com.bk.movie;

import org.springframework.data.repository.CrudRepository;

public interface MovieRepo extends CrudRepository<Movie, Long> {

	Iterable<Movie> findMoviesByTitle(String title);

	Iterable<Movie> findMoviesByGenreAndRunTimeMins(String genre, int runLength);

}
