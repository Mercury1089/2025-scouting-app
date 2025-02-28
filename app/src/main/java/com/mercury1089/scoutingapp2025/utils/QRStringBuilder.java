package com.mercury1089.scoutingapp2025.utils;

import android.content.Context;

import com.mercury1089.scoutingapp2025.HashMapManager;

import java.util.LinkedHashMap;

public class QRStringBuilder {

    private static StringBuilder QRString = new StringBuilder();

    public static void buildQRString(){
        LinkedHashMap<String, String> setup = HashMapManager.getSetupHashMap();
        LinkedHashMap<String, String> auton = HashMapManager.getAutonHashMap();
        LinkedHashMap<String, String> teleop = HashMapManager.getTeleopHashMap();
        LinkedHashMap<String, String> climb = HashMapManager.getClimbHashMap();

        // Setup Data
        QRString.append(setup.get("TeamNumber")).append(",");
        QRString.append(setup.get("MatchNumber")).append(",");
        QRString.append(setup.get("AlliancePartner1")).append(",");
        QRString.append(setup.get("AlliancePartner2")).append(",");
        QRString.append(setup.get("AllianceColor")).append(",");
        QRString.append(setup.get("PreloadNote")).append(",");
        QRString.append(setup.get("NoShow")).append(",");
        QRString.append(setup.get("FellOver")).append(",");

        QRString.append(auton.get("Leave")).append(",");
        QRString.append(setup.get("PlayedDefense")).append(",");
        QRString.append(climb.get("Park")).append(",");
        QRString.append(climb.get("Barge")).append(",");


        // Event Data
        // Auton
        QRString.append("Auton").append(",");
        QRString.append("Coral").append(",");
        QRString.append(auton.get("L4ScoredCoral")).append(",");
        QRString.append(auton.get("L3ScoredCoral")).append(",");
        QRString.append(auton.get("L2ScoredCoral")).append(",");
        QRString.append(auton.get("L1ScoredCoral")).append(",");
        QRString.append(auton.get("L4MissedCoral")).append(",");
        QRString.append(auton.get("L3MissedCoral")).append(",");
        QRString.append(auton.get("L2MissedCoral")).append(",");
        QRString.append(auton.get("L1MissedCoral")).append(",");
        QRString.append(auton.get("CoralPickedUp")).append(",");

        QRString.append("Algae").append(",");
        QRString.append(auton.get("RemovedAlgaeL3")).append(",");
        QRString.append(auton.get("RemovedAlgaeL2")).append(",");
        QRString.append(auton.get("AttemptedAlgaeL3")).append(",");
        QRString.append(auton.get("AttemptedAlgaeL2")).append(",");
        QRString.append(auton.get("ScoredAlgaeProcessor")).append(",");
        QRString.append(auton.get("ScoredAlgaeNet")).append(",");
        QRString.append(auton.get("MissedAlgaeNet")).append(",");
        QRString.append(auton.get("AlgaePickedUp")).append(",");

        // Teleop
        QRString.append("Teleop").append(",");
        QRString.append("Coral").append(",");
        QRString.append(teleop.get("L4ScoredCoral")).append(",");
        QRString.append(teleop.get("L3ScoredCoral")).append(",");
        QRString.append(teleop.get("L2ScoredCoral")).append(",");
        QRString.append(teleop.get("L1ScoredCoral")).append(",");
        QRString.append(teleop.get("L4MissedCoral")).append(",");
        QRString.append(teleop.get("L3MissedCoral")).append(",");
        QRString.append(teleop.get("L2MissedCoral")).append(",");
        QRString.append(teleop.get("L1MissedCoral")).append(",");
        QRString.append(teleop.get("CoralPickedUp")).append(",");

        QRString.append("Algae").append(",");
        QRString.append(teleop.get("RemovedAlgaeL3")).append(",");
        QRString.append(teleop.get("RemovedAlgaeL2")).append(",");
        QRString.append(teleop.get("AttemptedAlgaeL3")).append(",");
        QRString.append(teleop.get("AttemptedAlgaeL2")).append(",");
        QRString.append(teleop.get("ScoredAlgaeProcessor")).append(",");
        QRString.append(teleop.get("ScoredAlgaeNet")).append(",");
        QRString.append(teleop.get("MissedAlgaeNet")).append(",");
        QRString.append(teleop.get("AlgaePickedUp")).append(",");
        QRString.append(setup.get("ScouterName"));
    }

    public static String getQRString(){
        return QRString.toString();
    }

    public static void clearQRString(Context context) {
        HashMapManager.appendQRList(QRString.toString(), context);
        QRString = new StringBuilder();
    }
}
