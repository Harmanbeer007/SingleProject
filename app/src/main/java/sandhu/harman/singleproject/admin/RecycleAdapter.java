package sandhu.harman.singleproject.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import sandhu.harman.singleproject.R;

import static android.content.Context.MODE_PRIVATE;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyHoder> {

    List<BusInfo> list;
    Context context;
    String college;

    public RecycleAdapter(List<BusInfo> list, Context context) {
        this.list = list;
        this.context = context;
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.userLoggedPrefs), MODE_PRIVATE);
        college = settings.getString(context.getString(R.string.college_id), " ");
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.listlook, parent, false);
        MyHoder myHoder = new MyHoder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(MyHoder holder, final int position) {
        final BusInfo mylist = list.get(position);
        holder.name.setText("Driver Name: " + mylist.getDrivername());
        holder.driverNumber.setText("Driver Number: " + mylist.getDriverNumber());
        holder.driverCollege.setText("Route: " + mylist.getBusRoute());
        holder.busAssigned.setText(mylist.getBusNumber());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, DriverInformation.class).putExtra("DATA", list.get(position)));

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
        private final TextView driverCollege;
        private final TextView busAssigned;
        TextView name, driverNumber;
        View cardView;


        public MyHoder(View itemView) {
            super(itemView);
            cardView = itemView;
            name = (TextView) itemView.findViewById(R.id.textDriverName);
            driverNumber = (TextView) itemView.findViewById(R.id.textDriverNumber);
            driverCollege = (TextView) itemView.findViewById(R.id.textDriverCollege);
            busAssigned = (TextView) itemView.findViewById(R.id.textBusAssigned);
        }
    }


}

