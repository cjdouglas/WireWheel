package com.wirewheel.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.wirewheel.listings.Listing;
import com.wirewheel.listings.ListingDatabase;
import com.wirewheel.ui.ProportionalImageView;
import com.wirewheel.wirewheel.R;

import java.util.List;

/**
 * Created by Chris on 9/15/2016.
 */
public class AdListFragment extends Fragment {

    private static final String DIALOG_AD_LISTING = "DialogAdListing";
    private static final String ARG_PAGE_LINK = "link";

    public static AdListFragment newInstance(String link) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PAGE_LINK, link);

        AdListFragment adListFragment = new AdListFragment();
        adListFragment.setArguments(args);
        return adListFragment;
    }

    private RecyclerView mRecyclerView;
    private ListingAdapter mListingAdapter;
    private String link;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            link = (String)getArguments().getSerializable(ARG_PAGE_LINK);
        } else {
            link = "http://www.wirewheel.com/LOTUS.html";
        }

        View v = inflater.inflate(R.layout.fragment_ad_list, container, false);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.list_ad_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDatabase();
                mSwipeRefreshLayout.setRefreshing(false);
                updateUI();
            }
        });

        updateUI();

        return v;
    }

    private class ListingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProportionalImageView mThumbnailView;
        private TextView mTitleView;
        private TextView mPriceView;
        private TextView mMileageView;

        private Listing mListing;

        public ListingHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mThumbnailView = (ProportionalImageView)itemView.findViewById(R.id.list_ad_thumbnail);
            mTitleView = (TextView)itemView.findViewById(R.id.list_ad_title);
            mPriceView = (TextView)itemView.findViewById(R.id.list_ad_price_field);
            mMileageView = (TextView)itemView.findViewById(R.id.list_ad_mileage_field);
        }

        public void bindListing(Listing listing) {
            mListing = listing;
            Ion.with(mThumbnailView).fitCenter().load(mListing.getKeyImageLink());
            mTitleView.setText(mListing.getTitle());
            mPriceView.setText(mListing.getPrice());
            mMileageView.setText(mListing.getMileage());
        }

        @Override
        public void onClick(View v) {
            // Toast.makeText(getActivity(), mListing.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getFragmentManager();
            AdDialogFragment adDialogFragment = AdDialogFragment.newInstance(mListing.getLink());
            adDialogFragment.show(fragmentManager, DIALOG_AD_LISTING);
        }
    }

    private void refreshDatabase() {
        ListingDatabase.get(getActivity()).refreshPage(link);
    }

    private void updateUI() {
        ListingDatabase listingDatabase = ListingDatabase.get(getActivity());
        List<Listing> listings = listingDatabase.getListings();

        if (mListingAdapter == null) {
            mListingAdapter = new ListingAdapter(listings);
            mRecyclerView.setAdapter(mListingAdapter);
        } else {
            mListingAdapter.setListings(listings);
            mListingAdapter.notifyDataSetChanged();
        }
    }

    private class ListingAdapter extends RecyclerView.Adapter<ListingHolder> {
        private List<Listing> mListings;

        public ListingAdapter(List<Listing> listings) {
            mListings = listings;
        }

        @Override
        public ListingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_ad_item, parent, false);
            return new ListingHolder(view);
        }

        @Override
        public void onBindViewHolder(ListingHolder holder, int position) {
            Listing listing = mListings.get(position);
            holder.bindListing(listing);
        }

        @Override
        public int getItemCount() {
            return mListings.size();
        }

        public void setListings(List<Listing> listings) {
            mListings = listings;
        }
    }
}
