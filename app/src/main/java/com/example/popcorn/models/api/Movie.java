package com.example.popcorn.models.api;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.popcorn.persistence.converters.CastListTypeConverter;
import com.example.popcorn.persistence.converters.GenerListTypeConverter;
import com.example.popcorn.util.C;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "movies")
public class Movie implements Parcelable {

    @Expose
    @SerializedName("id")
    @NonNull
    @PrimaryKey
    private Long id;

    @Expose
    @SerializedName("vote_count")
    private int voters;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    private String posterPath;

    @Expose
    @SerializedName("overview")
    private String description;

    @Expose
    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    private String releaseDate;

    @Expose
    @TypeConverters(CastListTypeConverter.class)
    private List<Cast> casts;

    @Expose
    @TypeConverters(GenerListTypeConverter.class)
    private List<Gener> geners;


    @ColumnInfo(name = "refresh_time")
    private int timestamp;

    @Ignore
    public Movie() {
    }

    public Movie(Long id, int voters, String title, String posterPath, String description,
                 String releaseDate, List<Cast> casts, List<Gener> geners, int timestamp) {
        this.id = id;
        this.voters = voters;
        this.title = title;
        this.posterPath = posterPath;
        this.description = description;
        this.releaseDate = releaseDate;
        this.casts = casts;
        this.geners = geners;
        this.timestamp = timestamp;
    }

    protected Movie(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        voters = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        description = in.readString();
        releaseDate = in.readString();
        geners = in.createTypedArrayList(Gener.CREATOR);
        timestamp = in.readInt();
    }


    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }

    public List<Gener> getGeners() {
        return geners;
    }

    public void setGeners(List<Gener> geners) {
        this.geners = geners;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVoters() {
        return voters;
    }

    public void setVoters(int voters) {
        this.voters = voters;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        if(posterPath != null && !posterPath.startsWith("http")) {
            posterPath = String.format(C.IMAGE_URL, posterPath);
        }
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeInt(voters);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(description);
        dest.writeString(releaseDate);
        dest.writeTypedList(geners);
        dest.writeInt(timestamp);
    }


    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", voters=" + voters +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", casts=" + casts +
                ", geners=" + geners +
                ", timestamp=" + timestamp +
                '}';
    }
}
