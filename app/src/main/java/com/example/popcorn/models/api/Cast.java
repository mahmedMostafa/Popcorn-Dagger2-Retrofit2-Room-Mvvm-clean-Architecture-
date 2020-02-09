package com.example.popcorn.models.api;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.example.popcorn.util.C;
import com.google.gson.annotations.SerializedName;

public class Cast implements Parcelable {

    @ColumnInfo(name = "profile_path")
    @SerializedName("profile_path")
    private String profilePath;

    private String name;

    private String character;

    @Ignore
    public Cast() {
    }

    public Cast(String profilePath, String name, String character) {
        this.profilePath = profilePath;
        this.name = name;
        this.character = character;
    }


    public String getProfilePath() {
        if (profilePath != null && !profilePath.startsWith("http")) {
            profilePath = String.format(C.IMAGE_URL, profilePath);
        }
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(profilePath);
        dest.writeString(name);
        dest.writeString(character);
    }


    public static final Creator<Cast> CREATOR = new Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };


    protected Cast(Parcel in) {
        profilePath = in.readString();
        name = in.readString();
        character = in.readString();
    }

}
