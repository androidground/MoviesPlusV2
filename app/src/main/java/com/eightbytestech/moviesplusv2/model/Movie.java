package com.eightbytestech.moviesplusv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Home on 01/02/2017.
 */

public class Movie implements Parcelable {

    @SerializedName("id")
    public int id;

    @SerializedName("overview")
    public String overview;

    @SerializedName("release_date")
    public String releaseDate;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("backdrop_path")
    public String backdropPath;

    @SerializedName("popularity")
    public float popularity;

    @SerializedName("title")
    public String title;

    @SerializedName("vote_average")
    public float voteAverage;

    @SerializedName("vote_count")
    public int voteCount;

    /*@SerializedName("genre_ids")
    public List<Genres> genres;*/


    public Movie(int id, String overview, String releaseDate, String posterPath, String backdropPath, float popularity, String title, float voteAverage, int voteCount) {

        this.id = id;
        this.overview = overview;
        this.releaseDate = releaseDate;
        /*this.posterPath = "http://image.tmdb.org/t/p/w500" + posterPath;
        this.backdropPath = "http://image.tmdb.org/t/p/w500" + backdropPath;*/
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.title = title;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        //this.genres = genres;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeFloat(popularity);
        dest.writeString(title);
        dest.writeFloat(voteAverage);
        dest.writeInt(voteCount);
        //dest.writeTypedList(genres);
    }

    public Movie(Parcel parcel) {
        this.id = parcel.readInt();
        this.overview = parcel.readString();
        this.releaseDate = parcel.readString();
        this.posterPath = parcel.readString();
        this.backdropPath = parcel.readString();
        this.popularity = parcel.readFloat();
        this.title = parcel.readString();
        this.voteAverage = parcel.readFloat();
        this.voteCount = parcel.readInt();
        //this.genres = parcel.createTypedArrayList(Genres.CREATOR);
    }


    public static Creator<Movie> CREATOR = new Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
