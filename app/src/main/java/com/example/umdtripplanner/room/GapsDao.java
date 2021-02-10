package com.example.umdtripplanner.room;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GapsDao {
    @Query("SELECT gapTime FROM gaps WHERE routeTag=:route AND srcDirTag=:srcDir " +
            "AND srcStopTag=:srcStop AND dstDirTag=:dstDir AND dstStopTag=:dstStop")
    int getDuration(int route, String srcDir, String srcStop, String dstDir, String dstStop);
}
