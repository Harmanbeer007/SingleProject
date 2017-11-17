package sandhu.harman.singleproject.cart;

/**
 * Created by harman on 14-11-2017.
 */

public class ModelProducts {
    private String productName;
    private String productDesc;
    private Double productPrice;
    private String product_image_url;
    private String off_on_product;
    private String product_actual_price;

    //    public ModelProducts(String productName, String productDesc, Double productPrice){
//        this.productName = productName;
//        this.productDesc = productDesc;
//        this.productPrice = productPrice;
//    }
    public ModelProducts(String productName, String productDesc, Double productPrice, String ActualPrice, String ProductImage, String OffOnProduct) {
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.product_image_url = ProductImage;
        this.off_on_product = OffOnProduct;
        this.product_actual_price = ActualPrice;
    }

    public String getProduct_image_url() {
        return product_image_url;
    }

    public String getOff_on_product() {
        return off_on_product;
    }

    public String getProduct_actual_price() {
        return product_actual_price;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public Double getProductPrice() {
        return productPrice;
    }
}