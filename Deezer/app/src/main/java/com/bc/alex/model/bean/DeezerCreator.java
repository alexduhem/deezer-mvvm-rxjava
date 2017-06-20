package com.bc.alex.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alex on 20/06/17.
 */

public class DeezerCreator implements Parcelable{

    String name;

    protected DeezerCreator(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeezerCreator> CREATOR = new Creator<DeezerCreator>() {
        @Override
        public DeezerCreator createFromParcel(Parcel in) {
            return new DeezerCreator(in);
        }

        @Override
        public DeezerCreator[] newArray(int size) {
            return new DeezerCreator[size];
        }
    };
}
