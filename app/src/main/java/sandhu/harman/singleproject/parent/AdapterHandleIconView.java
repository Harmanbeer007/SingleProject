package sandhu.harman.singleproject.parent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sandhu.harman.singleproject.R;

/**
 * Created by harman on 02-11-2017.
 */

public class AdapterHandleIconView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int myColor;
    ArrayList<CarouselModel> data = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private JSONArray itemsArray;
    private ArrayList<ProductListGridModel> productItems;
    private JSONObject itemsArrayObj;
    private ProgressDialog pd;

    public AdapterHandleIconView(Context context, ArrayList<CarouselModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.view_image_horizontal, parent, false);
        CaroHolder holder = new CaroHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CaroHolder myHolder = (CaroHolder) holder;
        final CarouselModel ItemPosition = data.get(position);
        ((CaroHolder) holder).textView.setText(data.get(position).getName());
        if (ItemPosition.getImage().isEmpty()) {
            Picasso.with(context).load(R.drawable.shadows).into(myHolder.imageView);   // Picasso.with(context).load(ItemPosition.thumbURL).resize(200, 118).into(myHolder.imageView);
        } else {
            Picasso.with(context).load(ItemPosition.getImage()).placeholder(R.drawable.shadows).into(myHolder.imageView);   // Picasso.with(context).load(ItemPosition.thumbURL).resize(200, 118).into(myHolder.imageView);
        }
        ((CaroHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = data.get(position).getUrl();
                pd = new ProgressDialog(context);
                pd.setTitle("Please Wait");
                pd.setMessage("Loading");
                pd.show();
                getIconsData(url);
            }

            private void getIconsData(final String url) {
                JsonObjectRequest getProduct = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        setProductItems(jsonObject);
                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        if (volleyError instanceof TimeoutError) {
                            if (volleyError instanceof TimeoutError) {
                                new AlertDialog.Builder(context).setTitle("Connection Problem").setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getIconsData(url);
                                    }
                                }).show();
                            }
                        }
                    }
                });
                getProduct.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 3, 1.0f));

                Volley.newRequestQueue(context).add(getProduct);
            }

        });

    }

    private void setProductItems(JSONObject jsonObject) {
        String name = "";
        try {
            if (jsonObject.has("name")) {
                name = jsonObject.getString("name");
            }
            itemsArray = jsonObject.getJSONArray("grid_layout");
            productItems = new ArrayList<>();
            for (int i = 0; i < itemsArray.length(); i++) {
                ProductListGridModel productListGridActivityModel = new ProductListGridModel();
                itemsArrayObj = itemsArray.getJSONObject(i);
                if (itemsArrayObj.has("image_url")) {
                    productListGridActivityModel.setImage(itemsArrayObj.getString("image_url"));
                }
                if (itemsArrayObj.has("offer_price")) {
                    productListGridActivityModel.setPrice(itemsArrayObj.getString("offer_price"));
                }
                if (itemsArrayObj.has("actual_price")) {
                    productListGridActivityModel.setActual_price(itemsArrayObj.getString("actual_price"));
                }

                if (itemsArrayObj.has("tag")) {
                    productListGridActivityModel.setTag(itemsArrayObj.getString("tag"));
                }
                if (itemsArrayObj.has("discount")) {
                    productListGridActivityModel.setOffPercent(itemsArrayObj.getString("discount"));
                }
                if (itemsArrayObj.has("name")) {
                    productListGridActivityModel.setName(itemsArrayObj.getString("name"));
//                    name = jsonObject.getString("name");
                }
                if (itemsArrayObj.has("newurl")) {
                    productListGridActivityModel.setProductUrl(itemsArrayObj.getString("newurl"));
                }
                productItems.add(productListGridActivityModel);

            }
            context.startActivity(new Intent(context, DisplayProduct.class).putExtra("dataProducts", productItems).putExtra("titlename", name));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();

    }

    class CaroHolder extends RecyclerView.ViewHolder

    {

        ImageView imageView;
        View cardView;
        TextView textView;

        public CaroHolder(View itemView) {
            super(itemView);
            cardView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.adapterImageViewIconView);
            textView = (TextView) itemView.findViewById(R.id.txtDescIconView);


        }
    }
}
