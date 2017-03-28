package com.eightbytestech.moviesplusv2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eightbytestech.moviesplusv2.adapter.MovieAdapter;
import com.eightbytestech.moviesplusv2.adapter.ReviewAdapter;
import com.eightbytestech.moviesplusv2.adapter.TrailerAdapter;
import com.eightbytestech.moviesplusv2.data.FavoritesContract;
import com.eightbytestech.moviesplusv2.dummy.DummyContent;
import com.eightbytestech.moviesplusv2.model.Movie;
import com.eightbytestech.moviesplusv2.model.MoviesInfo;
import com.eightbytestech.moviesplusv2.model.Review;
import com.eightbytestech.moviesplusv2.model.ReviewInfo;
import com.eightbytestech.moviesplusv2.model.Trailer;
import com.eightbytestech.moviesplusv2.model.TrailerInfo;
import com.eightbytestech.moviesplusv2.utility.ApiUtility;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    private final static String TAG = "#MoviesPlusV2: ";
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    public static final String MOVIE_DETAILS = "movie_details";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    FloatingActionButton fab;
    ImageButton favoriteButton;
    Drawable favoriteIcon;
    Drawable nonFavoriteIcon;

    private Movie mMovie;

    private boolean isFavorite;

    ImageView poster;
    TextView title;
    TextView description;

    TextView voteAverage;
    TextView releaseDate;

    /*
    REVIEW BLOCK VARIABLES
     */
    RecyclerView reviewRecyclerView;
    ReviewAdapter reviewAdapter;

    /*
    TRAILER BLOCK VARIABLES
     */
    RecyclerView trailerRecyclerView;
    TrailerAdapter trailerAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }

        if ( getArguments().containsKey(MOVIE_DETAILS)) {
            mMovie = getArguments().getParcelable(MOVIE_DETAILS);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mMovie.title);
            }

            setFavoriteStatus();

        }
    }

    private void fetchMovieReviewsTrailers() {

        String apiKey = getResources().getString(R.string.moviedb_api_key);
        String language = getResources().getString(R.string.moviedb_language);

        Call<ReviewInfo> movieReviews = ApiUtility.MovieDbUtility.getMovieDbEndpointInterface().getMovieReviews(mMovie.id, apiKey, language);
        movieReviews.enqueue(new Callback<ReviewInfo>() {
            @Override
            public void onResponse(Call<ReviewInfo> call, Response<ReviewInfo> response) {
                if ( response.isSuccessful() ) 
                    updateReviewAdapter(response.body().reviewList);
                else
                    showErrorMessage();
            }

            @Override
            public void onFailure(Call<ReviewInfo> call, Throwable t) {
                //showErrorMessage();
                t.printStackTrace();
            }
        });

        Call<TrailerInfo> movieTrailers = ApiUtility.MovieDbUtility.getMovieDbEndpointInterface().getMovieVideos(mMovie.id, apiKey, language);
        movieTrailers.enqueue(new Callback<TrailerInfo>() {
            @Override
            public void onResponse(Call<TrailerInfo> call, Response<TrailerInfo> response) {
                if ( response.isSuccessful() )
                    updateTrailerAdapter(response.body().trailerList);
                else
                    showErrorMessage();
            }

            @Override
            public void onFailure(Call<TrailerInfo> call, Throwable t) {
                //showErrorMessage();
                t.printStackTrace();
            }
        });

    }

    private void showErrorMessage() {
    }

    private void updateReviewAdapter(List<Review> reviewList) {
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reviewAdapter = new ReviewAdapter(getContext());

        reviewRecyclerView.setAdapter(reviewAdapter);

        reviewAdapter.setReviewList(reviewList);
    }

    private void updateTrailerAdapter(List<Trailer> trailerList) {
        trailerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        trailerAdapter = new TrailerAdapter(getContext());

        trailerRecyclerView.setAdapter(trailerAdapter);

        trailerAdapter.setTrailerList(trailerList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        //((TextView) rootView.findViewById(R.id.movie_title)).setText(mMovie.title);

        title = (TextView) rootView.findViewById(R.id.movie_title);
        description = (TextView) rootView.findViewById(R.id.movie_description);
        poster = (ImageView) rootView.findViewById(R.id.movie_poster);
        voteAverage = (TextView) rootView.findViewById(R.id.vote_average);
        releaseDate = (TextView) rootView.findViewById(R.id.release_date);

        reviewRecyclerView = (RecyclerView) rootView.findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setHasFixedSize(true);

        trailerRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailerRecyclerView);
        trailerRecyclerView.setHasFixedSize(true);

        if ( title != null )
            title.setText(mMovie.title);
        description.setText(mMovie.overview);
        voteAverage.setText(getResources().getString(R.string.average_vote) + (Float.toString(mMovie.voteAverage)) + "/10.0");
        releaseDate.setText(getResources().getString(R.string.releaste_date) + mMovie.releaseDate);

        String strPosterPath = ApiUtility.MovieDbUtility.getCompletePhotoUrl(mMovie.posterPath);

        if ( poster != null ) {
            if (isFavorite) {
                try {
                    String filename = String.valueOf(mMovie.id);
                    File photofile = new File(getContext().getFilesDir(), filename);
                    Bitmap freshBitMap = BitmapFactory.decodeStream(new FileInputStream(photofile));
                    poster.setImageBitmap(freshBitMap);
                } catch (FileNotFoundException e) {
                    poster.setImageResource(R.drawable.poster_placeholder);
                    e.printStackTrace();
                }
            } else {
                Picasso.with(getContext())
                        .load(strPosterPath)
                        .into(poster);
            }
        }

        favoriteButton = (ImageButton) rootView.findViewById(R.id.favoriteButton);
        favoriteIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_24px);
        nonFavoriteIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_border_24px);

        //check if the current movie is a favorite
        if ( favoriteButton != null ) {
            favoriteButton.setImageDrawable(isFavorite ? favoriteIcon : nonFavoriteIcon);

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateFavoriteStatus();

                    favoriteButton.setImageDrawable(isFavorite ? favoriteIcon : nonFavoriteIcon);
                }
            });
        }
        if ( fab != null ) {
            fab.setImageDrawable(isFavorite ? favoriteIcon : nonFavoriteIcon);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateFavoriteStatus();

                    fab.setImageDrawable(isFavorite ? favoriteIcon : nonFavoriteIcon);
                }
            });
        }

        fetchMovieReviewsTrailers();

        // Show the dummy content as text in a TextView.
        //if (mItem != null) {
        //((TextView) rootView.findViewById(R.id.movie_detail)).setText(mMovie.overview);
        //}

        return rootView;
    }

    /*checks if the Movie is already in the db.  If so, sets isFavorite to true */
    private void setFavoriteStatus() {
        Cursor cursor = getContext().getContentResolver().query(
                FavoritesContract.Favorites.CONTENT_URI,
                new String[]{FavoritesContract.Favorites.COLUMN_MOVIE_ID},
                FavoritesContract.Favorites.COLUMN_MOVIE_ID + " = ? ",
                new String[]{String.valueOf(mMovie.id)},
                null);
        if (cursor != null) {
            int cursorCount = cursor.getCount();
            isFavorite = cursorCount > 0 ? true : false;
            cursor.close();
        }
    }

    private void updateFavoriteStatus() {
        //toggle favorite boolean
        isFavorite = !isFavorite;

        //toggle db
        if (isFavorite) {
            Bitmap bitmap = ((BitmapDrawable) poster.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            addMovie(byteArray);
        } else {
            removeMovie();
        }
    }

    /**
     * Remove Movie from favorites db and delete image from file directory
     */
    private void removeMovie() {
        String currentMovieId = String.valueOf(mMovie.id);
        String whereClause = FavoritesContract.Favorites.COLUMN_MOVIE_ID + " = ?";
        String[] whereArgs = new String[]{currentMovieId};
        int rowsDeleted = getContext().getContentResolver().delete(FavoritesContract.Favorites.CONTENT_URI, whereClause, whereArgs);

        File photofile = new File(getContext().getFilesDir(), currentMovieId);
        if (photofile.exists()) {
            photofile.delete();
        }
    }

    /**
     * Add Movie to favorite db and write image to file
     */
    private void addMovie(byte[] byteArray) {
        /* Add Movie to ContentProvider */
        ContentValues values = new ContentValues();
        values.put(FavoritesContract.Favorites.COLUMN_MOVIE_ID, mMovie.id);
        values.put(FavoritesContract.Favorites.COLUMN_TITLE, mMovie.title);
        values.put(FavoritesContract.Favorites.COLUMN_OVERVIEW, mMovie.overview);
        values.put(FavoritesContract.Favorites.COLUMN_VOTE_AVERAGE, mMovie.voteAverage);
        values.put(FavoritesContract.Favorites.COLUMN_RELEASE_DATE, mMovie.releaseDate);
        values.put(FavoritesContract.Favorites.COLUMN_POSTER_PATH, mMovie.posterPath);
        values.put(FavoritesContract.Favorites.COLUMN_BACKDROP_PATH, mMovie.backdropPath);
        values.put(FavoritesContract.Favorites.COLUMN_POPULARITY, mMovie.popularity);
        values.put(FavoritesContract.Favorites.COLUMN_VOTE_COUNT, mMovie.voteCount);

        Uri insertedMovieUri = getContext().getContentResolver().
                insert(FavoritesContract.Favorites.CONTENT_URI, values);

        /* Write the file to disk */
        String filename = String.valueOf(mMovie.id);
        FileOutputStream outputStream;
        try {
            outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(byteArray);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
