package com.bk.movie;

import org.springframework.stereotype.Service;

@Service
public class MovieService {

	private MovieRepo repo;
	
	
	public Iterable<Movie> findAllMovies() {
		return repo.findAll();
	}

	public Movie addMovie(Movie movie) {
		return repo.save(movie);
	}

	public Iterable<Movie> findByMovieTitle(String name) {
		return repo.findMoviesByTitle(name);
	}

	public Iterable<Movie> findByGenreAndRunLength(String genre, String runLength) {
		return repo.findMoviesByGenreAndRunTimeMins(genre, Integer.valueOf(runLength));
	}

}
