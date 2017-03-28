package com.eightbytestech.moviesplusv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vishal on 28/3/17.
 */

public class TrailerInfo implements Parcelable {

    @SerializedName("id")
    public int id;

    @SerializedName("results")
    public List<Trailer> trailerList;


    protected TrailerInfo(Parcel parcel) {
        this.id = parcel.readInt();
        this.trailerList = parcel.createTypedArrayList(Trailer.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeTypedList(trailerList);
    }

    public static Creator<TrailerInfo> CREATOR = new Creator<TrailerInfo>() {

        @Override
        public TrailerInfo createFromParcel(Parcel source) {
            return new TrailerInfo(source);
        }

        @Override
        public TrailerInfo[] newArray(int size) {
            return new TrailerInfo[size];
        }
    };
}
