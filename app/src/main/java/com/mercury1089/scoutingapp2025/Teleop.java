package com.mercury1089.scoutingapp2025;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mercury1089.scoutingapp2025.listeners.NumericalDataInputListener;
import com.mercury1089.scoutingapp2025.listeners.UpdateListener;
import com.mercury1089.scoutingapp2025.utils.GenUtils;

import java.util.LinkedHashMap;

public class Teleop extends Fragment implements UpdateListener {
    //HashMaps for sending QR data between screens
    private LinkedHashMap<String, String> setupHashMap;
    private LinkedHashMap<String, String> teleopHashMap;

    //RadioButtons
    private TextView coralID;
    private TextView reefID;
    private TextView L4ReefID, L3ReefID, L2ReefID, L1ReefID;
    private TextView scoredL4ID, missedL4ID;
    private TextView scoredL3ID, missedL3ID;
    private TextView scoredL2ID, missedL2ID;
    private TextView scoredL1ID, missedL1ID;
    private ImageButton scoredL4Button, notScoredL4Button;
    private ImageButton missedL4Button, notMissedL4Button;
    private ImageButton scoredL3Button, notScoredL3Button;
    private ImageButton missedL3Button, notMissedL3Button;

    private ImageButton scoredL2Button, notScoredL2Button;
    private ImageButton missedL2Button, notMissedL2Button;

    private ImageButton scoredL1Button, notScoredL1Button;
    private ImageButton missedL1Button, notMissedL1Button;
    private TextView scoredL4Counter, missedL4Counter;
    private TextView scoredL3Counter, missedL3Counter;
    private TextView scoredL2Counter, missedL2Counter;
    private TextView scoredL1Counter, missedL1Counter;

    // Algae Scoring - Reef Dealgaefying
    private TextView algaeID;
    private TextView dealgaefyingID;
    private TextView L3AlgaeID, L2AlgaeID;
    private TextView removedL3ID, attemptedL3ID;
    private TextView removedL2ID, attemptedL2ID;
    private ImageButton removedL3Button, notRemovedL3Button;
    private ImageButton attemptedL3Button, notAttemptedL3Button;
    private ImageButton removedL2Button, notRemovedL2Button;
    private ImageButton attemptedL2Button, notAttemptedL2Button;
    private TextView removedL3Counter, attemptedL3Counter;
    private TextView removedL2Counter, attemptedL2Counter;

    // Algae - Processor
    private TextView processorID;
    private TextView scoredProcessorID, missedProcessorID;
    private ImageButton scoredProcessorButton, notScoredProcessorButton;
    private ImageButton missedProcessorButton, notMissedProcessorButton;
    private TextView scoredProcessorCounter, missedProcessorCounter;

    // Algae - Net
    private TextView netID;
    private TextView scoredNetID, missedNetID;
    private ImageButton scoredNetButton, notScoredNetButton;
    private ImageButton missedNetButton, notMissedNetButton;
    private TextView scoredNetCounter, missedNetCounter;

    // Possession
    private TextView possessionCoralID, possessionAlgaeID;
    private TextView pickedUpCoralID;
    private ImageButton pickedUpCoralButton, notPickedUpCoralButton;
    private TextView pickedUpCoralCounter;
    private TextView pickedUpAlgaeID;
    private ImageButton pickedUpAlgaeButton, notPickedUpAlgaeButton;
    private TextView pickedUpAlgaeCounter;

    // Robot toggle options
    private TextView robotID;
    private TextView miscInstructionsID;
    private TextView playedDefenseID, fellOverID;
    private Switch playedDefenseSwitch;
    private Switch fellOverSwitch;

    private Button nextButton;


    private Boolean isQRButton;

    public static Teleop newInstance() {
        Teleop fragment = new Teleop();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private MatchActivity context;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (MatchActivity) getActivity();
        return inflater.inflate(R.layout.fragment_teleop, container, false);
    }

