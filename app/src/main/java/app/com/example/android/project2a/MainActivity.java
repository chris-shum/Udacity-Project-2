package app.com.example.android.project2a;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import app.com.example.android.project2a.SQLite.MovieSQLiteOpenHelper;
import app.com.example.android.project2a.data.APICall.MovieAPI;
import app.com.example.android.project2a.data.main.Movie;
import app.com.example.android.project2a.data.main.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String choice;
    Singleton mSingleton;
    GridLayoutManager gridLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    Toolbar toolbar;
    MovieSQLiteOpenHelper databaseHelper;
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: 7/2/16 internet check 
        choice = getString(R.string.sort_by_query1);
        mSingleton = Singleton.getInstance();
        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.mainActivityRecyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(mSingleton.getMovieInfoObjectsArrayList());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (findViewById(R.id.detailsFragment) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mSingleton.setmTwoPanes(mTwoPane);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_popular:
                choice = getString(R.string.sort_by_query1);
                toolbar.setTitle(R.string.action_popular);
                getMovies(choice);
                break;
            case R.id.action_best_rated:
                choice = getString(R.string.sort_by_query2);
                toolbar.setTitle(R.string.action_best_rated);
                getMovies(choice);
                break;
            case R.id.action_favorites:
                toolbar.setTitle("Favorites");
                getFavorites();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getMovies(String choice) {
        MovieAPI.Factory.getInstance().getMovie(choice, getString(R.string.API_KEY)).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                List<Result> something = response.body().getResults();
                mSingleton.movieInfoObjectsArrayList.clear();
                mSingleton.movieInfoObjectsArrayList.addAll(something);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    public void getFavorites() {
        mSingleton.movieInfoObjectsArrayList.clear();
        databaseHelper = new MovieSQLiteOpenHelper(MainActivity.this);
        Cursor cursor = databaseHelper.getMovieList();
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String id = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_ID));
                String movieName = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_MOVIE_NAME));
                String moviePoster = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_MOVIE_POSTER));
                String plot = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_PLOT));
                String rating = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_RATING));
                String release = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_RELEASE));
                mSingleton.getMovieInfoObjectsArrayList().add(new Result(id, movieName, moviePoster, plot, rating, release));
                cursor.moveToNext();
            }
            cursor.close();
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewAdapter.notifyDataSetChanged();
    }
}


