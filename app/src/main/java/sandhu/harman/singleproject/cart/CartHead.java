package sandhu.harman.singleproject.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import sandhu.harman.singleproject.R;
import sandhu.harman.singleproject.parent.listner.BillListner;

abstract public class CartHead extends AppCompatActivity implements BillListner {

    private ImageView cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_head);
        cart = (ImageView) findViewById(R.id.cartIco);
        setHead();
    }

    public void setHead() {
        setCartIcon();

        findViewById(R.id.backButtonPayDisplay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.cartIco).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCart();
            }

            private void getCart() {
                startActivity(new Intent(CartHead.this, myCart.class));
//        final int cartSize = ct.getCart().getCartsize();
//
//        String showString = "";
//
//
//        if(cartSize >0)
//        {
//
//            for(int i=0;i<cartSize;i++)
//            {
//                //Get product details
//                String pName    = ct.getCart().getProducts(i).getProductName();
//                Double pPrice      = ct.getCart().getProducts(i).getProductPrice();
//                String pDisc    = ct.getCart().getProducts(i).getProductDesc();
//
//                showString += " Product Name : "+pName+" "+ "Price : "+pPrice+" "+ "Discription : "+pDisc+"\n";
//            }
//        }
//        else {
//            showString = " Shopping cart is empty. ";
//        }
//
//        Toast.makeText(ct, showString, Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void setCartIcon() {
        CartDb cartDb = new CartDb(this);
        if (cartDb.fetchingData().size() > 0) {
            Toast.makeText(this, "Cart Filled", Toast.LENGTH_SHORT).show();
            ((ImageView) findViewById(R.id.cartIco)).setImageResource(R.drawable.cart_filled);

        }
    }

}
