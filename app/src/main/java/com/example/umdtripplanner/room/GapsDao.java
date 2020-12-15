package com.example.umdtripplanner.room;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GapsDao {
    @Query("SELECT * FROM gaps")
    List<Gap> getAll();
}
