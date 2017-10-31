package sandhu.harman.singleproject.parent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import sandhu.harman.singleproject.R;

/**
 * Created by harman on 27-10-2017.
 */

public class Institute_Adapter_Parent extends RecyclerView.Adapter<Institute_Adapter_Parent.InstituteHolder> implements Filterable {

    Activity context;
    ArrayList<Institute_Model> list;
    private ArrayList<Institute_Model> mFilteredList;

    public Institute_Adapter_Parent(Activity context, ArrayList<Institute_Model> list) {
        this.list = list;
        mFilteredList = list;
        this.context = context;
    }

    @Override
    public InstituteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.institute_look, parent, false);
        Institute_Adapter_Parent.InstituteHolder myHolder = new Institute_Adapter_Parent.InstituteHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(InstituteHolder holder, final int position) {
        final Institute_Model mylist = mFilteredList.get(position);
        holder.institueName.setText(mylist.getInstituteName());
        Picasso.with(context).load(mylist.getInsttuteImage()).error(R.drawable.no_img).into(holder.instituteImg);
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = mFilteredList.get(position).getInstituteName();
                String details = mFilteredList.get(position).getUrl();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("institute", result);
                returnIntent.putExtra("instituteDetails", details);
                context.setResult(Activity.RESULT_OK, returnIntent);
                context.finish();
//                context.startActivity(new Intent(context, MapsActivity.class).putExtra("DATA", list.get(position)).putExtra("collegeid",list.get(position).getUrl()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
//        int arr = 0;
//
//        try {
//            if (list.size() == 0) {
//
//                arr = 0;
//
//            } else {
//
//                arr = list.size();
//            }
//
//
//        } catch (Exception e) {
//
//
//        }
//
//        return arr;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = list;
                } else {

                    ArrayList<Institute_Model> filteredList = new ArrayList<>();

                    for (Institute_Model institutes : list) {


                        if (has(institutes.getInstituteName().trim().toLowerCase(), charSequence.toString())) {


                            filteredList.add(institutes);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Institute_Model>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }

    private boolean has(String s, String s2) {
        return s.contains(s2);
    }

    class InstituteHolder extends RecyclerView.ViewHolder {

        ImageView instituteImg;
        TextView institueName;
        View cardview;

        public InstituteHolder(View itemView) {
            super(itemView);
            institueName = (TextView) itemView.findViewById(R.id.set_Institute_Name);
            instituteImg = (ImageView) itemView.findViewById(R.id.set_Institute_Image);
            cardview = itemView;
        }
    }

}
