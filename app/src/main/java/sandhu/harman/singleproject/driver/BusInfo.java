package sandhu.harman.singleproject.driver;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PortDesk on 06-Sep-17.
 */

public class BusInfo implements Parcelable {
    private String drivername;
    private String driverNumber;
    private String busNumber;
    private String busRoute;
    private String driverId;


    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public BusInfo() {

    }

    public BusInfo(String Drivername, String DriverNumber, String BusNumber, String BusRoute, String DriverId) {
        this.drivername = Drivername;
        this.driverNumber = DriverNumber;
        this.busNumber = BusNumber;
        this.busRoute = BusRoute;
        this.driverId = DriverId;

    }

    public BusInfo(String Drivername, String DriverNumber, String DriverId) {
        this.drivername = Drivername;
        this.driverNumber = DriverNumber;
        this.driverId = DriverId;

    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute;
    }


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.drivername);
        dest.writeString(this.driverNumber);
        dest.writeString(this.busNumber);
        dest.writeString(this.busRoute);
        dest.writeString(this.driverId);
    }

    protected BusInfo(Parcel in) {
        this.drivername = in.readString();
        this.driverNumber = in.readString();
        this.busNumber = in.readString();
        this.busRoute = in.readString();
        this.driverId = in.readString();
    }

    public static final Creator<BusInfo> CREATOR = new Creator<BusInfo>() {
        @Override
        public BusInfo createFromParcel(Parcel source) {
            return new BusInfo(source);
        }

        @Override
        public BusInfo[] newArray(int size) {
            return new BusInfo[size];
        }
    };
}

//
//    public String name;
//    public String latitude;
//    public String longitude;
//
//
//    // Default constructor required for calls to
//    // DataSnapshot.getValue(User.class)
//    public DriverInfo() {
//    }
//
//    public DriverInfo(String name, String Latitude,String Longitude) {
//        this.name = name;
//        this.latitude = Latitude;
//        this.longitude=Longitude;
//    }
//}