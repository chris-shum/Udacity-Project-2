package app.com.example.android.project2a.data.remote;

import app.com.example.android.project2a.data.model.Movie;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by ShowMe on 7/3/16.
 */
public interface MovieAPI {

    String BASE_URL = "https://api.themoviedb.org/3/movie/";

    @GET("popular?api_key=5bcfaa2234e9753384005a12ae3db303")Call<Movie> getMoviePopular();
    @GET("top_rated?api_key=5bcfaa2234e9753384005a12ae3db303")Call<Movie> getMovieTopRated();

    class Factory {

        private static MovieAPI service;

        public static MovieAPI getInstance() {


            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
                service = retrofit.create(MovieAPI.class);
                return service;

            } else {
                return service;
            }

        }
    }


}
