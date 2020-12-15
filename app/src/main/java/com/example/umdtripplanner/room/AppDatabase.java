package com.example.umdtripplanner.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Gap.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GapsDao gapsDao();
}