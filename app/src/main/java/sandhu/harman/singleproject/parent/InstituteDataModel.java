package sandhu.harman.singleproject.parent;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harman on 30-10-2017.
 */

public class InstituteDataModel implements Parcelable {
    String schoolName;
    String location;
    String course;
    String amount;
    String title;
    String regex;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.schoolName);
        dest.writeString(this.location);
        dest.writeString(this.course);
        dest.writeString(this.amount);
        dest.writeString(this.title);
        dest.writeString(this.regex);
    }

    public InstituteDataModel() {
    }

    protected InstituteDataModel(Parcel in) {
        this.schoolName = in.readString();
        this.location = in.readString();
        this.course = in.readString();
        this.amount = in.readString();
        this.title = in.readString();
        this.regex = in.readString();
    }

    public static final Parcelable.Creator<InstituteDataModel> CREATOR = new Parcelable.Creator<InstituteDataModel>() {
        @Override
        public InstituteDataModel createFromParcel(Parcel source) {
            return new InstituteDataModel(source);
        }

        @Override
        public InstituteDataModel[] newArray(int size) {
            return new InstituteDataModel[size];
        }
    };
}
