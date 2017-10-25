package sandhu.harman.singleproject.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import sandhu.harman.singleproject.R;

/**
 * Created by PortDesk on 15-Sep-17.
 */

public class Loading {
    static ProgressDialog pd;
    private static Animation animShake;
    private static Animation animMove;

    public static void load(Context context, String title, String message) {

        pd = new ProgressDialog(context);
        pd.setTitle(title);
        pd.setMessage(message);
        pd.show();
    }

    public static void dismiss() {
        if (pd.isShowing()) {
            pd.dismiss();
        }
    }

    public static void setMessge(String msg) {
        if (pd.isShowing()) {
            pd.setMessage(msg);
        }
    }

    public static void shakeIt(Context context, View view) {

        animShake = AnimationUtils.loadAnimation(context, R.anim.shakemyicon);
        view.startAnimation(animShake);


    }

    public static void moveIt(Context context, View view) {

        animMove = AnimationUtils.loadAnimation(context, R.anim.zoom);
        view.startAnimation(animMove);


    }

    public static void hideIt(Context context, View view) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }


//    public Loading(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.shakeNumber, 0, 0);
//        try {
//            distanceExample = ta.getDimension(R.styleable.MyCustomElement_distanceExample, 100.0f);
//        } finally {
//            ta.recycle();
//        }
    // ...
//    }

}
