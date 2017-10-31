package sandhu.harman.singleproject;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by harman on 23-10-2017.
 */

public class SingleProject extends Application {

    public static final String TAG = SingleProject.class.getSimpleName();
    private static SingleProject mInstance;

    private RequestQueue mRequestQueue;

    public static synchronized SingleProject getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        mInstance = this;

    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequest(Object tag) {
        getRequestQueue().cancelAll(tag);
    }
}

//    private RequestQueue mRequestQueue;
//    private ImageLoader mImageLoader;
//    private static SingleProject mInstance;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mInstance = this;
//    }
//
//    public static synchronized SingleProject getInstance() {
//        return mInstance;
//    }
//
//    public RequestQueue getReqQueue() {
//        if (mRequestQueue == null) {
//            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
//        }
//
//        return mRequestQueue;
//    }
//
//    public <T> void addToReqQueue(Request<T> req, String tag) {
//
//        getReqQueue().add(req);
//    }
//
//    public <T> void addToReqQueue(Request<T> req) {
//
//        getReqQueue().add(req);
//    }
//
//    public ImageLoader getImageLoader() {
//        getReqQueue();
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue,
//                    new BitmapLruCache());
//        }
//        return this.mImageLoader;
//    }
//
//    public void cancelPendingReq(Object tag) {
//        if (mRequestQueue != null) {
//            mRequestQueue.cancelAll(tag);
//        }
//    }
//}