    public void onStart(){
        super.onStart();

        //linking variables to XML elements on the screen
        coralID = getView().findViewById(R.id.IDCoral);
        reefID = getView().findViewById(R.id.IDReef);
        L4ReefID = getView().findViewById(R.id.IDL4Coral);
        L3ReefID = getView().findViewById(R.id.IDL3Coral);
        L2ReefID = getView().findViewById(R.id.IDL2Coral);
        L1ReefID = getView().findViewById(R.id.IDL1Coral);

        scoredL4ID = getView().findViewById(R.id.IDL4Scored);
        scoredL3ID = getView().findViewById(R.id.IDL3Scored);
        scoredL2ID = getView().findViewById(R.id.IDL2Scored);
        scoredL1ID = getView().findViewById(R.id.IDL1Scored);

        missedL4ID = getView().findViewById(R.id.IDL4Missed);
        missedL3ID = getView().findViewById(R.id.IDL3Missed);
        missedL2ID = getView().findViewById(R.id.IDL2Missed);
        missedL1ID = getView().findViewById(R.id.IDL1Missed);

        scoredL4Button = getView().findViewById(R.id.scoredL4Button);
        notScoredL4Button = getView().findViewById(R.id.notScoredL4Button);
        missedL4Button = getView().findViewById(R.id.missedL4Button);
        notMissedL4Button = getView().findViewById(R.id.notMissedL4Button);

        scoredL3Button = getView().findViewById(R.id.scoredL3Button);
        notScoredL3Button = getView().findViewById(R.id.notScoredL3Button);
        missedL3Button = getView().findViewById(R.id.missedL3Button);
        notMissedL3Button = getView().findViewById(R.id.notMissedL3Button);

        scoredL2Button = getView().findViewById(R.id.scoredL2Button);
        notScoredL2Button = getView().findViewById(R.id.notScoredL2Button);
        missedL2Button = getView().findViewById(R.id.missedL2Button);
        notMissedL2Button = getView().findViewById(R.id.notMissedL2Button);

        scoredL1Button = getView().findViewById(R.id.scoredL1Button);
        notScoredL1Button = getView().findViewById(R.id.notScoredL1Button);
        missedL1Button = getView().findViewById(R.id.missedL1Button);
        notMissedL1Button = getView().findViewById(R.id.notMissedL1Button);

        scoredL4Counter = getView().findViewById(R.id.L4ScoredCounter);
        missedL4Counter = getView().findViewById(R.id.L4MissedCounter);
        scoredL3Counter = getView().findViewById(R.id.L3ScoredCounter);
        missedL3Counter = getView().findViewById(R.id.L3MissedCounter);
        scoredL2Counter = getView().findViewById(R.id.L2ScoredCounter);
        missedL2Counter = getView().findViewById(R.id.L2MissedCounter);
        scoredL1Counter = getView().findViewById(R.id.L1ScoredCounter);
        missedL1Counter = getView().findViewById(R.id.L1MissedCounter);

        algaeID = getView().findViewById(R.id.IDAlgae);
        dealgaefyingID = getView().findViewById(R.id.IDDealgaefying);
        L3AlgaeID = getView().findViewById(R.id.IDL3Algae);
        L2AlgaeID = getView().findViewById(R.id.IDL2Algae);

        L3AlgaeID = getView().findViewById(R.id.IDL3Algae);
        L2AlgaeID = getView().findViewById(R.id.IDL2Algae);

        removedL3ID = getView().findViewById(R.id.IDL3Removed);
        attemptedL3ID = getView().findViewById(R.id.IDL3Attempted);
        removedL2ID = getView().findViewById(R.id.IDL2Removed);
        attemptedL2ID = getView().findViewById(R.id.IDL2Attempted);

        removedL3Button = getView().findViewById(R.id.removedL3Button);
        notRemovedL3Button = getView().findViewById(R.id.notRemovedL3Button);
        attemptedL3Button = getView().findViewById(R.id.attemptedL3Button);
        notAttemptedL3Button = getView().findViewById(R.id.notAttemptedL3Button);

        removedL2Button = getView().findViewById(R.id.removedL2Button);
        notRemovedL2Button = getView().findViewById(R.id.notRemovedL2Button);
        attemptedL2Button = getView().findViewById(R.id.attemptedL2Button);
        notAttemptedL2Button = getView().findViewById(R.id.notAttemptedL2Button);

        removedL3Counter = getView().findViewById(R.id.L3RemovedCounter);
        attemptedL3Counter = getView().findViewById(R.id.L3AttemptedCounter);
        removedL2Counter = getView().findViewById(R.id.L2RemovedCounter);
        attemptedL2Counter = getView().findViewById(R.id.L2AttemptedCounter);

        processorID = getView().findViewById(R.id.IDProcessor);
        scoredProcessorID = getView().findViewById(R.id.IDProcessorScored);
        missedProcessorID = getView().findViewById(R.id.IDProcessorMissed);
        scoredProcessorButton = getView().findViewById(R.id.scoredProcessorButton);
        notScoredProcessorButton = getView().findViewById(R.id.notScoredProcessorButton);
        missedProcessorButton = getView().findViewById(R.id.missedProcessorButton);
        notMissedProcessorButton = getView().findViewById(R.id.notMissedProcessorButton);
        scoredProcessorCounter = getView().findViewById(R.id.ProcessorScoredCounter);
        missedProcessorCounter = getView().findViewById(R.id.ProcessorMissedCounter);

        netID = getView().findViewById(R.id.IDNet);
        scoredNetID = getView().findViewById(R.id.IDNetScored);
        missedNetID = getView().findViewById(R.id.IDNetMissed);
        scoredNetButton = getView().findViewById(R.id.scoredNetButton);
        notScoredNetButton = getView().findViewById(R.id.notScoredNetButton);
        missedNetButton = getView().findViewById(R.id.missedNetButton);
        notMissedNetButton = getView().findViewById(R.id.notMissedNetButton);
        scoredNetCounter = getView().findViewById(R.id.netScoredCounter);
        missedNetCounter = getView().findViewById(R.id.netMissedCounter);

        possessionCoralID = getView().findViewById(R.id.IDCoralPossession);
        pickedUpCoralID = getView().findViewById(R.id.IDCoralPossessed);
        pickedUpCoralButton = getView().findViewById(R.id.possessedCoralButton);
        notPickedUpCoralButton = getView().findViewById(R.id.notPossessedCoralButton);
        pickedUpCoralCounter = getView().findViewById(R.id.coralPossessedCounter);

        possessionAlgaeID = getView().findViewById(R.id.IDAlgaePossession);
        pickedUpAlgaeID = getView().findViewById(R.id.IDAlgaePossessed);
        pickedUpAlgaeButton = getView().findViewById(R.id.possessedAlgaeButton);
        notPickedUpAlgaeButton = getView().findViewById(R.id.notPossessedAlgaeButton);
        pickedUpAlgaeCounter = getView().findViewById(R.id.algaePossessedCounter);

        robotID = getView().findViewById(R.id.IDRobot);
        miscInstructionsID = getView().findViewById(R.id.IDMiscDirections);
        playedDefenseID = getView().findViewById(R.id.IDPlayedDefense);
        playedDefenseSwitch = getView().findViewById(R.id.playedDefenseSwitch);
        fellOverID = getView().findViewById(R.id.IDFellOver);
        fellOverSwitch = getView().findViewById(R.id.fellOverSwitch);

        nextButton = getView().findViewById(R.id.NextClimbButton);

        //get HashMap data (fill with defaults if empty or null)
        HashMapManager.checkNullOrEmpty(HashMapManager.HASH.SETUP);
        HashMapManager.checkNullOrEmpty(HashMapManager.HASH.AUTON);
        setupHashMap = HashMapManager.getSetupHashMap();
        teleopHashMap = HashMapManager.getTeleopHashMap();

        //set listeners for buttons
        pickedUpCoralButton.setOnClickListener(new NumericalDataInputListener(pickedUpCoralCounter, teleopHashMap, "CoralPickedUp", true, this));
        notPickedUpCoralButton.setOnClickListener(new NumericalDataInputListener(pickedUpCoralCounter, teleopHashMap, "CoralPickedUp", false, this));

        scoredL4Button.setOnClickListener(new NumericalDataInputListener(scoredL4Counter, teleopHashMap, "ScoredCoralL4", true, this));
        notScoredL4Button.setOnClickListener(new NumericalDataInputListener(scoredL4Counter, teleopHashMap, "ScoredCoralL4", false, this));
        scoredL3Button.setOnClickListener(new NumericalDataInputListener(scoredL3Counter, teleopHashMap, "ScoredCoralL3", true, this));
        notScoredL3Button.setOnClickListener(new NumericalDataInputListener(scoredL3Counter, teleopHashMap, "ScoredCoralL3", false, this));
        scoredL2Button.setOnClickListener(new NumericalDataInputListener(scoredL2Counter, teleopHashMap, "ScoredCoralL2", true, this));
        notScoredL2Button.setOnClickListener(new NumericalDataInputListener(scoredL2Counter, teleopHashMap, "ScoredCoralL2", false, this));
        scoredL1Button.setOnClickListener(new NumericalDataInputListener(scoredL1Counter, teleopHashMap, "ScoredCoralL1", true, this));
        notScoredL1Button.setOnClickListener(new NumericalDataInputListener(scoredL1Counter, teleopHashMap, "ScoredCoralL1", false, this));

        missedL4Button.setOnClickListener(new NumericalDataInputListener(missedL4Counter, teleopHashMap, "MissedCoralL4", true, this));
        notMissedL4Button.setOnClickListener(new NumericalDataInputListener(missedL4Counter, teleopHashMap, "MissedCoralL4", false, this));
        missedL3Button.setOnClickListener(new NumericalDataInputListener(missedL3Counter, teleopHashMap, "MissedCoralL3", true, this));
        notMissedL3Button.setOnClickListener(new NumericalDataInputListener(missedL3Counter, teleopHashMap, "MissedCoralL3", false, this));
        missedL2Button.setOnClickListener(new NumericalDataInputListener(missedL2Counter, teleopHashMap, "MissedCoralL2", true, this));
        notMissedL2Button.setOnClickListener(new NumericalDataInputListener(missedL2Counter, teleopHashMap, "MissedCoralL2", false, this));
        missedL1Button.setOnClickListener(new NumericalDataInputListener(missedL1Counter, teleopHashMap, "MissedCoralL1", true, this));
        notMissedL1Button.setOnClickListener(new NumericalDataInputListener(missedL1Counter, teleopHashMap, "MissedCoralL1", false, this));

        removedL3Button.setOnClickListener(new NumericalDataInputListener(removedL3Counter, teleopHashMap, "RemovedAlgaeL3", true, this));
        notRemovedL3Button.setOnClickListener(new NumericalDataInputListener(removedL3Counter, teleopHashMap, "RemovedAlgaeL3", false, this));
        removedL2Button.setOnClickListener(new NumericalDataInputListener(removedL2Counter, teleopHashMap, "RemovedAlgaeL2", true, this));
        notRemovedL2Button.setOnClickListener(new NumericalDataInputListener(removedL2Counter, teleopHashMap, "RemovedAlgaeL2", false, this));

        attemptedL3Button.setOnClickListener(new NumericalDataInputListener(attemptedL3Counter, teleopHashMap, "AttemptedAlgaeL3", true, this));
        notAttemptedL3Button.setOnClickListener(new NumericalDataInputListener(attemptedL3Counter, teleopHashMap, "AttemptedAlgaeL3", false, this));
        attemptedL2Button.setOnClickListener(new NumericalDataInputListener(attemptedL2Counter, teleopHashMap, "AttemptedAlgaeL2", true, this));
        notAttemptedL2Button.setOnClickListener(new NumericalDataInputListener(attemptedL2Counter, teleopHashMap, "AttemptedAlgaeL2", false, this));

        scoredProcessorButton.setOnClickListener(new NumericalDataInputListener(scoredProcessorCounter, teleopHashMap, "ScoredAlgaeProcessor", true, this));
        notScoredProcessorButton.setOnClickListener(new NumericalDataInputListener(scoredProcessorCounter, teleopHashMap, "ScoredAlgaeProcessor", false, this));
        missedProcessorButton.setOnClickListener(new NumericalDataInputListener(missedProcessorCounter, teleopHashMap, "MissedAlgaeProcessor", true, this));
        notMissedProcessorButton.setOnClickListener(new NumericalDataInputListener(missedProcessorCounter, teleopHashMap, "MissedAlgaeProcessor", false, this));

        scoredNetButton.setOnClickListener(new NumericalDataInputListener(scoredNetCounter, teleopHashMap, "ScoredAlgaeNet", true, this));
        notScoredNetButton.setOnClickListener(new NumericalDataInputListener(scoredNetCounter, teleopHashMap, "ScoredAlgaeNet", false, this));
        missedNetButton.setOnClickListener(new NumericalDataInputListener(missedNetCounter, teleopHashMap, "MissedAlgaeNet", true, this));
        notMissedNetButton.setOnClickListener(new NumericalDataInputListener(missedNetCounter, teleopHashMap, "MissedAlgaeNet", false, this));

        fellOverSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setupHashMap.put("FellOver", isChecked ? "1" : "0");
                updateXMLObjects();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.tabs.getTabAt(2).select();
            }
        });
    }

    private void possessionButtonsEnabledState(boolean enable){
        possessionCoralID.setEnabled(enable);
        pickedUpCoralID.setEnabled(enable);
        pickedUpCoralButton.setEnabled(enable);
        notPickedUpAlgaeButton.setEnabled(enable);
        pickedUpCoralCounter.setEnabled(enable);

        possessionAlgaeID.setEnabled(enable);
        pickedUpAlgaeID.setEnabled(enable);
        pickedUpAlgaeButton.setEnabled(enable);
        notPickedUpAlgaeButton.setEnabled(enable);
        pickedUpAlgaeCounter.setEnabled(enable);
    }

    private void scoringButtonsEnabledState(boolean enable){
        coralID.setEnabled(enable);
        reefID.setEnabled(enable);
        L4ReefID.setEnabled(enable);
        L3ReefID.setEnabled(enable);
        L2ReefID.setEnabled(enable);
        L1ReefID.setEnabled(enable);

        scoredL4ID.setEnabled(enable);
        scoredL4Button.setEnabled(enable);
        notScoredL4Button.setEnabled(enable);
        scoredL4Counter.setEnabled(enable);

        missedL4ID.setEnabled(enable);
        missedL4Button.setEnabled(enable);
        notMissedL4Button.setEnabled(enable);
        missedL4Counter.setEnabled(enable);

        scoredL3ID.setEnabled(enable);
        scoredL3Button.setEnabled(enable);
        notScoredL3Button.setEnabled(enable);
        scoredL3Counter.setEnabled(enable);

        missedL3ID.setEnabled(enable);
        missedL3Button.setEnabled(enable);
        notMissedL3Button.setEnabled(enable);
        missedL3Counter.setEnabled(enable);

        scoredL2ID.setEnabled(enable);
        scoredL2Button.setEnabled(enable);
        notScoredL2Button.setEnabled(enable);
        scoredL2Counter.setEnabled(enable);

        missedL2ID.setEnabled(enable);
        missedL2Button.setEnabled(enable);
        notMissedL2Button.setEnabled(enable);
        missedL2Counter.setEnabled(enable);

        scoredL1ID.setEnabled(enable);
        scoredL1Button.setEnabled(enable);
        notScoredL1Button.setEnabled(enable);
        scoredL1Counter.setEnabled(enable);

        missedL1ID.setEnabled(enable);
        missedL1Button.setEnabled(enable);
        notMissedL1Button.setEnabled(enable);
        missedL1Counter.setEnabled(enable);

        algaeID.setEnabled(enable);
        dealgaefyingID.setEnabled(enable);
        L3AlgaeID.setEnabled(enable);
        L2AlgaeID.setEnabled(enable);

        removedL3ID.setEnabled(enable);
        removedL3Button.setEnabled(enable);
        notRemovedL3Button.setEnabled(enable);
        removedL3Counter.setEnabled(enable);

        removedL2ID.setEnabled(enable);
        removedL2Button.setEnabled(enable);
        notRemovedL2Button.setEnabled(enable);
        removedL2Counter.setEnabled(enable);

        attemptedL3ID.setEnabled(enable);
        attemptedL3Button.setEnabled(enable);
        notAttemptedL3Button.setEnabled(enable);
        attemptedL3Counter.setEnabled(enable);

        attemptedL2ID.setEnabled(enable);
        attemptedL2Button.setEnabled(enable);
        notAttemptedL2Button.setEnabled(enable);
        attemptedL2Counter.setEnabled(enable);

        processorID.setEnabled(enable);
        scoredProcessorID.setEnabled(enable);
        scoredProcessorButton.setEnabled(enable);
        notScoredProcessorButton.setEnabled(enable);
        scoredProcessorCounter.setEnabled(enable);
        missedProcessorID.setEnabled(enable);
        missedProcessorButton.setEnabled(enable);
        notMissedProcessorButton.setEnabled(enable);
        missedProcessorCounter.setEnabled(enable);

        netID.setEnabled(enable);
        scoredNetID.setEnabled(enable);
        scoredNetButton.setEnabled(enable);
        notScoredNetButton.setEnabled(enable);
        scoredNetCounter.setEnabled(enable);
        missedNetID.setEnabled(enable);
        missedNetButton.setEnabled(enable);
    }

    private void miscButtonsEnabledState(boolean enable){
        robotID.setEnabled(enable);
        miscInstructionsID.setEnabled(enable);
        playedDefenseID.setEnabled(enable);
        playedDefenseSwitch.setEnabled(enable);
    }
    private void allButtonsEnabledState(boolean enable){
        possessionButtonsEnabledState(enable);
        scoringButtonsEnabledState(enable);
        miscButtonsEnabledState(enable);
    }

    private void updateXMLObjects(){
        scoredL4Counter.setText(GenUtils.padLeftZeros(teleopHashMap.get("ScoredCoralL4"), 3));
        scoredL3Counter.setText(GenUtils.padLeftZeros(teleopHashMap.get("ScoredCoralL3"), 3));
        scoredL2Counter.setText(GenUtils.padLeftZeros(teleopHashMap.get("ScoredCoralL2"), 3));
        scoredL1Counter.setText(GenUtils.padLeftZeros(teleopHashMap.get("ScoredCoralL1"), 3));

        missedL4Counter.setText(GenUtils.padLeftZeros(teleopHashMap.get("MissedCoralL4"), 3));
        missedL3Counter.setText(GenUtils.padLeftZeros(teleopHashMap.get("MissedCoralL3"), 3));
        missedL2Counter.setText(GenUtils.padLeftZeros(teleopHashMap.get("MissedCoralL2"), 3));
        missedL1Counter.setText(GenUtils.padLeftZeros(teleopHashMap.get("MissedCoralL1"), 3));

        removedL3Counter.setText(GenUtils.padLeftZeros(teleopHashMap.get("RemovedAlgaeL3"), 3));
        removedL2Counter.setText(GenUtils.padLeftZeros(teleopHashMap.get("RemovedAlgaeL2"), 3));
        attemptedL3Counter.setText(GenUtils.padLeftZeros(teleopHashMap.get("AttemptedAlgaeL3"), 3));
        attemptedL2Counter.setText(GenUtils.padLeftZeros(teleopHashMap.get("AttemptedAlgaeL2"), 3));

        scoredProcessorCounter.setText(GenUtils.padLeftZeros(teleopHashMap.get("ScoredAlgaeProcessor"), 3));
        missedProcessorCounter.setText(GenUtils.padLeftZeros(teleopHashMap.get("MissedAlgaeProcessor"), 3));
        scoredNetCounter.setText(GenUtils.padLeftZeros(teleopHashMap.get("ScoredAlgaeNet"), 3));
        missedNetCounter.setText(GenUtils.padLeftZeros(teleopHashMap.get("MissedAlgaeNet"), 3));

        if(setupHashMap.get("FellOver").equals("1")) {
            fellOverSwitch.setChecked(true);
            nextButton.setPadding(150, 0, 185, 0);
            nextButton.setText(R.string.GenerateQRCode);
            isQRButton = true;
            allButtonsEnabledState(false);
        } else {
            fellOverSwitch.setChecked(false);
            nextButton.setPadding(150, 0, 150, 0);
            nextButton.setText(R.string.ClimbNext);
            isQRButton = false;
            allButtonsEnabledState(true);
            // Disables decrement buttons if counter is at 0
            // There's totally a better way to do this without the redundancy
            notPickedUpCoralButton.setEnabled(Integer.parseInt(pickedUpCoralCounter.getText().toString()) > 0);
            notPickedUpAlgaeButton.setEnabled(Integer.parseInt(pickedUpAlgaeCounter.getText().toString()) > 0);
            notScoredL4Button.setEnabled(Integer.parseInt(scoredL4Counter.getText().toString()) > 0);
            notScoredL3Button.setEnabled(Integer.parseInt(scoredL3Counter.getText().toString()) > 0);
            notScoredL2Button.setEnabled(Integer.parseInt(scoredL2Counter.getText().toString()) > 0);
            notScoredL1Button.setEnabled(Integer.parseInt(scoredL1Counter.getText().toString()) > 0);

            notMissedL4Button.setEnabled(Integer.parseInt(missedL4Counter.getText().toString()) > 0);
            notMissedL3Button.setEnabled(Integer.parseInt(missedL3Counter.getText().toString()) > 0);
            notMissedL2Button.setEnabled(Integer.parseInt(missedL2Counter.getText().toString()) > 0);
            notMissedL1Button.setEnabled(Integer.parseInt(missedL1Counter.getText().toString()) > 0);

            notRemovedL3Button.setEnabled(Integer.parseInt(removedL3Counter.getText().toString()) > 0);
            notRemovedL2Button.setEnabled(Integer.parseInt(removedL2Counter.getText().toString()) > 0);
            notAttemptedL3Button.setEnabled(Integer.parseInt(attemptedL3Counter.getText().toString()) > 0);
            notAttemptedL2Button.setEnabled(Integer.parseInt(attemptedL2Counter.getText().toString()) > 0);

            notScoredProcessorButton.setEnabled(Integer.parseInt(scoredProcessorCounter.getText().toString()) > 0);
            notMissedProcessorButton.setEnabled(Integer.parseInt(missedProcessorCounter.getText().toString()) > 0);

            notScoredNetButton.setEnabled(Integer.parseInt(scoredNetCounter.getText().toString()) > 0);
            notMissedNetButton.setEnabled(Integer.parseInt(missedNetCounter.getText().toString()) > 0);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming visible, then...
            if (isVisibleToUser) {
                setupHashMap = HashMapManager.getSetupHashMap();
                teleopHashMap = HashMapManager.getTeleopHashMap();
                updateXMLObjects();
                //set all objects in the fragment to their values from the HashMaps
            } else {
                HashMapManager.putSetupHashMap(setupHashMap);
                HashMapManager.putTeleopHashMap(teleopHashMap);
            }
        }
    }

    @Override
    public void onUpdate() {
        updateXMLObjects();
    }
}
