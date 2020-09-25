package com.test.ticketbooking.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.test.ticketbooking.models.MasterData;
import com.test.ticketbooking.repositories.data.DataRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<MasterData>> mMasterData;

    private DataRepository mRepo;

    public void init(Context applicationContext){
        if(mMasterData != null){
            return;
        }
        mRepo = DataRepository.getInstance(applicationContext);
        mMasterData = new MutableLiveData<>();
    }




    public LiveData<List<MasterData>> getMasterData(int pageNumber){

        mMasterData = mRepo.getMasterDatas(pageNumber);

        return mMasterData;
    }


    public LiveData<List<MasterData>> getSimilarData(String pageNumber){

        mMasterData = mRepo.getSimilar(pageNumber);

        return mMasterData;
    }

}