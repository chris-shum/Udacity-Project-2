package app.com.example.android.project2a;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.com.example.android.project2a.data.main.Result;

/**
 * Created by ShowMe on 6/23/16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    List<Result> movieInfoObjectArrayList;

    public RecyclerViewAdapter(List<Result> movieInfoObjectArrayList) {
        this.movieInfoObjectArrayList = movieInfoObjectArrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        RecyclerViewHolder rcv = new RecyclerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Context context = holder.moviePoster.getContext();
        String posterPath = movieInfoObjectArrayList.get(position).getPosterPath();
        String fullPosterPath = context.getString(R.string.poster_url) + posterPath;
        Picasso.with(context).load(fullPosterPath).into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        if (movieInfoObjectArrayList == null) {
            return 0;
        }
        return movieInfoObjectArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView moviePoster;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("Position", getAdapterPosition());
            context.startActivity(intent);
        }

    }
}
