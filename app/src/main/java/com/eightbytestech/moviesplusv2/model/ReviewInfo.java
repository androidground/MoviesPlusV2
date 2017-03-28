package com.eightbytestech.moviesplusv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vishal on 28/3/17.
 */

public class ReviewInfo implements Parcelable {

    @SerializedName("id")
    public int id;

    @SerializedName("page")
    public int page;

    @SerializedName("results")
    public List<Review> reviewList;

    @SerializedName("total_pages")
    public int totalPages;

    @SerializedName("total_results")
    public int totalResults;


    protected ReviewInfo(Parcel parcel) {
        this.id = parcel.readInt();
        this.page = parcel.readInt();
        this.reviewList = parcel.createTypedArrayList(Review.CREATOR);
        this.totalPages = parcel.readInt();
        this.totalResults = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(page);
        dest.writeTypedList(reviewList);
        dest.writeInt(totalPages);
        dest.writeInt(totalResults);
    }

    public static Creator<ReviewInfo> CREATOR = new Creator<ReviewInfo>() {

        @Override
        public ReviewInfo createFromParcel(Parcel source) {
            return new ReviewInfo(source);
        }

        @Override
        public ReviewInfo[] newArray(int size) {
            return new ReviewInfo[size];
        }
    };
}
