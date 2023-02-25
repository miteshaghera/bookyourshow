package com.xyz.bp.movie;

import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface MovieService {
    Single<Movie> createMovie(Movie movie);

    Single<List<Movie>> listMovies(String city, String searchStr);

}
