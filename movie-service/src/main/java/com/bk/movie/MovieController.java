package com.bk.movie;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {

	private MovieService service;

	public MovieController(MovieService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<Iterable<Movie>> allMovies() {
		return ResponseEntity.ok(service.findAllMovies());
	}

	@PostMapping
	public ResponseEntity<Movie> addNewMovie(@RequestBody Movie movie) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.addMovie(movie));
	}

	@GetMapping(params = "title")
	public ResponseEntity<Iterable<Movie>> searchMoviesByName(@RequestParam("title") String name) {
		return ResponseEntity.ok(service.findByMovieTitle(name));
	}

	@GetMapping(value = "/search-by/genreAndRunLength", params = { "genre", "runLength" })
	public ResponseEntity<Iterable<Movie>> searchByGenreAndRunlength(@RequestParam("genre") String genre,
			@RequestParam("runLength") String runLength) {
		return ResponseEntity.ok(service.findByGenreAndRunLength(genre, runLength));
	}
}
