package com.mercury1089.scoutingapp2024.utils;

import android.content.Context;

import com.mercury1089.scoutingapp2024.HashMapManager;

import java.util.LinkedHashMap;

public class QRStringBuilder {

    private static StringBuilder QRString = new StringBuilder();

    public static void buildQRString(){
        LinkedHashMap setup = HashMapManager.getSetupHashMap();
        LinkedHashMap auton = HashMapManager.getAutonHashMap();
        LinkedHashMap teleop = HashMapManager.getTeleopHashMap();
        LinkedHashMap climb = HashMapManager.getClimbHashMap();

        // Setup Data
        QRString.append(setup.get("ScouterName")).append(",");
        QRString.append(setup.get("TeamNumber")).append(",");
        QRString.append(setup.get("MatchNumber")).append(",");
        QRString.append(setup.get("AlliancePartner1")).append(",");
        QRString.append(setup.get("AlliancePartner2")).append(",");
        QRString.append(setup.get("AllianceColor")).append(",");
        QRString.append(setup.get("PreloadNote").equals("1") ? "Y" : "N").append(",");
        QRString.append(setup.get("NoShow").equals("1") ? "Y" : "N").append(",");
        QRString.append(setup.get("FellOver").equals("1") ? "Y" : "N").append(",");

        QRString.append(auton.get("Leave").equals("1") ? "Y" : "N").append(",");
        QRString.append(climb.get("Park")).append(",");
        QRString.append(climb.get("Stage")).append(",");


        // Event Data
        // Auton
        QRString.append("Auton").append(",");
        QRString.append(auton.get("NumberPickedUp")).append(",");
        QRString.append(auton.get("ScoredSpeaker")).append(",");
        QRString.append(auton.get("MissedSpeaker")).append(",");
        QRString.append(auton.get("ScoredAmp")).append(",");
        QRString.append(auton.get("MissedAmp")).append(",");
        // Teleop
        QRString.append("Teleop").append(",");
        QRString.append(teleop.get("NumberPickedUp")).append(",");
        QRString.append(teleop.get("ScoredSpeaker")).append(",");
        QRString.append(teleop.get("MissedSpeaker")).append(",");
        QRString.append(teleop.get("ScoredAmp")).append(",");
        QRString.append(teleop.get("MissedAmp")).append(",");
        QRString.append(climb.get("ScoredTrap")).append(",");
        QRString.append(climb.get("MissedTrap")).append(",");
        // Remove last comma
        QRString.deleteCharAt(QRString.length()-1);
    }

    public static String getQRString(){
        return QRString.toString();
    }

    public static void clearQRString(Context context) {
        HashMapManager.appendQRList(QRString.toString(), context);
        QRString = new StringBuilder();
    }
}
