package sandhu.harman.singleproject.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sandhu.harman.singleproject.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BusTrackAdapterParent extends RecyclerView.Adapter<BusTrackAdapterParent.MyHoder> {

    List<BusInfoparent> list;
    Context context;
    String college;

    public BusTrackAdapterParent(List<BusInfoparent> list, Context context, String user) {

        this.list = list;
        this.context = context;
        if (user.equalsIgnoreCase("parent")) {
            SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.parentPref), MODE_PRIVATE);
            college = settings.getString(context.getString(R.string.college_id), " ");
        } else if (user.equalsIgnoreCase("admin")) {
            SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.adminPref), MODE_PRIVATE);
            college = settings.getString(context.getString(R.string.college_id), " ");
        }

    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.driverlist, parent, false);
        MyHoder myHoder = new MyHoder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(MyHoder holder, final int position) {
        final BusInfoparent mylist = list.get(position);
        holder.name.setText("Driver Name: " + mylist.getDrivername());
        holder.driverNumber.setText("Driver Number: " + mylist.getDriverNumber());
        holder.busRoute.setText("Route to: " + mylist.getBusRoute());
        holder.busAssigned.setText(mylist.getBusNumber());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MapsActivity.class).putExtra("DATA", list.get(position)).putExtra("collegeid", college));

            }
        });
    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try {
            if (list.size() == 0) {

                arr = 0;

            } else {

                arr = list.size();
            }


        } catch (Exception e) {


        }

        return arr;

    }

    class MyHoder extends RecyclerView.ViewHolder {
        private final TextView busRoute;
        private final TextView busAssigned;
        TextView name, driverNumber;
        View cardView;


        public MyHoder(View itemView) {
            super(itemView);
            cardView = itemView;
            name = (TextView) itemView.findViewById(R.id.textDriverName);
            driverNumber = (TextView) itemView.findViewById(R.id.textDriverNumber);
            busRoute = (TextView) itemView.findViewById(R.id.txtBusRoute);
            busAssigned = (TextView) itemView.findViewById(R.id.textBusAssigned);
        }
    }

}

