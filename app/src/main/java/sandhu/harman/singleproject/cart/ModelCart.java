package sandhu.harman.singleproject.cart;

import java.util.ArrayList;

/**
 * Created by harman on 14-11-2017.
 */


public class ModelCart {
    private ArrayList<ModelProducts> cartItems = new ArrayList<ModelProducts>();

    public ModelProducts getProducts(int position) {
        return cartItems.get(position);
    }

    public void setProducts(ModelProducts Products) {
        cartItems.add(Products);
    }

    public int getCartsize() {
        return cartItems.size();
    }

    public boolean CheckProductInCart(ModelProducts aproduct) {
        return cartItems.contains(aproduct);
    }
}