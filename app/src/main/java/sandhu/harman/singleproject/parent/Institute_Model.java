package sandhu.harman.singleproject.parent;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harman on 27-10-2017.
 */

class Institute_Model implements Parcelable {
    String instituteName;
    String insttuteImage;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static Creator<Institute_Model> getCREATOR() {
        return CREATOR;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getInsttuteImage() {
        return insttuteImage;
    }

    public void setInsttuteImage(String insttuteImage) {
        this.insttuteImage = insttuteImage;
    }

    public Institute_Model() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.instituteName);
        dest.writeString(this.insttuteImage);
        dest.writeString(this.url);
    }

    protected Institute_Model(Parcel in) {
        this.instituteName = in.readString();
        this.insttuteImage = in.readString();
        this.url = in.readString();
    }

    public static final Creator<Institute_Model> CREATOR = new Creator<Institute_Model>() {
        @Override
        public Institute_Model createFromParcel(Parcel source) {
            return new Institute_Model(source);
        }

        @Override
        public Institute_Model[] newArray(int size) {
            return new Institute_Model[size];
        }
    };
}
