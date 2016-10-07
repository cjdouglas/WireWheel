package com.wirewheel.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wirewheel.ui.ProportionalImageView;
import com.wirewheel.wirewheel.R;

/**
 * Created by Chris on 9/30/2016.
 */
public class HomeFragment extends Fragment {

    private ProportionalImageView mImageView;
    private TextView mTextView;
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mImageView = (ProportionalImageView)v.findViewById(R.id.home_image_view);
        mTextView = (TextView)v.findViewById(R.id.home_text_view);
        mButton = (Button)v.findViewById(R.id.home_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Fragment fragment = AdListFragment.newInstance("http://www.wirewheel.com/LOTUS.html");
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                ft.add(R.id.fragment_container, fragment).commit();
                */

                Fragment fragment = AdListFragment.newInstance("http://www.wirewheel.com/LOTUS.html", "Lotus");
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        });

        return v;
    }
}
