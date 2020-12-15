package com.example.umdtripplanner.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gaps")
@SuppressWarnings("all")
public class Gap {
    @PrimaryKey
    public Integer id;

    public int routeTag, gapTime, numLogs;

    @NonNull
    public String srcDirTag;
    @NonNull
    public String srcStopTag;
    @NonNull
    public String dstDirTag;
    @NonNull
    public String dstStopTag;
}
