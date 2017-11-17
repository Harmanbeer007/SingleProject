package sandhu.harman.singleproject.cart;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import sandhu.harman.singleproject.R;
import sandhu.harman.singleproject.parent.listner.BillListner;

/**
 * Created by harman on 14-11-2017.
 */

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<ModelProducts> data;
    BillListner billListner;

    public CartAdapter(Context context, List<ModelProducts> cartitems) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = cartitems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_cart, parent, false);
        CartViewHolder cartHolder = new CartViewHolder(view);
        return cartHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CartViewHolder cartView = (CartViewHolder) holder;
        cartView.name.setText(data.get(position).getProductName());
        cartView.price.setText("₹ " + data.get(position).getProductPrice().toString());
        cartView.actual.setText("₹ " + data.get(position).getProduct_actual_price().toString());
        cartView.actual.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        cartView.off.setText(data.get(position).getOff_on_product().toString());
        final CartDb cartDb = new CartDb(context);
        if (cartDb.checkCart(data.get(position).getProductName()) > 1) {
            cartView.trash.setVisibility(View.GONE);
            cartView.minus.setVisibility(View.VISIBLE);

        }
        cartView.qnty.setText(String.valueOf(cartDb.checkCart(data.get(position).getProductName())));

        cartView.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quntity = Integer.parseInt(cartView.qnty.getText().toString());
                quntity++;
                cartView.qnty.setText(String.valueOf(quntity));
                cartDb.updateData(data.get(position).getProductName(), Integer.parseInt(cartView.qnty.getText().toString()));
                ((BillListner) context).updateBill();

            }

        });

        cartView.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int quntity = Integer.parseInt(cartView.qnty.getText().toString());
                    quntity--;
                    cartView.qnty.setText(String.valueOf(quntity));
                    cartDb.updateData(data.get(position).getProductName(), Integer.parseInt(cartView.qnty.getText().toString()));
                    ((BillListner) context).updateBill();

                } catch (Exception e) {
                }
            }

        });
        cartView.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CartDb cartDb = new CartDb(context);
                    cartDb.deleteData(data.get(position).getProductName());
                    data.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());
                    ((BillListner) context).updateBill();

                } catch (Exception e) {
                }
            }

        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    if (Integer.parseInt(cartView.qnty.getText().toString()) == 1) {
                        cartView.trash.setVisibility(View.VISIBLE);
                        cartView.minus.setVisibility(View.GONE);
                    } else if (Integer.parseInt(cartView.qnty.getText().toString()) <= 1) {

                    } else {
                        cartView.trash.setVisibility(View.GONE);
                        cartView.minus.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        cartView.qnty.addTextChangedListener(textWatcher);
        Picasso.with(context).load(data.get(position).getProduct_image_url()).placeholder(R.drawable.shadows).into(cartView.cartImg);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView cartImg, plus, minus, trash;
        TextView name, price, qnty, actual, off;

        public CartViewHolder(View itemView) {
            super(itemView);
            cartImg = (ImageView) itemView.findViewById(R.id.product_in_cart_img);
            minus = (ImageView) itemView.findViewById(R.id.product_in_cart_qnty_minus);
            trash = (ImageView) itemView.findViewById(R.id.product_in_cart_qnty_trash);
            plus = (ImageView) itemView.findViewById(R.id.product_in_cart_qnty_plus);
            name = (TextView) itemView.findViewById(R.id.product_in_cart_name);
            price = (TextView) itemView.findViewById(R.id.product_in_cart_price);
            qnty = (TextView) itemView.findViewById(R.id.product_in_cart_qnty);
            actual = (TextView) itemView.findViewById(R.id.product_in_cart_actual_price);
            off = (TextView) itemView.findViewById(R.id.product_in_cart_off);
        }
    }

}
