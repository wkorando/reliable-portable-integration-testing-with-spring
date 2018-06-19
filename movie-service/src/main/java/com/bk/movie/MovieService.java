package com.bk.movie;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MovieService {

	private MovieRepo repo;

	public Iterable<Movie> findAllMovies() {
		return repo.findAll();
	}

	public Movie addMovie(Movie movie) {
		Errors errors = new Errors();
		if (movie.getId() <= 0) {
			errors.add("Id required");
		}
		if (movie.getReleaseYear() <= 0) {
			errors.add("Release year required");
		}
		if (movie.getRunTimeMins() <= 0) {
			errors.add("Runtime required");
		}
		if (movie.getTitle() == null || movie.getTitle().isEmpty()) {
			errors.add("Title required");
		}
		if (movie.getGenre() == null || movie.getGenre().isEmpty()) {
			errors.add("Title required");
		}
		if (!errors.getErrorMessages().isEmpty()) {
			throw new MovieClientException(errors);
		}
		return repo.save(movie);
	}

	public Iterable<Movie> findByMovieTitle(String name) {
		return repo.findMoviesByTitle(name);
	}

	public Iterable<Movie> findByGenreAndRunLength(String genre, String runLength) {
		return repo.findMoviesByGenreAndRunTimeMins(genre, Integer.valueOf(runLength));
	}

}
