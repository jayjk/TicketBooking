package com.test.ticketbooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Toast;

import com.test.ticketbooking.adapter.MovieListAdapter;
import com.test.ticketbooking.models.MasterData;
import com.test.ticketbooking.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private MainActivityViewModel mMainActivityViewModel;


    ArrayList<MasterData> masterList = new ArrayList<>();

    ArrayList<MasterData> searchList = new ArrayList<>();

    private ProgressDialog dialog;

    RecyclerView rv_movie;
    MovieListAdapter movieListAdapter;

    boolean isScrolling = false;

    int currentItem,totalItems,scrollItems, pageNumber = 1;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setupRecyclerView();

        fetchData();
    }


    private void init() {
        rv_movie = findViewById(R.id.rv_movie);

        dialog = new ProgressDialog(MainActivity.this);
    }





    private void setupRecyclerView() {
        if (movieListAdapter == null) {
            movieListAdapter = new MovieListAdapter(MainActivity.this, searchList);
            rv_movie.setLayoutManager(new LinearLayoutManager(this));
            rv_movie.setAdapter(movieListAdapter);
            rv_movie.setItemAnimator(new DefaultItemAnimator());
            rv_movie.setNestedScrollingEnabled(true);
        } else {
            movieListAdapter.notifyDataSetChanged();
        }

        rv_movie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = recyclerView.getLayoutManager().getChildCount();
                totalItems = recyclerView.getLayoutManager().getItemCount();
                scrollItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();


                // To identify user has finished scrolling and fetch data for next page

                if(isScrolling && (currentItem + scrollItems == totalItems)){
                    pageNumber = pageNumber+1;
                    isScrolling = false;
                    if (pageNumber<4){
                        mMainActivityViewModel.getMasterData(pageNumber).observe(MainActivity.this, new Observer<List<MasterData>>() {
                            @Override
                            public void onChanged(@Nullable List<MasterData> data) {
                                if (data!=null){
                                    masterList.addAll(data);
                                    searchList.addAll(masterList);
                                }

                                else
                                    Toast.makeText(MainActivity.this,"Search Failed. Try Again",Toast.LENGTH_SHORT).show();

                                movieListAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

            }
        });

    }


    private void fetchData() {
        dialog.setMessage("Fetching Movies");
        dialog.show();

        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.init(getApplicationContext());
        mMainActivityViewModel.getMasterData(1).observe(MainActivity.this, new Observer<List<MasterData>>() {
            @Override
            public void onChanged(@Nullable List<MasterData> data) {
                dialog.dismiss();
                masterList.addAll(data);
                searchList.addAll(masterList);
                movieListAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {

        getMenuInflater().inflate(R.menu.menu,m);

        MenuItem searchViewMenuItem = m.findItem(R.id.search);
        searchView = (SearchView) searchViewMenuItem.getActionView();

        searchView.setQueryHint("Enter Movie Title");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do your search
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if (!newText.isEmpty()){
                    searchList.clear();


                    if (!newText.contains(" ")){
                        for (int i = 0; i < masterList.size(); i++) {
                            if (masterList.get(i).getOriginal_title().matches("^(.*?(?i)(\\b"+newText+"\\w{0})[^$]*)$")){
                                searchList.add(masterList.get(i));
                            }
                        }
                    }else{

                        String[] temp = newText.split(" ");

                        String newExp = "";

                        for (int i = 0; i < temp.length; i++) {
                            if (i == 0){
                                newExp = "(?=.*\\b"+temp[i]+")";
                            }else{
                                newExp = newExp +"(?=.*\\b"+temp[i]+")";
                            }

                        }
                        for (int i = 0; i < masterList.size(); i++) {
                            if (masterList.get(i).getOriginal_title().matches("^(.*?(?i)("+newExp+")[^$]*)$")){
                                searchList.add(masterList.get(i));
                            }
                        }
                    }


                    movieListAdapter.notifyDataSetChanged();

                }else {
                    searchList.clear();
                    searchList.addAll(masterList);
                    movieListAdapter.notifyDataSetChanged();
                }



                return false;
            }
        });

        return true;
    }

}
