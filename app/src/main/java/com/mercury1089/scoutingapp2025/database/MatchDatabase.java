package com.mercury1089.scoutingapp2025.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mercury1089.scoutingapp2025.database.dao.MatchDataAccessObject;
import com.mercury1089.scoutingapp2025.database.model.Match;

@Database(entities = {Match.class}, version = 1)
public abstract class MatchDatabase extends RoomDatabase {
    private static MatchDatabase instance;
    public abstract MatchDataAccessObject matchDao();

    public static MatchDatabase getInstance(Context context) {
        if (instance == null) {
            // lock if null so only one thread creates an instance
            synchronized (MatchDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    MatchDatabase.class, "app-db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
