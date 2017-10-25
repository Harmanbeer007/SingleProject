package sandhu.harman.singleproject.parent;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sandhu.harman.singleproject.R;

/**
 * Created by harman on 23-10-2017.
 */

public class Exp_Adapter_Location extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Exp_Group_Location> groups;
    private Typeface typeface;

//    ImageLoader imageLoader = SingleProject.getInstance().getImageLoader();

    public Exp_Adapter_Location(Context context, ArrayList<Exp_Group_Location> groups) {
        this.context = context;
        this.groups = groups;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Exp_Child_Location> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Exp_Child_Location child = (Exp_Child_Location) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_exp_child_location, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.country_name);
        tv.setText(child.getName().toString());
        tv.setTypeface(typeface);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Exp_Child_Location> chList = groups.get(groupPosition).getItems();
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Exp_Group_Location group = (Exp_Group_Location) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.layout_exp_parent, null);
        }
        ImageView exp_ico = (ImageView) convertView.findViewById(R.id.expande_ico);
        exp_ico.setSelected(isExpanded);

        TextView tv = (TextView) convertView.findViewById(R.id.group_name);
        tv.setText(group.getName());
        tv.setTypeface(typeface);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}