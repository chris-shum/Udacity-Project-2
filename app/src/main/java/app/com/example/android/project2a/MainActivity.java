package app.com.example.android.project2a;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import app.com.example.android.project2a.data.model.Movie;
import app.com.example.android.project2a.data.model.Result;
import app.com.example.android.project2a.data.remote.MovieAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String choice;
    Singleton mSingleton;
    GridLayoutManager gridLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    Toolbar toolbar;

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
                moviePopularAPICall();
                break;
            case R.id.action_best_rated:
                choice = getString(R.string.sort_by_query2);
                toolbar.setTitle(R.string.action_best_rated);
                movieTopAPICall();
                break;
            case R.id.action_favorites:
                Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void moviePopularAPICall() {
        MovieAPI.Factory.getInstance().getMoviePopular().enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                List<Result> something = response.body().getResults();
                mSingleton.movieInfoObjectsArrayList.clear();
                mSingleton.movieInfoObjectsArrayList.addAll(something);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void movieTopAPICall() {
        MovieAPI.Factory.getInstance().getMovieTopRated().enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                List<Result> something = response.body().getResults();
                mSingleton.movieInfoObjectsArrayList.clear();
                mSingleton.movieInfoObjectsArrayList.addAll(something);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


