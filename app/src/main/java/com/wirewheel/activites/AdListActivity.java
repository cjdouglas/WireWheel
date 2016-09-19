package com.wirewheel.activites;

import android.support.v4.app.Fragment;

import com.wirewheel.fragments.AdListFragment;

/**
 * Created by Chris on 9/15/2016.
 */
public class AdListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AdListFragment();
    }
}
