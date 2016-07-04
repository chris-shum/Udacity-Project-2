package app.com.example.android.project2a;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    ImageView moviePoster;
    TextView title, plot, rating, release;
    Toolbar toolbar;
    Button trailersButton, reviewsButton;

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

        title.setText(singleton.getMovieInfoObjectsArrayList().get(position).getOriginalTitle());
        plot.setText(singleton.getMovieInfoObjectsArrayList().get(position).getOverview());
        rating.setText("\nRating: "+singleton.getMovieInfoObjectsArrayList().get(position).getVoteAverage()+"/10");
        release.setText("\nRelease Date: "+singleton.getMovieInfoObjectsArrayList().get(position).getReleaseDate());

        String posterPath = singleton.getMovieInfoObjectsArrayList().get(position).getPosterPath();
        String fullPosterPath = getString(R.string.poster_url) + posterPath;
        Picasso.with(this).load(fullPosterPath).into(moviePoster);
        toolbar = (Toolbar) findViewById(R.id.toolbarDetails);
        toolbar.setTitle(title.getText().toString());

        trailersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, ListActivity.class);
                intent.putExtra("ID",String.valueOf(singleton.getMovieInfoObjectsArrayList().get(position).getId()));
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

    }
}
