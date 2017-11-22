package sandhu.harman.singleproject.cart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by harman on 14-11-2017.
 */

public class CartDb extends SQLiteOpenHelper {

    private static final String DB_NAME = "CART_DB";
    private static final String TB_NAME = "CART_PRODUCTS";
    private static final String COL_ID = "C_ID";
    private static final String PRODUCT_NAME = "C_PRODUCT_NAME";
    private static final String PRODUCT_DESC = "C_PRODUCT_DESC";
    private static final String PRODUCT_PRICE = "C_PRODUCT_PRICE";
    private static final String PRODUCT_ACTUAL_PRICE = "C_PRODUCT_ACTUAL_PRICE";
    private static final String PRODUCT_IMAGE_URL = "C_PRODUCT_IMAGE_URL";
    private static final String PRODUCT_OFF = "C_PRODUCT_OFF";
    private static final int VERSION = 1;
    private static final String PRODUCT_QUANTITY = "C_PRODUCT_QUANTITY";
    Context context;
    private List<ModelProducts> list;
    private LinkedHashMap<String, String> allproductNameInCart;


    public CartDb(Context context) {

        super(context, DB_NAME, null, VERSION);
        this.context = context;
        list = new LinkedList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TB_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY ," + PRODUCT_NAME + " VARCHAR(200) NOT NULL ," + PRODUCT_DESC + " VARCHAR(200) NOT NULL ," + PRODUCT_PRICE + "  DOUBLE NOT NULL," + PRODUCT_ACTUAL_PRICE + " DOUBLE NOT NULL ," + PRODUCT_IMAGE_URL + " VARCHAR(200) NOT NULL," + PRODUCT_OFF + " INT (10)," + PRODUCT_QUANTITY + "   INT NOT NULL DEFAULT '1');");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }

    public LinkedHashMap<String, String> getAllProductsName() {
        allproductNameInCart = new LinkedHashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PRODUCT_NAME + " FROM   " + TB_NAME, null);
        try {
            if (cursor.moveToFirst()) {
                int i = 0;
                while (!cursor.isAfterLast()) {
                    String name = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                    allproductNameInCart.put("Product" + String.valueOf(i), name);
                    cursor.moveToNext();
                    i++;
                }
            } else {

            }
        } catch (CursorIndexOutOfBoundsException e) {
        }
        db.close();
        return allproductNameInCart;
    }

    public LinkedHashMap<String, String> getAllProductsPrices() {
        allproductNameInCart = new LinkedHashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PRODUCT_PRICE + " FROM   " + TB_NAME, null);
        try {
            if (cursor.moveToFirst()) {
                int i = 0;
                while (!cursor.isAfterLast()) {
                    String price = cursor.getString(cursor.getColumnIndex(PRODUCT_PRICE));
                    allproductNameInCart.put("Product" + String.valueOf(i), price);
                    cursor.moveToNext();
                    i++;
                }
            } else {

            }
        } catch (CursorIndexOutOfBoundsException e) {
        }
        db.close();
        return allproductNameInCart;
    }
    public void addData(String pname, String pdisc, Double pprice, String pactualprice, String url, String off) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cnt = new ContentValues();
        cnt.put(PRODUCT_NAME, pname);
        cnt.put(PRODUCT_DESC, pdisc);
        cnt.put(PRODUCT_PRICE, pprice);
        cnt.put(PRODUCT_ACTUAL_PRICE, pactualprice);
        cnt.put(PRODUCT_IMAGE_URL, url);
        cnt.put(PRODUCT_OFF, off);

        db.insert(TB_NAME, null, cnt);
        db.close();
    }


    public List<ModelProducts> fetchingData() {
        ModelProducts products;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM   " + TB_NAME, null);
        try {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String name = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                    String descr = cursor.getString(cursor.getColumnIndex(PRODUCT_DESC));
                    Double price = cursor.getDouble(cursor.getColumnIndex(PRODUCT_PRICE));
                    String actual = cursor.getString(cursor.getColumnIndex(PRODUCT_ACTUAL_PRICE));
                    String img_url = cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGE_URL));
                    String off = cursor.getString(cursor.getColumnIndex(PRODUCT_OFF));
                    products = new ModelProducts(name, descr, price, actual, img_url, off);

                    list.add(products);
                    cursor.moveToNext();
                }
            } else {

            }
        } catch (CursorIndexOutOfBoundsException e) {
        }
        db.close();
        return (list);

    }


    public int checkCart(String productname) {
        SQLiteDatabase db = this.getReadableDatabase();
        int productInCart = 0;
        String query = "SELECT " + PRODUCT_QUANTITY + " from " + TB_NAME + " WHERE " + PRODUCT_NAME + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{productname});

        try {
            if (cursor.moveToFirst()) {
                productInCart = cursor.getInt(cursor.getColumnIndex(PRODUCT_QUANTITY));
            } else {
                productInCart = 0;
            }
//
        } catch (CursorIndexOutOfBoundsException e) {
        }

        db.close();
        return productInCart;
    }

    public int updateData(String name, int new_Quantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cnt = new ContentValues();
        cnt.put(PRODUCT_QUANTITY, new_Quantity);

        int result = db.update(TB_NAME, cnt, PRODUCT_NAME + "=?", new String[]{name});
        db.close();

        return result;
    }


    public int deleteData(String name) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TB_NAME, PRODUCT_NAME + "=?", new String[]{name});
        db.close();

        return result;
    }

}