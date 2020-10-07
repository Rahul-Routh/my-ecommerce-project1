package com.purpuligo.ajeevikafarmfresh.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShelfLifeImp implements Parcelable {

    public final String name;

    public ShelfLifeImp(String name) {
        this.name = name;
    }

    protected ShelfLifeImp(Parcel in) {
        name = in.readString();
    }

    public static final Creator<ShelfLifeImp> CREATOR = new Creator<ShelfLifeImp>() {
        @Override
        public ShelfLifeImp createFromParcel(Parcel in) {
            return new ShelfLifeImp(in);
        }

        @Override
        public ShelfLifeImp[] newArray(int size) {
            return new ShelfLifeImp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
