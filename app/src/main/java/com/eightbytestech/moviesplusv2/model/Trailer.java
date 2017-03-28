package com.eightbytestech.moviesplusv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Trailer implements Parcelable {

    @SerializedName("id")
    public String id;

    @SerializedName("iso_639_1")
    public String iso_639_1;

    @SerializedName("iso_3166_1")
    public String iso_3166_1;

    @SerializedName("key")
    public String key;

    @SerializedName("name")
    public String name;

    @SerializedName("site")
    public String site;

    @SerializedName("size")
    public int size;

    @SerializedName("type")
    public String type;


    public Trailer(Parcel parcel) {
        this.id = parcel.readString();
        this.iso_639_1 = parcel.readString();
        this.iso_3166_1 = parcel.readString();
        this.key = parcel.readString();
        this.name = parcel.readString();
        this.site = parcel.readString();
        this.size = parcel.readInt();
        this.type = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

    public static Creator<Trailer> CREATOR = new Creator<Trailer>() {

        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
