package app.com.example.android.project2a;

import java.util.ArrayList;
import java.util.List;

import app.com.example.android.project2a.data.main.Result;

/**
 * Created by ShowMe on 6/23/16.
 */
public class Singleton {
    List<Result> movieInfoObjectsArrayList;

    public Singleton() {
        if (movieInfoObjectsArrayList == null) {
            movieInfoObjectsArrayList = new ArrayList<>();
        }
    }

    public static Singleton singleton;

    public static Singleton getInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }

    public List<Result> getMovieInfoObjectsArrayList() {
        return movieInfoObjectsArrayList;
    }


}
