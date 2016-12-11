package com.wirewheel.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.wirewheel.wirewheel.R;

/**
 * Created by Chris on 9/30/2016.
 */
public class HomeFragment extends Fragment {

    private ImageButton mCallButton;
    private ImageButton mEmailButton;
    private ImageButton mAddressButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mCallButton = (ImageButton)v.findViewById(R.id.home_call_button);
        mEmailButton = (ImageButton)v.findViewById(R.id.home_email_button);
        mAddressButton = (ImageButton)v.findViewById(R.id.home_address_button);

        mCallButton.setOnClickListener(new View.OnClickListener() {
            //TODO: Add permissions for call intent
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:7082547139"));
                startActivity(callIntent);
            }
        });

        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = "WireWheel Vehicle Inquiry";
                Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                        getString(R.string.email), null));
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                startActivity(mailIntent);
            }
        });

        mAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri intentUri = Uri.parse("geo:0,0?q=" + getString(R.string.address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        return v;
    }
}
