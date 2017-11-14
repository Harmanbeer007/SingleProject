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
 * Created by harman on 07-11-2017.
 */

public class AdapterHandleProducts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int myColor;
    ArrayList<ProductListGridModel> data = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public AdapterHandleProducts(Context context, ArrayList<ProductListGridModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.all_products_view, parent, false);
        AdapterHandleProducts.HandleProducts holder = new AdapterHandleProducts.HandleProducts(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AdapterHandleProducts.HandleProducts myHolder = (AdapterHandleProducts.HandleProducts) holder;
        final ProductListGridModel ItemPosition = data.get(position);
        ((HandleProducts) holder).name.setText(data.get(position).getName());
        ((HandleProducts) holder).price.setText("₹ " + data.get(position).getPrice());
        ((HandleProducts) holder).actual_price.setText(data.get(position).getActual_price());
        ((HandleProducts) holder).actual_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        ((HandleProducts) holder).actual_price.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
        if (data.get(position).getOffPercent().contains("null")) {
            ((HandleProducts) holder).off_percent.setVisibility(View.GONE);
        }
        ((HandleProducts) holder).off_percent.setText("-" + data.get(position).getOffPercent() + "%");
        if (data.get(position).getTag().contains("null")) {
            ((HandleProducts) holder).tag.setVisibility(View.INVISIBLE);
        }
        ((HandleProducts) holder).tag.setText(data.get(position).getTag());

        if (ItemPosition.getImage().isEmpty()) {
            Picasso.with(context).load(R.drawable.shadows).into(myHolder.imageView);   // Picasso.with(context).load(ItemPosition.thumbURL).resize(200, 118).into(myHolder.imageView);
        } else {
            Picasso.with(context).load(ItemPosition.getImage()).placeholder(R.drawable.shadows).into(myHolder.imageView);   // Picasso.with(context).load(ItemPosition.thumbURL).resize(200, 118).into(myHolder.imageView);
        }
        ((HandleProducts) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = data.get(position).getProductUrl();
                context.startActivity(new Intent(context, Product_Display_Pay.class).putExtra("productUrl", url));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();

    }

    class HandleProducts extends RecyclerView.ViewHolder

    {

        ImageView imageView;
        View cardView;
        TextView name, price, actual_price, off_percent, tag;

        public HandleProducts(View itemView) {
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
