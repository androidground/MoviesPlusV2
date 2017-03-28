package com.eightbytestech.moviesplusv2.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eightbytestech.moviesplusv2.MovieDetailActivity;
import com.eightbytestech.moviesplusv2.MovieDetailFragment;
import com.eightbytestech.moviesplusv2.MovieListActivity;
import com.eightbytestech.moviesplusv2.R;
import com.eightbytestech.moviesplusv2.model.Movie;
import com.eightbytestech.moviesplusv2.utility.ApiUtility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    List<Movie> movieList;
    private LayoutInflater mLayouInflater;
    private Context mContext;

    boolean showingFavorites;

    public MovieAdapter(Context context, boolean showingOffline) {
        mContext = context;
        mLayouInflater = LayoutInflater.from(context);
        showingFavorites = showingOffline;
    }

    public void setMovieList(List<Movie> movies) {
        if ( movies != null ) {
            this.movieList = new ArrayList<>();
            this.movieList.addAll(movies);
            notifyDataSetChanged();
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayouInflater.inflate(R.layout.movie_list_content, parent, false);
        final MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int moviePosition = movieViewHolder.getAdapterPosition();
                if (MovieListActivity.mTwoPane) {

                    //Toast.makeText(mContext, movieList.get(moviePosition).title, Toast.LENGTH_LONG).show();

                    Bundle arguments = new Bundle();
                    arguments.putParcelable(MovieDetailFragment.MOVIE_DETAILS, movieList.get(moviePosition));
                    MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                    movieDetailFragment.setArguments(arguments);
                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container, movieDetailFragment).commit();

                    /*Bundle arguments = new Bundle();
                    arguments.putString(MovieDetailFragment.ARG_ITEM_ID, movieViewHolder.mItem.id);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();*/
                } else {
                    Intent intent = new Intent(mContext, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movieList.get(moviePosition));
                    mContext.startActivity(intent);
                }
            }
        });
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        if ( !showingFavorites ) {

            Movie movie = movieList.get(position);
            String imagePath = ApiUtility.MovieDbUtility.getCompletePhotoUrl(movie.posterPath);

            Picasso.with(mContext)
                    //.load("http://image.tmdb.org/t/p/w185" + movie.posterPath)
                    .load(imagePath)
                    .into(holder.imageView);
        } else {
            try {
                String filename = String.valueOf(movieList.get(position).id);
                File photofile = new File(mContext.getFilesDir(), filename);
                Bitmap freshBitMap = BitmapFactory.decodeStream(new FileInputStream(photofile));
                holder.imageView.setImageBitmap(freshBitMap);
            } catch (FileNotFoundException e) {
                holder.imageView.setImageResource(R.drawable.poster_placeholder);
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        if ( movieList == null ) {
            return 0;
        } else {
            return movieList.size();
        }
    }
}
