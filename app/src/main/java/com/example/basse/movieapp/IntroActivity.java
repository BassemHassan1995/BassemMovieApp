package com.example.basse.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        int background = getResources().getColor(R.color.red);
        int white = getResources().getColor(R.color.white);

        addSlide(AppIntroFragment.newInstance("Popular Movies", "The Most Popular Movies", R.drawable.screenshot_popular, background));
        addSlide(AppIntroFragment.newInstance("Top Rated Movies", "The Top Rated Movies", R.drawable.screenshot_top_rated, background));
        addSlide(AppIntroFragment.newInstance("Now Playing Movies", "The Now-Playing in Theatres", R.drawable.screenshot_now_playing, background));
        addSlide(AppIntroFragment.newInstance("Coming Soon Movies", "The Movies That Will be In Theatres Soon", R.drawable.screenshot_upcoming, background));
        addSlide(AppIntroFragment.newInstance("Add To Favorites", "Add Any movie to Your Favorite Movies", R.drawable.screenshot_add_to_favorites, background));
        addSlide(AppIntroFragment.newInstance("Search Movies", "Search for Any Movie Name", R.drawable.screenshot_search, background));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(background);
        setSeparatorColor(white);

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent i = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(i);
        finish();
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent i = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(i);
        finish();

        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
