package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by abhinav on 28-07-2016.
 */
public class MainActivityPojo extends RealmObject implements Parcelable {
    @SerializedName(value = "title", alternate = {"name"})
    public String cardtitle;
    @PrimaryKey
    @SerializedName("id")
    private int movieid;
    @SerializedName(value = "release_date",alternate = {"first_air_date"})
    private String timestamp;
    @SerializedName("poster_path")
    private String imagepath;
    @SerializedName("homepage")
    private String shareurl;
    @SerializedName("overview")
    private String content;
    @SerializedName("backdrop_path")
    private String backPoster;
    @SerializedName("vote_count")
    private String voteCount;

    public MainActivityPojo(){}


    protected MainActivityPojo(Parcel in) {
        cardtitle = in.readString();
        movieid = in.readInt();
        timestamp = in.readString();
        imagepath = in.readString();
        shareurl = in.readString();
        content = in.readString();
        backPoster= in.readString();
        voteCount=in.readString();
    }

    public static final Creator<MainActivityPojo> CREATOR = new Creator<MainActivityPojo>() {
        @Override
        public MainActivityPojo createFromParcel(Parcel in) {
            return new MainActivityPojo(in);
        }

        @Override
        public MainActivityPojo[] newArray(int size) {
            return new MainActivityPojo[size];
        }
    };

    public String getImagepath(){
        return imagepath;
    }
    public void setImagepath(String imagepath ){
        this.imagepath = imagepath;
    }

    public String getCardtitle(){
        return cardtitle;
    }
    public void setCardtitle(String cardtitle ){
        this.cardtitle = cardtitle;
    }

    public String getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(String timestamp ){this.timestamp = timestamp;}

    public int getMovieid(){return movieid;}
    public void setMovieid(int movieid){this.movieid= movieid;}

    public String getShareurl(){return shareurl;}
    public void setShareurl(String shareurl){this.shareurl=shareurl;}

    public String getContent(){return content;}
    public void setContent(String content){this.content=content;}

    public String getBackPoster(){ return backPoster;}
    public void setBackPoster(String backPoster){this.backPoster=backPoster;}

    public String getVoteCount(){return voteCount;}
    public void setVoteCount(String voteCount){this.voteCount= voteCount;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cardtitle);
        parcel.writeInt(movieid);
        parcel.writeString(timestamp);
        parcel.writeString(imagepath);
        parcel.writeString(shareurl);
        parcel.writeString(content);
        parcel.writeString(backPoster);
        parcel.writeString(voteCount);
    }
}

