package sandhu.harman.singleproject.parent;

import java.util.ArrayList;

/**
 * Created by harman on 23-10-2017.
 */

public class Exp_Group_Location {

    private String Name;
    private ArrayList<Exp_Child_Location> Items;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public ArrayList<Exp_Child_Location> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Exp_Child_Location> Items) {
        this.Items = Items;
    }


}
