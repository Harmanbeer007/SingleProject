package sandhu.harman.singleproject.parent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sandhu.harman.singleproject.R;

/**
 * Created by harman on 03-11-2017.
 */

public class AdapterHandleRow extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int myColor;
    ArrayList<CarouselRowModel> data = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public AdapterHandleRow(Context context, ArrayList<CarouselRowModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.view_image_row, parent, false);
        AdapterHandleRow.CaroRowHolder holder = new AdapterHandleRow.CaroRowHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AdapterHandleRow.CaroRowHolder myHolder = (AdapterHandleRow.CaroRowHolder) holder;
        final CarouselRowModel ItemPosition = data.get(position);

        ((CaroRowHolder) holder).name.setText(data.get(position).getName());
        ((CaroRowHolder) holder).price.setText("₹ " + data.get(position).getPrice());
        ((CaroRowHolder) holder).actual_price.setText(data.get(position).getActual_price());
        ((CaroRowHolder) holder).actual_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (data.get(position).getOffPercent().contains("null")) {
            ((CaroRowHolder) holder).off_percent.setVisibility(View.INVISIBLE);
        }
        ((CaroRowHolder) holder).off_percent.setText("-" + data.get(position).getOffPercent() + "%");
        if (data.get(position).getTag().contains("null")) {
            ((CaroRowHolder) holder).tag.setVisibility(View.INVISIBLE);
        }
        ((CaroRowHolder) holder).tag.setText(data.get(position).getTag());

        if (ItemPosition.getImage().isEmpty()) {
            Picasso.with(context).load(R.drawable.shadows).into(myHolder.imageView);   // Picasso.with(context).load(ItemPosition.thumbURL).resize(200, 118).into(myHolder.imageView);
        } else {
            Picasso.with(context).load(ItemPosition.getImage()).placeholder(R.drawable.shadows).into(myHolder.imageView);   // Picasso.with(context).load(ItemPosition.thumbURL).resize(200, 118).into(myHolder.imageView);
        }
        ((CaroRowHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = data.get(position).getRowProductUrl();
                context.startActivity(new Intent(context, Product_Display_Pay.class).putExtra("productUrl", url));

            }
        });
    }

    @Override
    public int getItemCount() {

        return data.size();

    }

    class CaroRowHolder extends RecyclerView.ViewHolder

    {

        ImageView imageView;
        View cardView;
        TextView name, price, actual_price, off_percent, tag;

        public CaroRowHolder(View itemView) {
            super(itemView);
            cardView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.adapterImageView);
            name = (TextView) itemView.findViewById(R.id.txtName);
            price = (TextView) itemView.findViewById(R.id.txt_offer_price);
            actual_price = (TextView) itemView.findViewById(R.id.txt_actual_price);
            off_percent = (TextView) itemView.findViewById(R.id.txt_off_percent);
            tag = (TextView) itemView.findViewById(R.id.txt_tag);
        }
    }
}
