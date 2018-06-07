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

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringJUnitWebConfig(MovieServiceApplication.class)
@WebMvcTest(controllers = MovieController.class, secure = false)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@AutoConfigureMockMvc
public class ITMovieController {

	@Autowired
	private MockMvc mockMvc;
	private ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private MovieService service;

	private final Movie PREDATOR = new Movie(1, "Predator", "Action", 1986, 127);
	private final Movie JOHN_WICK = new Movie(2, "John Wick", "Action", 2014, 104);
	private final Movie TROPIC_THUNDER = new Movie(3, "Tropic Thunder", "Comedy", 2009, 97);

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
								fieldWithPath("[].runTimeMins").description("The movie's runtime in minutes"))));
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
								fieldWithPath("runTimeMins").description("The movie's runtime in minutes"))));
	}

	@Test
	public void callSearchByTitleEndpoint() throws Exception {
		// Setup
		when(service.findByMovieTitle("John Wick")).thenReturn(Arrays.asList(JOHN_WICK));

		// When
		mockMvc.perform(get("/movies?title=John Wick"))

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
