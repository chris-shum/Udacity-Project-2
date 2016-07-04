package app.com.example.android.project2a;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    ImageView moviePoster;
    TextView title, plot, rating, release;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        int position = intent.getIntExtra("Position", 9999);

        Singleton singleton = Singleton.getInstance();

        moviePoster = (ImageView) findViewById(R.id.detailsPoster);
        title = (TextView) findViewById(R.id.detailsTitle);
        plot = (TextView) findViewById(R.id.detailsPlot);
        rating = (TextView) findViewById(R.id.detailsRating);
        release = (TextView) findViewById(R.id.detailsReleaseDate);

        title.setText(singleton.getMovieInfoObjectsArrayList().get(position).getOriginalTitle());
        plot.setText(singleton.getMovieInfoObjectsArrayList().get(position).getOverview());
        rating.setText("\nRating: "+singleton.getMovieInfoObjectsArrayList().get(position).getVoteAverage()+"/10");
        release.setText("\nRelease Date: "+singleton.getMovieInfoObjectsArrayList().get(position).getReleaseDate());

        String posterPath = singleton.getMovieInfoObjectsArrayList().get(position).getPosterPath();
        String fullPosterPath = getString(R.string.poster_url) + posterPath;
        Picasso.with(this).load(fullPosterPath).into(moviePoster);
        toolbar = (Toolbar) findViewById(R.id.toolbarDetails);
        toolbar.setTitle(title.getText().toString());


    }
}
