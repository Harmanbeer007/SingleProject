package sandhu.harman.singleproject.parent;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harman on 06-11-2017.
 */

class ProductListGridModel implements Parcelable {

    public static final Creator<ProductListGridModel> CREATOR = new Creator<ProductListGridModel>() {
        @Override
        public ProductListGridModel createFromParcel(Parcel source) {
            return new ProductListGridModel(source);
        }

        @Override
        public ProductListGridModel[] newArray(int size) {
            return new ProductListGridModel[size];
        }
    };
    String Name;
    String image;
    String price;
    String actual_price;
    String offPercent;
    String tag;
    String productUrl;

    public ProductListGridModel() {
    }

    protected ProductListGridModel(Parcel in) {
        this.Name = in.readString();
        this.image = in.readString();
        this.price = in.readString();
        this.actual_price = in.readString();
        this.offPercent = in.readString();
        this.tag = in.readString();
        this.productUrl = in.readString();
    }

    public static Creator<ProductListGridModel> getCREATOR() {
        return CREATOR;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getActual_price() {
        return actual_price;
    }

    public void setActual_price(String actual_price) {
        this.actual_price = actual_price;
    }

    public String getOffPercent() {
        return offPercent;
    }

    public void setOffPercent(String offPercent) {
        this.offPercent = offPercent;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.image);
        dest.writeString(this.price);
        dest.writeString(this.actual_price);
        dest.writeString(this.offPercent);
        dest.writeString(this.tag);
        dest.writeString(this.productUrl);
    }
}
