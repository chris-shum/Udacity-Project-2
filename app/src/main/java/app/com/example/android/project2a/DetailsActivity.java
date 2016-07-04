package app.com.example.android.project2a;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import app.com.example.android.project2a.SQLite.MovieSQLiteOpenHelper;
import app.com.example.android.project2a.data.main.Result;

public class DetailsActivity extends AppCompatActivity {
    ImageView moviePoster;
    TextView title, plot, rating, release;
    Toolbar toolbar;
    Button trailersButton, reviewsButton, favoritesButton;
    MovieSQLiteOpenHelper movieSQLiteOpenHelper;
    Result temp;
    Cursor cursor;
    Toast toastObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        final int position = intent.getIntExtra("Position", 9999);

        final Singleton singleton = Singleton.getInstance();

        moviePoster = (ImageView) findViewById(R.id.detailsPoster);
        title = (TextView) findViewById(R.id.detailsTitle);
        plot = (TextView) findViewById(R.id.detailsPlot);
        rating = (TextView) findViewById(R.id.detailsRating);
        release = (TextView) findViewById(R.id.detailsReleaseDate);
        trailersButton = (Button) findViewById(R.id.trailersButton);
        reviewsButton = (Button) findViewById(R.id.reviewsButton);
        favoritesButton = (Button) findViewById(R.id.favoritesButton);
        toastObject = new Toast(DetailsActivity.this);

        title.setText(singleton.getMovieInfoObjectsArrayList().get(position).getOriginalTitle());
        plot.setText(singleton.getMovieInfoObjectsArrayList().get(position).getOverview());
        rating.setText("\nRating: " + singleton.getMovieInfoObjectsArrayList().get(position).getVoteAverage() + "/10");
        release.setText("\nRelease Date: " + singleton.getMovieInfoObjectsArrayList().get(position).getReleaseDate());

        String posterPath = singleton.getMovieInfoObjectsArrayList().get(position).getPosterPath();
        String fullPosterPath = getString(R.string.poster_url) + posterPath;
        Picasso.with(this).load(fullPosterPath).into(moviePoster);
        toolbar = (Toolbar) findViewById(R.id.toolbarDetails);
        toolbar.setTitle(title.getText().toString());

        temp = singleton.getMovieInfoObjectsArrayList().get(position);
        movieSQLiteOpenHelper = new MovieSQLiteOpenHelper(DetailsActivity.this);
        cursor = movieSQLiteOpenHelper.searchMovieList(String.valueOf(temp.getId()));


        trailersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, ListActivity.class);
                intent.putExtra("ID", String.valueOf(singleton.getMovieInfoObjectsArrayList().get(position).getId()));
                intent.putExtra("Type", "Trailer");
                startActivity(intent);
            }
        });

        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, ListActivity.class);
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
                    toastObject.cancel();
                    toastObject.makeText(DetailsActivity.this, "Movie added to favorites.", Toast.LENGTH_SHORT).show();
                    favoritesButton.setText(getString(R.string.remove_from_favorites));
                } else {
                    movieSQLiteOpenHelper.removeMovie(String.valueOf(temp.getId()));
                    toastObject.cancel();
                    toastObject.makeText(DetailsActivity.this, "Movie removed from favorites.", Toast.LENGTH_SHORT).show();
                    favoritesButton.setText(getString(R.string.add_to_favorites));
                    Log.d("test", ""+temp.getId());


                    for (int i = 0; i < singleton.getMovieInfoObjectsArrayList().size(); i++) {
                        Log.d("test", ""+singleton.getMovieInfoObjectsArrayList().get(i).getId());
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

    @Override
    protected void onPause() {
        super.onPause();
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor = movieSQLiteOpenHelper.searchMovieList(String.valueOf(temp.getId()));
    }
}

