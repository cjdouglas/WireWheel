package com.wirewheel.activites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wirewheel.database.ListingDbSchema.ListingTable;
import com.wirewheel.fragments.AdListFragment;
import com.wirewheel.fragments.HomeFragment;
import com.wirewheel.parsing.WebScraper;
import com.wirewheel.wirewheel.R;

/**
 * Huge thanks to [ http://codetheory.in/android-navigation-drawer/ ] for help with the hamburger icon!!!
 */

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private ArrayAdapter<String> mListAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    private String[] mFragments = {
            "Race Cars",
            "Austin Healey",
            "Jaguar",
            "Lotus",
            "Marcos",
            "Mini",
            "MG",
            "Panoz",
            "Triumph",
            "TVR"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mListView = (ListView)findViewById(R.id.drawer_list_view);
        mListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mFragments);
        mListView.setAdapter(mListAdapter);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment;

                switch(position) {
                    case 0:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.RACECARS, ListingTable.NAME_RACECARS);
                        break;
                    case 1:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.AUSTIN, ListingTable.NAME_AUSTIN_HEALEY);
                        break;
                    case 2:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.JAGUAR, ListingTable.NAME_JAGUAR);
                        break;
                    case 3:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.LOTUS, ListingTable.NAME_LOTUS);
                        break;
                    case 4:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.MARCOS, ListingTable.NAME_MARCOS);
                        break;
                    case 5:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.MINI, ListingTable.NAME_MINI);
                        break;
                    case 6:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.MG, ListingTable.NAME_MG);
                        break;
                    case 7:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.PANOZ, ListingTable.NAME_PANOZ);
                        break;
                    case 8:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.TRIUMPH, ListingTable.NAME_TRIUMPH);
                        break;
                    case 9:
                        fragment = AdListFragment.newInstance(WebScraper.WebLinks.TVR, ListingTable.NAME_TVR);
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

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
