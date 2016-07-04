package app.com.example.android.project2a;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import app.com.example.android.project2a.data.APICall.MovieAPI;
import app.com.example.android.project2a.data.reviews.Result;
import app.com.example.android.project2a.data.reviews.Reviews;
import app.com.example.android.project2a.data.trailers.Trailers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayAdapter;
    String id;
    String type;
    ArrayList<String> testArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        testArray = new ArrayList<>();

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        type = intent.getStringExtra("Type");

        if (type.equals("Trailer")) {
            getTrailers(id);
        }
        if (type.equals("Review")) {
            getReviews(id);

        }

        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, testArray);
        listView.setAdapter(arrayAdapter);


    }

    public void getTrailers(String inputID) {
        MovieAPI.Factory.getInstance().getTrailers(inputID, getString(R.string.API_KEY)).enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Call<Trailers> call, Response<Trailers> response) {
                List<app.com.example.android.project2a.data.trailers.Result> something = response.body().getResults();
                if (something.size() > 0) {
                    for (app.com.example.android.project2a.data.trailers.Result boop : something) {
                        testArray.add("https://www.youtube.com/watch?v=" + boop.getKey());
                        Log.d("trailer", boop.getKey());
                    }
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String) listView.getItemAtPosition(position))));

                        }
                    });

                } else {
                    testArray.add("No trailers for this movies.");
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Trailers> call, Throwable t) {

            }
        });
    }


    public void getReviews(String inputID) {
        MovieAPI.Factory.getInstance().getReviews(inputID, getString(R.string.API_KEY)).enqueue(new Callback<Reviews>() {

            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                List<Result> something = response.body().getResults();
                if (something.size() > 0) {
                    for (Result boop : something) {
                        testArray.add("Author: " + boop.getAuthor() + "\n\n" + boop.getContent());
                    }
                } else {
                    testArray.add("No reviews for this movies.");
                }
                arrayAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {

            }
        });
    }
}
