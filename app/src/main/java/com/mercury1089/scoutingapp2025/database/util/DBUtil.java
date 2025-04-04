package com.mercury1089.scoutingapp2025.database.util;

public class DBUtil {

    public static final String LAST_API_FETCH_KEY = "last_api_fetch"; // For metadata table
    public enum MatchType {
        // Technically the prefix for qualification matches is "qm" but the "m" overlaps
        // with the "m" before the match number in TBA data so I'm choosing to omit it.
        QUALIFICATION("q"),
        QUARTERFINAL("qf"),
        SEMIFINAL("sf"),
        FINAL("f");
        private final String prefix;
        MatchType(String prefix) {
            this.prefix = prefix;
        }
        public String getPrefix() {
            return prefix;
        }
    }

    public static String createQualificationMatchKey(String event_key, int matchNumber) {
        return event_key + "_" + MatchType.QUALIFICATION.getPrefix() + "m" + matchNumber;
    }

    public static String createMatchKey(MatchType type, String eventKey, int setNumber, int matchNumber) {
        if (type == MatchType.QUALIFICATION) return createQualificationMatchKey(eventKey, matchNumber);
        return eventKey + "_" + type.getPrefix() + setNumber + "m" + matchNumber;
    }
}
