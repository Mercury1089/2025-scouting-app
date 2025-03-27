package com.mercury1089.scoutingapp2025.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mercury1089.scoutingapp2025.api.model.ApiMatch;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Entity(tableName = "matches")
public class Match {
    @PrimaryKey
    @NotNull
    public String matchKey;
    // Info in Room Entities are required to be simple data types
    // So taems are stored in a comma-separated string and split when accessed
    @ColumnInfo(name = "red_alliance")
    public String redAllianceTeams;
    @ColumnInfo(name = "blue_alliance")
    public String blueAllianceTeams;

    public String getMatchKey() {
        return matchKey;
    }

    public List<String> getRedAllianceTeams() {
        return Arrays.asList(redAllianceTeams.split(","));
    }

    public List<String> getBlueAllianceTeams() {
        return Arrays.asList(blueAllianceTeams.split(","));
    }

    @NonNull
    @Override
    public String toString() {
        return matchKey + ": Red(" + redAllianceTeams + ") | Blue(" + blueAllianceTeams + ")";
    }
    // Builder for Match because creating an explicit constructor for Match (with args)
    // conflicts with auto generated Impls for Match created by Room
    public static class Builder {
        private String matchKey;
        private String redAllianceTeams;
        private String blueAllianceTeams;

        public Builder setMatchKey(String matchKey) {
            this.matchKey = matchKey;
            return this;
        }

        public Builder setRedAllianceTeams(String redAllianceTeams) {
            this.redAllianceTeams = redAllianceTeams;
            return this;
        }

        public Builder setBlueAllianceTeams(String blueAllianceTeams) {
            this.blueAllianceTeams = blueAllianceTeams;
            return this;
        }

        public Match build() {
            Match match = new Match();
            match.matchKey = this.matchKey;
            match.redAllianceTeams = this.redAllianceTeams;
            match.blueAllianceTeams = this.blueAllianceTeams;
            return match;
        }
    }
}
