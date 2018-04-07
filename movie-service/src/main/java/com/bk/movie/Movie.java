package com.bk.movie;

import java.io.Serializable;

public class Movie implements Serializable {

	private static final long serialVersionUID = -8553189923124180473L;
	private long id;
	private String title;
	private String genre;
	private int releaseYear;
	private int runTimeMins;

	public Movie() {
	}

	public Movie(long id, String title, String genre, int releaseYear, int runTimeMins) {
		this.id = id;
		this.title = title;
		this.genre = genre;
		this.releaseYear = releaseYear;
		this.runTimeMins = runTimeMins;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getGenre() {
		return genre;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public int getRunTimeMins() {
		return runTimeMins;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + releaseYear;
		result = prime * result + runTimeMins;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		if (id != other.id)
			return false;
		if (releaseYear != other.releaseYear)
			return false;
		if (runTimeMins != other.runTimeMins)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
