package sandhu.harman.singleproject;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by harman on 23-10-2017.
 */

public class SingleProject extends Application {
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static SingleProject mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized SingleProject getInstance() {
        return mInstance;
    }

    public RequestQueue getReqQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToReqQueue(Request<T> req, String tag) {

        getReqQueue().add(req);
    }

    public <T> void addToReqQueue(Request<T> req) {

        getReqQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        getReqQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new BitmapLruCache());
        }
        return this.mImageLoader;
    }

    public void cancelPendingReq(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
