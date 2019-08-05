package com.example.poc1.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseProvider {

    private static AppDatabase appDatabase;

    public static AppDatabase getDatabaseInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "POC1").build();
        }
        return appDatabase;
    }
}
