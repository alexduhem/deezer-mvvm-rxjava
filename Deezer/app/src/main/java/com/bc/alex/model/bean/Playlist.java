package com.bc.alex.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 19/06/17.
 */

public class Playlist implements Parcelable{
    int id;
    String title;
    String description;
    int duration;
    String formattedDuration;
    boolean isPublic;
    @SerializedName("nb_tracks")
    int trackNumber;
    @SerializedName("picture")
    String pictureUrl;
    @SerializedName("picture_small")
    String pictureSmallUrl;
    @SerializedName("picture_medium")
    String pictureMediumUrl;
    @SerializedName("picture_big")
    String pictureBigUrl;
    @SerializedName("creator")
    DeezerCreator creator;


    protected Playlist(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        duration = in.readInt();
        formattedDuration = in.readString();
        isPublic = in.readByte() != 0;
        trackNumber = in.readInt();
        pictureUrl = in.readString();
        pictureSmallUrl = in.readString();
        pictureMediumUrl = in.readString();
        pictureBigUrl = in.readString();
        creator = in.readParcelable(DeezerCreator.class.getClassLoader());
    }

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

    public void setFormattedDuration(String formattedDuration) {
        this.formattedDuration = formattedDuration;
    }



    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getPictureSmallUrl() {
        return pictureSmallUrl;
    }

    public String getPictureMediumUrl() {
        return pictureMediumUrl;
    }

    public String getPictureBigUrl() {
        return pictureBigUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(duration);
        dest.writeString(formattedDuration);
        dest.writeByte((byte) (isPublic ? 1 : 0));
        dest.writeInt(trackNumber);
        dest.writeString(pictureUrl);
        dest.writeString(pictureSmallUrl);
        dest.writeString(pictureMediumUrl);
        dest.writeString(pictureBigUrl);
        dest.writeParcelable(creator, flags);
    }

    public DeezerCreator getCreator() {
        return creator;
    }

    public String getFormattedDuration() {
        return formattedDuration;
    }
}
