package com.wirewheel.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.wirewheel.wirewheel.R;

/**
 * Created by Chris on 9/17/2016.
 */
public class AdDialogFragment extends DialogFragment {

    private TextView descriptionView;
    private ViewPager mViewPager;

    public static AdDialogFragment newInstance() {
        return new AdDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_ad, null);

        descriptionView = (TextView) view.findViewById(R.id.ad_description_view);
        descriptionView.setMovementMethod(new ScrollingMovementMethod());

        String[] strings = getResources().getStringArray(R.array.test);
        for (String str : strings) {
            descriptionView.append(str);
            descriptionView.append("\n\n");
        }

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        ImageAdapter imageAdapter = new ImageAdapter();
        mViewPager.setAdapter(imageAdapter);


        // Build alertDialog

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog)
                .setView(view)
                .setTitle("2007 Lotus Exige S")
                .setNegativeButton(R.string.dialog_close, null).create();
        alertDialog.show(); // We need to show this first in order for the below params to work

        // Change button text color to black

        final Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setBackgroundColor(Color.argb(255, 0, 100, 45));
        negativeButton.setTextColor(Color.BLACK);

        // Change size of the alert dialog to ~90% of the window

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        alertDialog.getWindow().setAttributes(layoutParams);

        return alertDialog;

        // 09/17/2016: It's 1:31 AM and you have to get up at 6:30 for practice tomorrow...
        // but when you're having fun creating an app and you have good music playing
        // how can you possibly get yourself to stop?
    }

    private class ImageAdapter extends PagerAdapter {

        // private int[] mImages = {R.drawable.testphoto, R.drawable.testphoto2, R.drawable.testphoto3};
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

            Context context = getActivity();
            ImageView imageView = new ImageView(context);
            Ion.with(imageView).fitCenter().load(mImages[position]);

            container.addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }

        private int toPx(int dp) {
            DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
            return (int)((dp * displayMetrics.density) + 0.5f);
        }
    }
}
