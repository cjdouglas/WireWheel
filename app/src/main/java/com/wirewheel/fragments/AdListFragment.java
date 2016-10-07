package com.wirewheel.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String ARG_PAGE_ID = "id";

    public static AdListFragment newInstance(String link, String id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PAGE_LINK, link);
        args.putSerializable(ARG_PAGE_ID, id);

        AdListFragment adListFragment = new AdListFragment();
        adListFragment.setArguments(args);
        return adListFragment;
    }

    private RecyclerView mRecyclerView;
    private ListingAdapter mListingAdapter;
    private String link;
    private String id;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            link = (String)getArguments().getSerializable(ARG_PAGE_LINK);
            id = (String)getArguments().getSerializable(ARG_PAGE_ID);
        } else {
            link = "http://www.wirewheel.com/LOTUS.html";
            id = "Lotus";
        }

        // ListingDatabase.get(getActivity()).refreshPage(link, id);
        new RefreshTask().execute();

        View v = inflater.inflate(R.layout.fragment_ad_list, container, false);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.list_ad_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RefreshTask().execute();
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

        private FloatingActionButton mFloatingActionButton;

        private Listing mListing;

        public ListingHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mThumbnailView = (ProportionalImageView)itemView.findViewById(R.id.list_ad_thumbnail);
            mTitleView = (TextView)itemView.findViewById(R.id.list_ad_title);
            mPriceView = (TextView)itemView.findViewById(R.id.list_ad_price_field);
            mMileageView = (TextView)itemView.findViewById(R.id.list_ad_mileage_field);
            mFloatingActionButton = (FloatingActionButton)itemView.findViewById(R.id.list_ad_floating_email_button);
            mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Email Filler", Toast.LENGTH_SHORT).show();
                }
            });
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
            AdDialogFragment adDialogFragment = AdDialogFragment.newInstance(mListing.getLink(), id);
            adDialogFragment.show(fragmentManager, DIALOG_AD_LISTING);
        }
    }

    private void refreshDatabase() {
        ListingDatabase.get(getActivity()).refreshPage(link, id);
    }

    private void updateUI() {
        ListingDatabase listingDatabase = ListingDatabase.get(getActivity());
        List<Listing> listings = listingDatabase.getListingsFrom(id);

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

    private class RefreshTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            refreshDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            updateUI();
        }
    }
}
