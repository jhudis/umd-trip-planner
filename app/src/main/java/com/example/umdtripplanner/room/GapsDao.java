package com.example.umdtripplanner.room;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GapsDao {
    @Query("SELECT gapTime FROM gaps WHERE routeTag=:route AND srcStopTag=:src AND dstStopTag=:dst")
    int getDuration(int route, String src, String dst);
}
