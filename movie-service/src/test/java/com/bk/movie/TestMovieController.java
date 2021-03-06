package com.bk.movie;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.restdocs.WireMockRestDocs;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringJUnitWebConfig(MovieServiceApplication.class)
@WebMvcTest(controllers = MovieController.class, secure = false)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@AutoConfigureMockMvc
public class TestMovieController {

	@Autowired
	private MockMvc mockMvc;
	private ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private MovieService service;

	private final Movie PREDATOR = new Movie(1, "Predator", "Action", 1986, 127);
	private final Movie JOHN_WICK = new Movie(2, "John Wick", "Action", 2014, 104);
	private final Movie TROPIC_THUNDER = new Movie(3, "Tropic Thunder", "Comedy", 2009, 97);
	private final Movie MOVIE_MISSING_FIELDS = new Movie(0, "", "", 0, 0);

	@Test
	public void callFindAllEndpoint() throws Exception {
		// Setup
		when(service.findAllMovies()).thenReturn(Arrays.asList(PREDATOR, JOHN_WICK, TROPIC_THUNDER));

		// When
		mockMvc.perform(get("/movies"))

				// Then
				.andDo(document("find-all-movies",
						responseFields(fieldWithPath("[].id").description("The movie's id"),
								fieldWithPath("[].title").description("The movie's title"),
								fieldWithPath("[].genre").description("The genre of the movie"),
								fieldWithPath("[].releaseYear").description("The year the movie was released"),
								fieldWithPath("[].runTimeMins").description("The movie's runtime in minutes"))))
				.andExpect(jsonPath("$[1].title").value(MatchesPattern.matchesPattern(Pattern.compile(".{2,40}")))); //

	}

	@Test
	public void callAddMovieEndpoint() throws Exception {
		// Setup
		when(service.addMovie(PREDATOR)).thenReturn(PREDATOR);

		// When
		mockMvc.perform(post("/movies").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsString(PREDATOR)))

				// Then
				.andDo(document("add-movie",
						requestFields(fieldWithPath("id").description("The movie's id"),
								fieldWithPath("title").description("The movie's title"),
								fieldWithPath("genre").description("The genre of the movie"),
								fieldWithPath("releaseYear").description("The year the movie was released"),
								fieldWithPath("runTimeMins").description("The movie's runtime in minutes")),
						responseFields(fieldWithPath("id").description("The movie's id"),
								fieldWithPath("title").description("The movie's title"),
								fieldWithPath("genre").description("The genre of the movie"),
								fieldWithPath("releaseYear").description("The year the movie was released"),
								fieldWithPath("runTimeMins").description("The movie's runtime in minutes"))))
				.andDo(WireMockRestDocs.verify().jsonPath("$[?(@.id =~ /[0-9]+/)]")
						.jsonPath("$[?(@.title =~ /.{1,40}/)]").jsonPath("$[?(@.genre =~ /.{1,25}/)]")
						.jsonPath("$[?(@.releaseYear =~ /[1-2]{1}[0,9]{1}[0-9]{2}/)]")
						.jsonPath("$[?(@.runTimeMins =~ /[1-9]{1}[0-9]{1,2}/)]")
						.contentType(MediaType.valueOf("application/json")).stub("add-movie"));
	}

	@Test
	public void callAddMovieEndpointError() throws Exception {
		// Setup
		when(service.addMovie(MOVIE_MISSING_FIELDS)).thenCallRealMethod();

		// When
		mockMvc.perform(post("/movies").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsString(MOVIE_MISSING_FIELDS)))

				// Then
				.andDo(document("add-movie-error",
						requestFields(fieldWithPath("id").description("The movie's id"),
								fieldWithPath("title").description("The movie's title"),
								fieldWithPath("genre").description("The genre of the movie"),
								fieldWithPath("releaseYear").description("The year the movie was released"),
								fieldWithPath("runTimeMins").description("The movie's runtime in minutes")),
						responseFields(fieldWithPath("errorMessages")
								.description("Array of messages describing from with request."))));
	}

	@Test
	public void callSearchByTitleEndpoint() throws Exception {
		// Setup
		when(service.findByMovieTitle("John Wick")).thenReturn(Arrays.asList(JOHN_WICK));

		// When
		mockMvc.perform(get("/movies").param("title", "John Wick"))

				// Then
				.andDo(document("find-movies-by-title",
						requestParameters(parameterWithName("title").description("The title of the movie to look up")),
						responseFields(fieldWithPath("[].id").description("The movie's id"),
								fieldWithPath("[].title").description("The movie's title"),
								fieldWithPath("[].genre").description("The genre of the movie"),
								fieldWithPath("[].releaseYear").description("The year the movie was released"),
								fieldWithPath("[].runTimeMins").description("The movie's runtime in minutes"))));
	}

	@Test
	public void callGenreAndRunLengthEndpoint() throws Exception {
		// Setup
		when(service.findByGenreAndRunLength("Action", "150")).thenReturn(Arrays.asList(PREDATOR, JOHN_WICK));

		// When
		mockMvc.perform(get("/movies/search-by/genreAndRunLength?genre=Action&runLength=150"))

				// Then
				.andDo(document("find-movies-by-genre-run-length",
						requestParameters(parameterWithName("genre").description("The genre of the movie to look up"),
								parameterWithName("runLength")
										.description("The maximum length of a movie in minutes to loopkup")),
						responseFields(fieldWithPath("[].id").description("The movie's id"),
								fieldWithPath("[].title").description("The movie's title"),
								fieldWithPath("[].genre").description("The genre of the movie"),
								fieldWithPath("[].releaseYear").description("The year the movie was released"),
								fieldWithPath("[].runTimeMins").description("The movie's runtime in minutes"))));
	}

}