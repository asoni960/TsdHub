package rest;

import model.MainActivityPojoExtra;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by abhinav on 12-10-2016.
 */

public interface ApiInterface {

    @GET("movie/top_rated") Call<MainActivityPojoExtra> getTopRatedMovies(@Query("api_key")String apiKey,@Query("page")int page);
    @GET("movie/upcoming") Call<MainActivityPojoExtra> getUpcomingMovies(@Query("api_key")String apiKey,@Query("page")int page);
    @GET("movie/popular") Call<MainActivityPojoExtra> getPopularMovies(@Query("api_key")String apiKey,@Query("page")int page);
    @GET("tv/popular") Call<MainActivityPojoExtra> getTopTv(@Query("api_key")String apiKey,@Query("page")int page);

    @GET("search/movie") Call<MainActivityPojoExtra> getSearchMovieData(@Query("api_key")String apiKey,@Query("query")String query,@Query("page")int page);

}
