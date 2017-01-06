/**
 * Outdated class: Using AdDialogFragment
 */

package com.wirewheel.fragments;

import android.content.Context;
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
import com.wirewheel.database.ListingDbSchema;
import com.wirewheel.listings.Listing;
import com.wirewheel.listings.ListingDatabase;
import com.wirewheel.ui.ProportionalImageView;
import com.wirewheel.wirewheel.R;

/**
 * Created by Chris on 9/15/2016.
 */
public class AdFragment extends Fragment {

    public static final String ARG_LISTING_LINK = "link";
    public static final String ARG_LISTING_TABLE_ID = "id";

    private Listing mListing;
    private TextView titleView;
    private ViewPager mViewPager;
    private TextView descriptionView;

    public static AdFragment newInstance(String link, String id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LISTING_LINK, link);
        args.putSerializable(ARG_LISTING_TABLE_ID, id);

        AdFragment fragment = new AdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String link = (String) getArguments().getSerializable(ARG_LISTING_LINK);
            String id = (String) getArguments().getSerializable(ARG_LISTING_TABLE_ID);
            mListing = ListingDatabase.get(getActivity()).getListing(link, id);
        } else {
            String link = "http://www.wirewheel.com/2011-Lotus-Evora-Ardent-Red-for-sale.html";
            String id = ListingDbSchema.ListingTable.NAME_LOTUS;
            mListing = ListingDatabase.get(getActivity()).getListing(link, id);
        }

        // getActivity().setTitle(mListing.getTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ad, container, false);

        titleView = (TextView)v.findViewById(R.id.ad_title_view);
        titleView.setText(mListing.getTitle());

        mViewPager = (ViewPager)v.findViewById(R.id.ad_view_pager);
        ImageAdapter imageAdapter = new ImageAdapter();
        mViewPager.setAdapter(imageAdapter);

        descriptionView = (TextView)v.findViewById(R.id.ad_description_view);
        descriptionView.setMovementMethod(new ScrollingMovementMethod());
        descriptionView.append(mListing.getText());

        return v;
    }

    private class ImageAdapter extends PagerAdapter {
        private String[] mImages = mListing.getImageLinks().split("[|]");

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = getActivity();
            ProportionalImageView imageView = new ProportionalImageView(context);
            Ion.with(imageView).centerCrop().load(mImages[position]);

            container.addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }
}
