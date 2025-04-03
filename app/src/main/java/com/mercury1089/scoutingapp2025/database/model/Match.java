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
    @ColumnInfo(name = "comp_level")
    public String compLevel;

    @ColumnInfo(name = "match_number")
    public int matchNumber;
    // Info in Room Entities are required to be simple data types
    // So taems are stored in a comma-separated string and split when accessed
    @ColumnInfo(name = "red_alliance")
    public String redAllianceTeams;
    @ColumnInfo(name = "blue_alliance")
    public String blueAllianceTeams;

    /*
    TBA match key with the format yyyy[EVENT_CODE]_[COMP_LEVEL]m[MATCH_NUMBER],
    where yyyy is the year, and EVENT_CODE is the event code of the event,
    COMP_LEVEL is (qm, ef, qf, sf, f), and MATCH_NUMBER is the match number in the competition level.
    A set number may append the competition level if more than one match in required per set.
     */
    public String getMatchKey() {
        return matchKey;
    }
    public String getCompLevel() {
        return compLevel;
    }
    public int getMatchNumber() {
        return matchNumber;
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
        private String compLevel;
        private int matchNumber;

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

        public Builder setCompLevel(String compLevel) {
            this.compLevel = compLevel;
            return this;
        }

        public Builder setMatchNumber(int matchNumber) {
            this.matchNumber = matchNumber;
            return this;
        }

        public Match build() {
            Match match = new Match();
            match.matchKey = this.matchKey;
            match.compLevel = this.compLevel;
            match.matchNumber = this.matchNumber;
            match.redAllianceTeams = this.redAllianceTeams;
            match.blueAllianceTeams = this.blueAllianceTeams;
            return match;
        }
    }
}
