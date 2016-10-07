package com.wirewheel.activites;

import android.support.v4.app.Fragment;

import com.wirewheel.fragments.HomeFragment;

public class HomeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new HomeFragment();
    }

}
