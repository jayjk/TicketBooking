package com.test.ticketbooking;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.test.ticketbooking.adapter.MovieListAdapter;
import com.test.ticketbooking.adapter.SimilarMovieListAdapter;
import com.test.ticketbooking.models.MasterData;
import com.test.ticketbooking.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MovieDetails extends AppCompatActivity {

    ArrayList<MasterData> similarList = new ArrayList<>();

    ImageView iv_poster,iv_banner;

    TextView tv_title,tv_rating,tv_description;

    MasterData movieData = new MasterData();

    RecyclerView rv_similar;

    private MainActivityViewModel mMainActivityViewModel;

    SimilarMovieListAdapter similarMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


init();


setUpData();

        setupRecyclerView();

    }


    private void init() {
        iv_banner = findViewById(R.id.iv_banner);
        iv_poster = findViewById(R.id.iv_poster);

        tv_title = findViewById(R.id.tv_title);
        tv_rating = findViewById(R.id.tv_rating);
        tv_description = findViewById(R.id.tv_description);

        rv_similar = findViewById(R.id.rv_similar);

        movieData = (MasterData) getIntent().getSerializableExtra("id");

        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.init(getApplicationContext());
    }


    private void setUpData() {

        RequestOptions rq = new RequestOptions().placeholder(android.R.drawable.stat_sys_download);

        String url ="https://image.tmdb.org/t/p/original/";

        Glide.with(MovieDetails.this)
                .load(url+ movieData.getBackdrop_path())
                .apply(rq).centerCrop()
                .into(iv_banner);

        Glide.with(MovieDetails.this)
                .load(url+ movieData.getPoster_path())
                .override(500, 600)
                .apply(rq)
                .into(iv_poster);


        tv_description.setText(movieData.getOverview());

        tv_title.setText(movieData.getTitle());

        tv_rating.setText("Ratings: "+movieData.getVote_average());



        mMainActivityViewModel.getSimilarData(String.valueOf(movieData.getGenre_ids().get(0))).observe(MovieDetails.this, new Observer<List<MasterData>>() {
            @Override
            public void onChanged(@Nullable List<MasterData> data) {
                similarList.clear();
                similarList.addAll(data);
            }
        });

    }


    private void setupRecyclerView() {
        if (similarMovieListAdapter == null) {
            similarMovieListAdapter = new SimilarMovieListAdapter(MovieDetails.this, similarList);
            rv_similar.setLayoutManager(new GridLayoutManager(this,2));
            rv_similar.setAdapter(similarMovieListAdapter);
            rv_similar.setItemAnimator(new DefaultItemAnimator());
            rv_similar.setNestedScrollingEnabled(true);
        } else {
            similarMovieListAdapter.notifyDataSetChanged();
        }

    }

}
