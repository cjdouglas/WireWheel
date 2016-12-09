/**
 * Outdated class: Using AdDialogFragment
 */

package com.wirewheel.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.wirewheel.wirewheel.R;

/**
 * Created by Chris on 9/15/2016.
 */
public class AdFragment extends Fragment {

    private TextView titleView;
    private ImageView imageView;
    private TextView descriptionView;
    private ViewPager mViewPager;
    // private ImageView logoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ad, container, false);

        /*
        titleView = (TextView)v.findViewById(R.id.ad_title_view);
        titleView.setText("2007 Lotus Exige S For Sale");
        */

        /*
        imageView = (ImageView)v.findViewById(R.id.ad_image_view);
        imageView.setImageResource(R.mipmap.testphoto);
        */

        descriptionView = (TextView)v.findViewById(R.id.ad_description_view);
        descriptionView.setMovementMethod(new ScrollingMovementMethod());

        /*
        String[] strings = getResources().getStringArray(R.array.test);
        for (String str : strings) {
            descriptionView.append(str);
            descriptionView.append("\n\n");
        }
        */

        mViewPager = (ViewPager)v.findViewById(R.id.view_pager);
        ImageAdapter imageAdapter = new ImageAdapter();
        mViewPager.setAdapter(imageAdapter);

        /*
        logoView = (ImageView)v.findViewById(R.id.logo_placeholder);
        logoView.setImageResource(R.mipmap.logo);
        */

        return v;
    }

    private class ImageAdapter extends PagerAdapter {

        private String[] mImages = {
                "http://www.wirewheel.com/gallery/177579/2006_Lotus_Elise.jpg",
                "http://www.wirewheel.com/gallery/177577/2006_Lotus_Elise.jpg",
                "http://www.wirewheel.com/gallery/177578/2006_Lotus_Elise.jpg",
                "http://www.wirewheel.com/gallery/177580/2006_Lotus_Elise.jpg",
                "http://www.wirewheel.com/gallery/177587/2006_Lotus_Elise.jpg",
                "http://www.wirewheel.com/gallery/177628/2006_Lotus_Elise.jpg" };

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Context context = getActivity();
            // ImageView imageView = (ImageView)container.findViewById(R.id.view_pager_image_view);
            // ImageView imageView = new ImageView(context);

            // imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // imageView.setAdjustViewBounds(false);
            // imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            // imageView.setBackgroundResource(R.drawable.rounded_corner_image);
            // imageView.setImageResource(mImages[position]);

            Ion.with(imageView).centerCrop().load(mImages[position]);

            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }
}
