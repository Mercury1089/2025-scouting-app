package com.mercury1089.scoutingapp2025.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.mercury1089.scoutingapp2025.database.model.Match;

import java.util.List;

@Dao
public interface MatchDataAccessObject {
    @Query("SELECT * FROM matches")
    List<Match> fetchAll();
    @Query("SELECT * FROM matches WHERE matchKey IS (:key)")
    List<Match> fetchByMatchKey(String key);
    @Query("SELECT * FROM matches WHERE matchKey IN (:keys)")
    List<Match> fetchAllByMatchKey(String[] keys);
}
