package sandhu.harman.singleproject.parent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sandhu.harman.singleproject.R;
import sandhu.harman.singleproject.cart.CartDb;
import sandhu.harman.singleproject.cart.CartHead;

/**
 * Created by harman on 07-11-2017.
 */

public class DisplayProduct extends CartHead {
    private ArrayList<ProductListGridModel> productItems;
    private RecyclerView productsRec;
    private String title;
    private TextView itemCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view_layout);
        productsRec = (RecyclerView) findViewById(R.id.productsRecyler);
        productItems = new ArrayList<>();
        Intent i = getIntent();
        setCartIcon();

        setHead();
        productItems = i.getExtras().getParcelableArrayList("dataProducts");
        title = i.getExtras().getString("titlename");
        TextView textCartTitle = (TextView) this.findViewById(R.id.titleCartHead);
        textCartTitle.setText(title);
        AdapterHandleProducts adapter = new AdapterHandleProducts(DisplayProduct.this, productItems);
        productsRec.setLayoutManager(new GridLayoutManager(DisplayProduct.this, 2));
        productsRec.setAdapter(adapter);

    }

    @Override
    public Double updateBill() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartIcon();
    }

    public void setCartIcon() {
        CartDb cartDb = new CartDb(this);
        int size = cartDb.fetchingData().size();
        itemCount = (TextView) findViewById(R.id.itemsInCartCount);
        try {
            if (size > 0) {
                ((ImageView) findViewById(R.id.cartIco)).setImageResource(R.drawable.cart_filled);

                itemCount.setVisibility(View.VISIBLE);
                itemCount.setText(String.valueOf(size));

            } else {
                itemCount.setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.cartIco)).setImageResource(R.drawable.cart_empty);
            }

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
