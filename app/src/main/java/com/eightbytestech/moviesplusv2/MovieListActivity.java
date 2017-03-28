package com.eightbytestech.moviesplusv2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.eightbytestech.moviesplusv2.adapter.MovieAdapter;
import com.eightbytestech.moviesplusv2.data.FavoritesContract;
import com.eightbytestech.moviesplusv2.model.Movie;
import com.eightbytestech.moviesplusv2.model.MoviesInfo;
import com.eightbytestech.moviesplusv2.utility.ApiUtility;
import com.eightbytestech.moviesplusv2.utility.MovieDBEndpointInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieListActivity extends AppCompatActivity {

    public static boolean mTwoPane;

    private final static String TAG = "#MoviesPlusV2: ";

    private static String tmdb_end_point;

    @BindView(R.id.movie_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.errorTextView)
    TextView mErrorMessage;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MovieAdapter movieAdapter;

    private String apiKey;
    private String language;
    private String posterEndPoint;
    private String posterEndPointSize;
    private int grid_columns;

    /*whether using offline data from the Favorites Provider */
    private boolean mUsingOfflineData;

    private List<Movie> mMovieList;

    private MovieDBEndpointInterface movieDBEndpointInterface;

    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        assert mRecyclerView != null;

        setupMovieList();

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_popular_movies) {
            return true;
        }*/

        switch (id) {
            case R.id.popular_movies: {
                fetchPopularMovies();
                break;
            }
            case R.id.toprated_movies: {
                fetchTopRatedMovies();
                break;
            }
            case R.id.favorites_movies: {
                fetchFavoritesMenu();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupMovieList() {
        //setupRecyclerView(mRecyclerView);

        grid_columns = getResources().getInteger(R.integer.gallery_columns);

        tmdb_end_point = getResources().getString(R.string.moviedb_endpoint);

        apiKey = getResources().getString(R.string.moviedb_api_key);

        language = getResources().getString(R.string.moviedb_language);

        posterEndPoint = getResources().getString(R.string.moviedb_poster_endpoint);

        posterEndPointSize = getResources().getString(R.string.moviedb_poster_size);

        populateRecyclerView();

        ApiUtility.setMovieDbApiValues(tmdb_end_point, apiKey, language, posterEndPoint, posterEndPointSize);

        movieDBEndpointInterface = ApiUtility.MovieDbUtility.getMovieDbEndpointInterface();

        fetchPopularMovies();

    }

    private void populateRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, grid_columns));

        movieAdapter = new MovieAdapter(this, mUsingOfflineData);

        mRecyclerView.setAdapter(movieAdapter);
    }

    private void fetchPopularMovies() {
        mUsingOfflineData = false;
        Call<MoviesInfo> popularMovies = movieDBEndpointInterface.getPopularMovies(apiKey, language);
        popularMovies.enqueue(new Callback<MoviesInfo>() {
            @Override
            public void onResponse(Call<MoviesInfo> call, Response<MoviesInfo> response) {
                if ( response.isSuccessful() )
                    updateMovieAdapter(response.body().movieList);
                else
                    showErrorMessage();
            }

            @Override
            public void onFailure(Call<MoviesInfo> call, Throwable t) {
                showErrorMessage();
                t.printStackTrace();
            }
        });
    }

    private void fetchTopRatedMovies() {
        mUsingOfflineData = false;
        Call<MoviesInfo> popularMovies = movieDBEndpointInterface.getTopRatedMovies(apiKey, language);
        popularMovies.enqueue(new Callback<MoviesInfo>() {
            @Override
            public void onResponse(Call<MoviesInfo> call, Response<MoviesInfo> response) {
                if ( response.isSuccessful() )
                    updateMovieAdapter(response.body().movieList);
                else
                    showErrorMessage();
            }

            @Override
            public void onFailure(Call<MoviesInfo> call, Throwable t) {
                showErrorMessage();
                t.printStackTrace();
            }
        });
    }

    private void fetchFavoritesMenu() {
        mUsingOfflineData = true;
        Cursor cursor = getContentResolver().query(FavoritesContract.Favorites.CONTENT_URI,
                new String[]{FavoritesContract.Favorites.COLUMN_MOVIE_ID,
                        FavoritesContract.Favorites.COLUMN_TITLE,
                        FavoritesContract.Favorites.COLUMN_VOTE_AVERAGE,
                        FavoritesContract.Favorites.COLUMN_OVERVIEW,
                        FavoritesContract.Favorites.COLUMN_RELEASE_DATE,
                        FavoritesContract.Favorites.COLUMN_POSTER_PATH,
                        FavoritesContract.Favorites.COLUMN_BACKDROP_PATH,
                        FavoritesContract.Favorites.COLUMN_POPULARITY,
                        FavoritesContract.Favorites.COLUMN_VOTE_COUNT},
                null,
                null,
                null);

        /*use the results from the cursor to make a movie list and update ui */
        // TODO: ultimately will switch to Realm and this won't be a thing
        mMovieList = new ArrayList<>();
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int idColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_MOVIE_ID);
                int titleColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_TITLE);
                int overviewColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_OVERVIEW);
                int voteColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_VOTE_AVERAGE);
                int releaseDateColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_RELEASE_DATE);
                int posterPathColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_POSTER_PATH);
                int backdropPathColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_BACKDROP_PATH);
                int popularityColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_POPULARITY);
                int voteCountColumnIndex = cursor.getColumnIndex(FavoritesContract.Favorites.COLUMN_VOTE_COUNT);

                int id = cursor.getInt(idColumnIndex);
                String title = cursor.getString(titleColumnIndex);
                String overview = cursor.getString(overviewColumnIndex);
                float vote_average = cursor.getFloat(voteColumnIndex);
                String release_date = cursor.getString(releaseDateColumnIndex);
                String poster_path = cursor.getString(posterPathColumnIndex);
                String backdrop_path = cursor.getString(backdropPathColumnIndex);
                float popularity = cursor.getFloat(popularityColumnIndex);
                int vote_count = cursor.getInt(voteCountColumnIndex);



                /*the poster will be set by the adapter, so pass null*/
                //mMovieList.add(new Movie(null, overview, release_date, id, title, vote_average));

                //Movie(int id, String overview, String releaseDate, String posterPath, String backdropPath, long popularity, String title, long voteAverage, int voteCount)
                mMovieList.add(new Movie(id, overview, release_date, poster_path, backdrop_path, popularity, title, vote_average, vote_count));
            }
            cursor.close();

            updateMovieAdapter(mMovieList);

        }
    }


    private void updateMovieAdapter(List<Movie> movies) {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

        populateRecyclerView();

        movieAdapter.setMovieList(movies);
    }

    private void showErrorMessage() {
        mErrorMessage.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    /*private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            //holder.mIdView.setText(mValues.get(position).id);
            //holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        //intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, Integer.toString(holder.getAdapterPosition() + 1) );

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            *//*public final TextView mIdView;
            public final TextView mContentView;*//*
            public final ImageView mPosterView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
               *//* mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);*//*
               mPosterView = (ImageView) view.findViewById(R.id.imageView);
            }

            *//*@Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }*//*
        }
    }*/
}
