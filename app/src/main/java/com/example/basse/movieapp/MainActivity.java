package com.example.basse.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//import android.view.View;

public class MainActivity extends AppCompatActivity {


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPager;

    public static MovieDbHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new MovieDbHelper(this);

        if (isTablet(this)) {
            startActivity(new Intent(MainActivity.this, TabletActivity.class));
            this.finish();
        } else {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            MainPagerAdapter mSectionsPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

            FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.search_fab);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    startActivity(intent);
                }
            });

        }
    }
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class MainPagerAdapter extends FragmentPagerAdapter {

        MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Popular";
                case 1:
                    return "Top Rated";
                case 2:
                    return "Now Playing";
                case 3:
                    return "Upcoming";
                case 4:
                    return "Favorites";
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            String sort_type = "";

            if (position == 0)
                sort_type = "popular";

            else if (position == 1)
                sort_type = "top_rated";

            else if (position == 2)
                sort_type = "now_playing";

            else if (position == 3)
                sort_type = "upcoming";

            else if (position == 4)  //Favourites Tab
                return new FavouritesFragment();

            MainActivityFragment myFragment = new MainActivityFragment();
            Bundle args = new Bundle();
            args.putString("sort_type", sort_type);
            myFragment.setArguments(args);
            return myFragment;
        }
    }
}
