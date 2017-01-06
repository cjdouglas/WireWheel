package com.wirewheel.activites;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.wirewheel.fragments.AdFragment;

/**
 * Created by Chris on 9/14/2016.
 */
public class AdActivity extends SingleFragmentActivity {

    /**
     * Creates a new instance of this fragment and returns it
     * @return the new created instance of this fragment
     */
    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        return AdFragment.newInstance(intent.getStringExtra(AdFragment.ARG_LISTING_LINK),
                intent.getStringExtra(AdFragment.ARG_LISTING_TABLE_ID));
    }
}
