package sandhu.harman.singleproject.admin;

/**
 * Created by PortDesk on 15-Sep-17.
 */

public class AccessInfo {

    private String college;

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public AccessInfo() {

    }

    public AccessInfo(String college, String email) {
        this.college = college;
        this.email = email;
    }

}
