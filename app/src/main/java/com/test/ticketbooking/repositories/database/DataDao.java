package com.test.ticketbooking.repositories.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.test.ticketbooking.models.MasterData;

import java.util.List;

@Dao
public interface DataDao {
 
    @Query("SELECT * FROM masterdata")
    List<MasterData> getAllData();
 
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<MasterData> data);

    @Query("SELECT * FROM masterdata WHERE genre_ids Like :imgID ")
    List<MasterData> getSingleData(String imgID);



    
}