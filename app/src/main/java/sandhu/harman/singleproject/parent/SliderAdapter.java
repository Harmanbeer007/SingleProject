package sandhu.harman.singleproject.parent;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import sandhu.harman.singleproject.R;

/**
 * Created by PortDesk on 6/17/2017.
 */

class SliderAdapter extends PagerAdapter {

    private List images;
    private LayoutInflater inflater;
    private Context context;
    private ProgressBar progressBar;
    private ImageView myImage;

    public SliderAdapter(Context context, List images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slider_image_look, view, false);
        myImage = (ImageView) myImageLayout.findViewById(R.id.imageSliderTop);
        progressBar = (ProgressBar) myImageLayout.findViewById(R.id.products_Loading_Spinner);
//        myImage.setImageResource(images.get(position).toString());
//         Target target = new Target() {
//            @Override
//            public void onPrepareLoad(Drawable drawable) {
//                myImage.setBackgroundResource(R.color.white);
//                progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onBitmapLoaded(Bitmap photo, Picasso.LoadedFrom from) {
//                myImage.setBackgroundDrawable(new BitmapDrawable(photo));
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable drawable) {
//                myImage.setBackgroundResource(R.color.white);
//                Toast.makeText(context, "Image load Failed", Toast.LENGTH_SHORT).show();
//            }
//        };
        Picasso.with(context).load(images.get(position).toString()).into(myImage, new Callback() {
            @Override
            public void onSuccess() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {

            }
        });
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}