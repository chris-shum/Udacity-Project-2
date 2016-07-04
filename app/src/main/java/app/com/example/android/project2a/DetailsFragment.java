package app.com.example.android.project2a;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import app.com.example.android.project2a.SQLite.MovieSQLiteOpenHelper;
import app.com.example.android.project2a.data.main.Result;

/**
 * Created by ShowMe on 7/4/16.
 */
public class DetailsFragment extends Fragment {

    ImageView moviePoster;
    TextView title, plot, rating, release;
    Toolbar toolbar;
    Button trailersButton, reviewsButton, favoritesButton;
    MovieSQLiteOpenHelper movieSQLiteOpenHelper;
    Result temp;
    Cursor cursor;
    Toast toastObject;
    Integer position;
    Singleton singleton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (getArguments() != null) {
            String positionString = bundle.getString("Position");
            position = Integer.valueOf(positionString);
        }
        singleton = Singleton.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        moviePoster = (ImageView) getActivity().findViewById(R.id.detailsPoster);
        title = (TextView) getActivity().findViewById(R.id.detailsTitle);
        plot = (TextView) getActivity().findViewById(R.id.detailsPlot);
        rating = (TextView) getActivity().findViewById(R.id.detailsRating);
        release = (TextView) getActivity().findViewById(R.id.detailsReleaseDate);
        trailersButton = (Button) getActivity().findViewById(R.id.trailersButton);
        reviewsButton = (Button) getActivity().findViewById(R.id.reviewsButton);
        favoritesButton = (Button) getActivity().findViewById(R.id.favoritesButton);
        toastObject = new Toast(getActivity());

        if (position != null) {

            favoritesButton.setVisibility(View.VISIBLE);
            reviewsButton.setVisibility(View.VISIBLE);
            trailersButton.setVisibility(View.VISIBLE);

            title.setText(singleton.getMovieInfoObjectsArrayList().get(position).getOriginalTitle());
            plot.setText(singleton.getMovieInfoObjectsArrayList().get(position).getOverview());
            rating.setText("\nRating: " + singleton.getMovieInfoObjectsArrayList().get(position).getVoteAverage() + "/10");
            release.setText("\nRelease Date: " + singleton.getMovieInfoObjectsArrayList().get(position).getReleaseDate());

            String posterPath = singleton.getMovieInfoObjectsArrayList().get(position).getPosterPath();
            String fullPosterPath = getString(R.string.poster_url) + posterPath;
            Picasso.with(getActivity()).load(fullPosterPath).into(moviePoster);
            toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarDetails);

            temp = singleton.getMovieInfoObjectsArrayList().get(position);
            movieSQLiteOpenHelper = new MovieSQLiteOpenHelper(getActivity());
            cursor = movieSQLiteOpenHelper.searchMovieList(String.valueOf(temp.getId()));


            trailersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("ID", String.valueOf(singleton.getMovieInfoObjectsArrayList().get(position).getId()));
                    intent.putExtra("Type", "Trailer");
                    startActivity(intent);
                }
            });

            reviewsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("ID", String.valueOf(singleton.getMovieInfoObjectsArrayList().get(position).getId()));
                    intent.putExtra("Type", "Review");
                    startActivity(intent);

                }
            });

            favoritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (favoritesButton.getText().equals(getString(R.string.add_to_favorites))) {
                        movieSQLiteOpenHelper.addMovie(String.valueOf(temp.getId()), temp.getTitle(), temp.getPosterPath(), temp.getOverview(), String.valueOf(temp.getVoteAverage()), temp.getReleaseDate());
                        singleton.movieInfoObjectsArrayList.add(new Result(String.valueOf(temp.getId()), temp.getTitle(), temp.getPosterPath(), temp.getOverview(), String.valueOf(temp.getVoteAverage()), temp.getReleaseDate()));
                        toastObject.cancel();
                        toastObject.makeText(getActivity(), "Movie added to favorites.", Toast.LENGTH_SHORT).show();
                        favoritesButton.setText(getString(R.string.remove_from_favorites));
                    } else {
                        movieSQLiteOpenHelper.removeMovie(String.valueOf(temp.getId()));
                        toastObject.cancel();
                        toastObject.makeText(getActivity(), "Movie removed from favorites.", Toast.LENGTH_SHORT).show();
                        favoritesButton.setText(getString(R.string.add_to_favorites));

                        for (int i = 0; i < singleton.getMovieInfoObjectsArrayList().size(); i++) {
                            if (String.valueOf(temp.getId()).equals(
                                    String.valueOf(singleton.getMovieInfoObjectsArrayList().get(i).getId()))) {
                                singleton.movieInfoObjectsArrayList.remove(i);
                            }
                        }
                    }
                }
            });

            if (cursor.getCount() <= 0) {
                favoritesButton.setText(getString(R.string.add_to_favorites));
            } else {
                favoritesButton.setText(getString(R.string.remove_from_favorites));
            }
        }
        return inflater.inflate(R.layout.fragment_details, container, false);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (temp != null) {
            cursor.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (temp != null) {
            cursor = movieSQLiteOpenHelper.searchMovieList(String.valueOf(temp.getId()));
        }

    }
}
