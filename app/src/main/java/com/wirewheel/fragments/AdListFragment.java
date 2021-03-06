package com.wirewheel.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.wirewheel.activites.AdActivity;
import com.wirewheel.listings.Listing;
import com.wirewheel.listings.ListingDatabase;
import com.wirewheel.ui.ProportionalImageView;
import com.wirewheel.ui.RecyclerViewEmpty;
import com.wirewheel.wirewheel.R;

import java.util.List;

/**
 * Created by Chris on 9/15/2016.
 */
public class AdListFragment extends Fragment {

    private static final String EXTRA_ADDRESS = "Hayes@wirewheel.com";
    private static final String DIALOG_AD_LISTING = "DialogAdListing";
    private static final String ARG_PAGE_LINK = "link";
    private static final String ARG_PAGE_ID = "id";

    /**
     * Creates a new instance of an AdListFragment
     * @param link the link to the listing it is displaying
     * @param id the id of the SQL table which the listing is associated with
     * @return a new instance of an AdListFragment with the given arguments attached
     */
    public static AdListFragment newInstance(String link, String id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PAGE_LINK, link);
        args.putSerializable(ARG_PAGE_ID, id);

        AdListFragment adListFragment = new AdListFragment();
        adListFragment.setArguments(args);
        return adListFragment;
    }

    private RecyclerViewEmpty mRecyclerView;
    private ListingAdapter mListingAdapter;
    private TextView mEmptyView;
    private String link;
    private String id;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            link = (String)getArguments().getSerializable(ARG_PAGE_LINK);
            id = (String)getArguments().getSerializable(ARG_PAGE_ID);
        } else {
            link = "http://www.wirewheel.com/LOTUS.html";
            id = "Lotus";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ad_list, container, false);

        mRecyclerView = (RecyclerViewEmpty) v.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setEmptyView(v.findViewById(R.id.list_empty_view));

        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.list_ad_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RefreshTask().execute();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        new RefreshTask().execute();
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

            mThumbnailView = (ProportionalImageView)itemView.findViewById(R.id.list_ad_photo);
            mTitleView = (TextView)itemView.findViewById(R.id.list_ad_title);
            mPriceView = (TextView)itemView.findViewById(R.id.list_ad_price);
            mMileageView = (TextView)itemView.findViewById(R.id.list_ad_mileage);


            mFloatingActionButton = (FloatingActionButton)itemView.findViewById(R.id.list_ad_fab);
            mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subject = "Interested in your " + mListing.getTitle();

                    Intent emailIntent = new Intent(
                            Intent.ACTION_SENDTO, Uri.fromParts("mailto", EXTRA_ADDRESS, null)
                    );
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    startActivity(Intent.createChooser(emailIntent, "I'm Interested!"));
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
            /*
            FragmentManager fragmentManager = getFragmentManager();
            AdDialogFragment adDialogFragment = AdDialogFragment.newInstance(mListing.getLink(), id);
            adDialogFragment.show(fragmentManager, DIALOG_AD_LISTING);
            */
            Intent intent = new Intent(getActivity(), AdActivity.class);
            intent.putExtra(AdFragment.ARG_LISTING_LINK, mListing.getLink());
            intent.putExtra(AdFragment.ARG_LISTING_TABLE_ID, id);
            startActivity(intent);
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

    /**
     * Async task associated with refreshing the RecyclerView after it is updated
     */
    private class RefreshTask extends AsyncTask<Void, Void, Void> {

        /**
         * Method to refresh the database
         * @param params Void
         * @return Void
         */
        @Override
        protected Void doInBackground(Void... params) {
            refreshDatabase();
            return null;
        }

        /**
         * Cancels the refresh animation on the refresh View and updates the UI
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            mSwipeRefreshLayout.setRefreshing(false);
            updateUI();
        }
    }
}
