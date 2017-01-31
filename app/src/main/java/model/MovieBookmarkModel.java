package model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by abhinav on 01-01-2017.
 */

public class MovieBookmarkModel extends RealmObject implements Parcelable{

    @PrimaryKey
        public int id;

        public String title;
        public String timestamp;
        public String description;
        public String image;
        public String share;
        public String backposter;
        public String vote;

        public MovieBookmarkModel() {}

    protected MovieBookmarkModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        timestamp = in.readString();
        description = in.readString();
        image = in.readString();
        share=in.readString();
        backposter=in.readString();
        vote=in.readString();
    }

    public static final Creator<MovieBookmarkModel> CREATOR = new Creator<MovieBookmarkModel>() {
        @Override
        public MovieBookmarkModel createFromParcel(Parcel in) {
            return new MovieBookmarkModel(in);
        }

        @Override
        public MovieBookmarkModel[] newArray(int size) {
            return new MovieBookmarkModel[size];
        }
    };

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    public String getShare(){return share;}
    public void setShare(String share){this.share=share;}

    public String getBackposter(){return backposter;}
    public void setBackposter(String backposter){this.backposter=backposter;}

    public String getVote(){return vote;}
    public void setVote(String vote){this.vote=vote;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(timestamp);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(share);
        dest.writeString(backposter);
        dest.writeString(vote);
    }
}
