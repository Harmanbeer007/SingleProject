package sandhu.harman.singleproject.parent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by harman on 27-11-2017.
 */

public class AdapterHandleBooks extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Bookholder bookholder = (Bookholder) holder;
//        return bookholder;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Bookholder extends RecyclerView.ViewHolder {

        public Bookholder(View itemView) {
            super(itemView);
        }
    }
}
