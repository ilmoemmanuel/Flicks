package ht.eilmot.flicks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ht.eilmot.flicks.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    // list of movies
    ArrayList<Movie> movies;
    // initialize with list

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }
    // creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the context and create the inflater
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        // return a new viewHolder

        return new ViewHolder(movieView);
    }
    // binds and inflated view to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the movie data at the specified position
        Movie movie = movies.get(position);
        // populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // TOOO - set image using Glide


    }
    // return the total number of item in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create the view holder as a static iner class
    public static class ViewHolder extends RecyclerView.ViewHolder{

        // track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;


        public ViewHolder(View itemView) {
            super(itemView);
            //loockup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
