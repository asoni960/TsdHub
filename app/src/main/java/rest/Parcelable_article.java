package rest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhinav on 31-07-2016.
 */
public class Parcelable_article implements Parcelable {
    int id;
    String title;
    public Parcelable_article(int id, String title){
        this.id= id;
        this.title= title;
    }


    public int getId() {
        return id;
    }

    public String gettitle() {
        return title;
    }


    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        // TODO Auto-generated method stub
        dest.writeInt(id);
        dest.writeString(title);
    }

    public Parcelable_article(Parcel in) {
        id = in.readInt();
        title = in.readString();
    }

    public static final Parcelable.Creator<Parcelable_article> CREATOR = new Parcelable.Creator<Parcelable_article>() {
        public Parcelable_article createFromParcel(Parcel in) {
            return new Parcelable_article(in);
        }

        public Parcelable_article[] newArray(int size) {
            return new Parcelable_article[size];
        }
    };
}
