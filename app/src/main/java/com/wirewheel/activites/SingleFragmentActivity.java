package com.wirewheel.activites;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.wirewheel.wirewheel.R;

/**
 * Created by Chris on 9/14/2016.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    /**
     * Creates a new instance of this fragment and returns it
     * @return the new created instance of this fragment
     */
    protected abstract Fragment createFragment();

    /**
     * Returns the resId associated with the fragment's layout
     * @return
     */
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    /**
     * Creates a fragment and commits it to the FragmentManager
     * @param savedInstance the saved instance state which this fragment should resurrect
     */
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
