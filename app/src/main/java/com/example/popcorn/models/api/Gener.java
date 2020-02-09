package com.example.popcorn.models.api;

import android.os.Parcel;
import android.os.Parcelable;

public class Gener implements Parcelable {

    private Long id;
    private String name;

    public Gener(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Gener(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
    }

    public static final Creator<Gener> CREATOR = new Creator<Gener>() {
        @Override
        public Gener createFromParcel(Parcel in) {
            return new Gener(in);
        }

        @Override
        public Gener[] newArray(int size) {
            return new Gener[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
    }
}
