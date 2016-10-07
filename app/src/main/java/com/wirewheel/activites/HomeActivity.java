package com.wirewheel.activites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wirewheel.fragments.AdListFragment;
import com.wirewheel.fragments.HomeFragment;
import com.wirewheel.parsing.WebScraper;
import com.wirewheel.wirewheel.R;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private ArrayAdapter<String> mListAdapter;
    private String[] mFragments = {"Lotus", "Jaguar", "TVR"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mListView = (ListView)findViewById(R.id.drawer_list_view);
        mListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mFragments);
        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment;

                switch(position) {
                    case 0:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.LOTUS);
                        break;
                    case 1:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.JAGUAR);
                        break;
                    case 2:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.TVR);
                        break;
                    default:
                        fragment = new HomeFragment();
                }

                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                        .replace(R.id.drawer_relative_layout, fragment)
                        .commit();

                mDrawerLayout.closeDrawers();
            }
        });
    }

}
