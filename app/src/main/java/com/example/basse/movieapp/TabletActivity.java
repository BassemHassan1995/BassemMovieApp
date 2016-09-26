package com.example.basse.movieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TabletActivity extends AppCompatActivity implements Communicator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void sendData(Movie movie) {
        DetailsActivityFragment fragment = (DetailsActivityFragment) getSupportFragmentManager().findFragmentById(R.id.container_detail_fragment_tablet);
        fragment.changeData(movie);
    }
}
