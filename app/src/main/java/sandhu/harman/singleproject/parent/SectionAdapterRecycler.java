package sandhu.harman.singleproject.parent;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import sandhu.harman.singleproject.R;

//import com.synnapps.carouselview.ImageListener;

/**
 * Created by harman on 02-11-2017.
 */

public class SectionAdapterRecycler extends StatelessSection {
    //    ArrayList<CarouselModel> data = new ArrayList<>();
    JSONObject homepage_layoutObj;
    String title;
    private Context context;
    private LayoutInflater inflater;
    private String layout_type;
    private JSONArray itemsArray;
    private ArrayList<CarouselModel> data;
    private ArrayList<CarouselRowModel> dataRow;
    private JSONObject itemsArrayObj;
//    private ImageListener imagelistner;

    public SectionAdapterRecycler(Context context, String title, JSONObject data) {
        super(new SectionParameters.Builder(R.layout.view_custom).headerResourceId(R.layout.view_title).build());
        this.context = context;
        this.title = title;
        inflater = LayoutInflater.from(context);
        this.homepage_layoutObj = data;
    }


    @Override
    public int getContentItemsTotal() {
        return 1; // number of items of this section
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyItemViewHolder itemHolder = (MyItemViewHolder) holder;
        try {
            layout_type = homepage_layoutObj.getString("layout");

            if (layout_type.contains("carousel-2")) {
                setCarouselView();
                ((MyItemViewHolder) holder).recyclerView.setHasFixedSize(true);
                AdapterHandleCarousel adapter = new AdapterHandleCarousel(context, data);
                ((MyItemViewHolder) holder).recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                ((MyItemViewHolder) holder).recyclerView.setAdapter(adapter);
            } else if (layout_type.contains("smart-icon-list")) {
                setIconListView();
                ((MyItemViewHolder) holder).recyclerView.setHasFixedSize(true);
                AdapterHandleIconView adapter = new AdapterHandleIconView(context, data);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                ((MyItemViewHolder) holder).recyclerView.setLayoutManager(linearLayoutManager);
                ((MyItemViewHolder) holder).recyclerView.setAdapter(adapter);
            } else if (layout_type.contains("row")) {
                setRowView();
                ((MyItemViewHolder) holder).recyclerView.setHasFixedSize(true);
                AdapterHandleRow adapter = new AdapterHandleRow(context, dataRow);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                ((MyItemViewHolder) holder).recyclerView.setLayoutManager(linearLayoutManager);
                ((MyItemViewHolder) holder).recyclerView.setAdapter(adapter);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setRowView() {
        String name = "";
        try {
            itemsArray = homepage_layoutObj.getJSONArray("items");
            dataRow = new ArrayList<>();
            for (int i = 0; i < itemsArray.length(); i++) {
                CarouselRowModel carouseRowlModel = new CarouselRowModel();
                itemsArrayObj = itemsArray.getJSONObject(i);
                if (itemsArrayObj.has("image_url")) {
                    carouseRowlModel.setImage(itemsArrayObj.getString("image_url"));
                }
                if (itemsArrayObj.has("offer_price")) {
                    carouseRowlModel.setPrice(itemsArrayObj.getString("offer_price"));
                }
                if (itemsArrayObj.has("actual_price")) {
                    carouseRowlModel.setActual_price(itemsArrayObj.getString("actual_price"));
                }

                if (itemsArrayObj.has("tag")) {
                    carouseRowlModel.setTag(itemsArrayObj.getString("tag"));
                }
                if (itemsArrayObj.has("discount")) {
                    carouseRowlModel.setOffPercent(itemsArrayObj.getString("discount"));
                }
                if (homepage_layoutObj.has("name")) {
                    carouseRowlModel.setName(itemsArrayObj.getString("name"));
                    name = homepage_layoutObj.getString("name");
                }
                if (itemsArrayObj.has("newurl")) {
                    carouseRowlModel.setRowProductUrl(itemsArrayObj.getString("newurl"));
                }
                dataRow.add(carouseRowlModel);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setCarouselView() {

        String name = "";
        try {
            itemsArray = homepage_layoutObj.getJSONArray("items");
            data = new ArrayList<>();
            for (int i = 0; i < itemsArray.length(); i++) {
                CarouselModel carouselModel = new CarouselModel();
                itemsArrayObj = itemsArray.getJSONObject(i);
                if (itemsArrayObj.has("image_url")) {
                    carouselModel.setImage(itemsArrayObj.getString("image_url"));
                }
                if (homepage_layoutObj.has("name")) {
                    carouselModel.setName(itemsArrayObj.getString("name"));
                    name = homepage_layoutObj.getString("name");
                }
                if (itemsArrayObj.has("url")) {
                    carouselModel.setUrl(itemsArrayObj.getString("url"));
                }
                data.add(carouselModel);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setIconListView() {
        String name = "";
        try {
            itemsArray = homepage_layoutObj.getJSONArray("items");
            data = new ArrayList<>();
            for (int i = 0; i < itemsArray.length(); i++) {
                CarouselModel carouselModel = new CarouselModel();
                itemsArrayObj = itemsArray.getJSONObject(i);
                if (itemsArrayObj.has("image_url")) {
                    carouselModel.setImage(itemsArrayObj.getString("image_url"));
                }
                if (itemsArrayObj.has("name")) {
                    carouselModel.setName(itemsArrayObj.getString("name"));
                    name = homepage_layoutObj.getString("name");
                }
                if (itemsArrayObj.has("url")) {
                    carouselModel.setUrl(itemsArrayObj.getString("url"));
                }
                data.add(carouselModel);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new SimpleHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        SimpleHeaderViewHolder headerHolder = (SimpleHeaderViewHolder) holder;
        headerHolder.title.setText(title);
// TODO: 07-11-2017  more button over row view//

//        try {
//            layout_type = homepage_layoutObj.getString("layout");
//            if (layout_type.contains("row")) {
//                headerHolder.labelTextMore.setVisibility(View.VISIBLE);
//
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        headerHolder.labelTextMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "More", Toast.LENGTH_SHORT).show();
////                context.startActivity(new Intent(context,DisplayProduct.class).putExtra("dataProducts",dataRow));
//            }
//        });
    }


    private class SimpleHeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView labelTextMore;
        TextView title;

        public SimpleHeaderViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.labelTextView);
            labelTextMore = (TextView) view.findViewById(R.id.labelTextMore);

        }

    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        RecyclerView recyclerView;

        public MyItemViewHolder(View view) {
            super(view);
//            imageView = (ImageView) view.findViewById(R.id.fruitImageView);
            recyclerView = (RecyclerView) view.findViewById(R.id.innerRecylerFirst);

        }
    }

}
