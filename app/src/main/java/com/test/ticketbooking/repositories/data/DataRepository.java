package com.test.ticketbooking.repositories.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.test.ticketbooking.R;
import com.test.ticketbooking.models.MasterData;
import com.test.ticketbooking.models.Results;
import com.test.ticketbooking.repositories.database.DatabaseClient;
import com.test.ticketbooking.repositories.network.ApiInterface;
import com.test.ticketbooking.repositories.network.ApiManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {

    private static DataRepository instance;
    private List<MasterData> dataSet = new ArrayList<>();
    private static Context context;

    private ApiInterface api;

    public static DataRepository getInstance(Context applicationContext){
        context = applicationContext;
        if(instance == null){
            instance = new DataRepository();
        }
        return instance;
    }

    public  DataRepository(){
        api = ApiManager.getApiInterface();
    }


    public MutableLiveData<List<MasterData>> getMasterDatas(final int pageNumber) {

        final MutableLiveData<List<MasterData>> data = new MutableLiveData<>();


        try {

            api.getData(context.getResources().getString(R.string.api_key),"en-US",String.valueOf(pageNumber))
                    .enqueue(new Callback<Results>() {

                                 @Override
                                 public void onResponse(Call<Results> call, Response<Results> response) {
                                     try {
                                         if (response.isSuccessful()) {

                                             dataSet.clear();

                                             dataSet.addAll(response.body().getResults());
                                             new insertData(response.body().getResults()).execute();
                                             data.setValue(dataSet);
                                         }
                                     } catch (Exception e) {
                                         e.printStackTrace();
                                         data.setValue(null);
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<Results> call, Throwable t) {
                                     t.printStackTrace();
                                     data.setValue(null);
                                 }
                             }
                    );



        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }


    class insertData extends AsyncTask<Void, Void, List<MasterData>> {

        List<MasterData> response;
        public insertData(List<MasterData> response1) {
            response = response1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<MasterData> doInBackground(Void... voids) {

            DatabaseClient.getInstance(context).getAppDatabase()
                    .dataDao()
                    .insert(response);

            return null;
        }

        @Override
        protected void onPostExecute(List<MasterData> aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    public MutableLiveData<List<MasterData>> getSimilar(final String movieID) {

        final MutableLiveData<List<MasterData>> data = new MutableLiveData<>();


        try {

            api.getSimilarData(movieID,context.getResources().getString(R.string.api_key),"en-US","1")
                    .enqueue(new Callback<Results>() {

                                 @Override
                                 public void onResponse(Call<Results> call, Response<Results> response) {
                                     try {
                                         if (response.isSuccessful()) {

                                             dataSet.clear();

                                             dataSet.addAll(response.body().getResults());
                                             new insertData(response.body().getResults()).execute();
                                             data.setValue(dataSet);
                                         }
                                     } catch (Exception e) {
                                         e.printStackTrace();
                                         data.setValue(null);
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<Results> call, Throwable t) {
                                     t.printStackTrace();
                                     data.setValue(null);
                                 }
                             }
                    );



        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}