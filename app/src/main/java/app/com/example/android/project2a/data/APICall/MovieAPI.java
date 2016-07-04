package app.com.example.android.project2a.data.APICall;

import app.com.example.android.project2a.data.main.Movie;
import app.com.example.android.project2a.data.reviews.Reviews;
import app.com.example.android.project2a.data.trailers.Trailers;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ShowMe on 7/3/16.
 */
public interface MovieAPI {

    String BASE_URL = "https://api.themoviedb.org/3/movie/";

    @GET("{type}")Call<Movie> getMovie(@Path("type") String type, @Query("api_key") String api);
    @GET("{id}/videos")Call<Trailers> getTrailers(@Path("id") String id, @Query("api_key") String api);
    @GET("{id}/reviews")Call<Reviews> getReviews(@Path("id") String id, @Query("api_key") String api);

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
