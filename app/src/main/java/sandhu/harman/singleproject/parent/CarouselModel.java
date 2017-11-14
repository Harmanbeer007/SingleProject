package sandhu.harman.singleproject.parent;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harman on 02-11-2017.
 */

public class CarouselModel implements Parcelable {

    public static final Creator<CarouselModel> CREATOR = new Creator<CarouselModel>() {
        @Override
        public CarouselModel createFromParcel(Parcel source) {
            return new CarouselModel(source);
        }

        @Override
        public CarouselModel[] newArray(int size) {
            return new CarouselModel[size];
        }
    };
    String Name;
    String image;
    String url;

    public CarouselModel() {
    }

    protected CarouselModel(Parcel in) {
        this.Name = in.readString();
        this.image = in.readString();
        this.url = in.readString();
    }

    public static Creator<CarouselModel> getCREATOR() {
        return CREATOR;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.image);
        dest.writeString(this.url);
    }
}
