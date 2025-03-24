package com.mercury1089.scoutingapp2025.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "matches")
public class Match {
    @PrimaryKey
    public String matchKey;
    @ColumnInfo(name = "red_alliance")
    public String redAllianceTeams;
    @ColumnInfo(name = "blue_alliance")
    public String blueAllianceTeams;

    public String getMatchKey() {
        return matchKey;
    }

    public String getRedAllianceTeams() {
        return redAllianceTeams;
    }

    public String getBlueAllianceTeams() {
        return blueAllianceTeams;
    }

}
