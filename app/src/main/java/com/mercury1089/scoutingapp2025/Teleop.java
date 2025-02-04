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

import com.mercury1089.scoutingapp2025.utils.GenUtils;

import java.util.LinkedHashMap;

public class Teleop extends Fragment {
    //HashMaps for sending QR data between screens
    private LinkedHashMap<String, String> setupHashMap;
    private LinkedHashMap<String, String> teleopHashMap;

    //RadioButtons
    private ImageButton pickedUpIncrementButton;
    private ImageButton pickedUpDecrementButton;
    private ImageButton scoredSpeakerButton;
    private ImageButton notScoredSpeakerButton;
    private ImageButton scoredAmpButton;
    private ImageButton notScoredAmpButton;
    private ImageButton missedSpeakerButton;
    private ImageButton notMissedSpeakerButton;
    private ImageButton missedAmpButton;
    private ImageButton notMissedAmpButton;
    private Button nextButton;

    //Switches
    private Switch fellOverSwitch;

    //TextViews
    private TextView possessionID;
    private TextView possessionDescription;
    private TextView pickedUpID;
    private TextView pickedUpCounter;
    private TextView IDSpeaker;
    private TextView IDAmp;
    private TextView IDScoredSpeaker;
    private TextView IDScoredAmp;
    private TextView IDMissedSpeaker;
    private TextView IDMissedAmp;

    private TextView scoringID;
    private TextView scoringDescription;
    private TextView scoredSpeakerCounter;
    private TextView scoredAmpCounter;
    private TextView missedSpeakerCounter;
    private TextView missedAmpCounter;

    private TextView miscID;
    private TextView miscDescription;

    private TextView fellOverID;

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
        possessionID = getView().findViewById(R.id.IDPossession);
        possessionDescription = getView().findViewById(R.id.IDPossessionDirections);
        pickedUpID = getView().findViewById(R.id.IDPickedUp);
        pickedUpIncrementButton = getView().findViewById(R.id.PickedUpButton);
        pickedUpDecrementButton = getView().findViewById(R.id.NotPickedUpButton);
        pickedUpCounter = getView().findViewById(R.id.PickedUpCounter);

        scoringID = getView().findViewById(R.id.IDScoring);
        scoringDescription = getView().findViewById(R.id.IDScoringDirections);
        scoredSpeakerButton = getView().findViewById(R.id.scoredSpeakerButton);
        notScoredSpeakerButton = getView().findViewById(R.id.notScoredSpeakerButton);
        scoredAmpButton = getView().findViewById(R.id.scoredAmpButton);
        notScoredAmpButton = getView().findViewById(R.id.notScoredAmpButton);
        missedSpeakerButton = getView().findViewById(R.id.missedSpeakerButton);
        notMissedSpeakerButton = getView().findViewById(R.id.notMissedSpeakerButton);
        missedAmpButton = getView().findViewById(R.id.missedAmpButton);
        notMissedAmpButton = getView().findViewById(R.id.notMissedAmpButton);
        scoredSpeakerCounter = getView().findViewById(R.id.scoredSpeakerCounter);
        scoredAmpCounter = getView().findViewById(R.id.scoredAmpCounter);
        missedSpeakerCounter = getView().findViewById(R.id.missedSpeakerCounter);
        missedAmpCounter = getView().findViewById(R.id.missedAmpCounter);
        IDSpeaker = getView().findViewById(R.id.IDSpeaker);
        IDAmp = getView().findViewById(R.id.IDAmp);
        IDScoredSpeaker = getView().findViewById(R.id.IDScoredSpeaker);
        IDScoredAmp = getView().findViewById(R.id.IDScoredAmp);
        IDMissedSpeaker = getView().findViewById(R.id.IDMissedSpeaker);
        IDMissedAmp = getView().findViewById(R.id.IDMissedAmp);

        miscID = getView().findViewById(R.id.IDMisc);
        miscDescription = getView().findViewById(R.id.IDMiscDirections);
        fellOverSwitch = getView().findViewById(R.id.FellOverSwitch);
        fellOverID = getView().findViewById(R.id.IDFellOver);

        nextButton = getView().findViewById(R.id.NextClimbButton);

        //get HashMap data (fill with defaults if empty or null)
        HashMapManager.checkNullOrEmpty(HashMapManager.HASH.SETUP);
        HashMapManager.checkNullOrEmpty(HashMapManager.HASH.AUTON);
        setupHashMap = HashMapManager.getSetupHashMap();
        teleopHashMap = HashMapManager.getTeleopHashMap();

        //set listeners for buttons
        pickedUpIncrementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                int currentCount = Integer.parseInt((String)pickedUpCounter.getText());
                currentCount++;
                teleopHashMap.put("NumberPickedUp", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

        pickedUpDecrementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                int currentCount = Integer.parseInt((String)pickedUpCounter.getText());
                if(currentCount > 0)
                    currentCount--;
                teleopHashMap.put("NumberPickedUp", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

        scoredSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt((String) scoredSpeakerCounter.getText());
                currentCount++;
                teleopHashMap.put("ScoredSpeaker", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

        notScoredSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt((String) scoredSpeakerCounter.getText());
                if (currentCount > 0)
                    currentCount--;
                teleopHashMap.put("ScoredSpeaker", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

        missedSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt((String) missedSpeakerCounter.getText());
                currentCount++;
                teleopHashMap.put("MissedSpeaker", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

        notMissedSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt((String) missedSpeakerCounter.getText());
                if (currentCount > 0)
                    currentCount--;
                teleopHashMap.put("MissedSpeaker", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

        scoredAmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt((String) scoredAmpCounter.getText());
                currentCount++;
                teleopHashMap.put("ScoredAmp", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

        notScoredAmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt((String) scoredAmpCounter.getText());
                if (currentCount > 0)
                    currentCount--;
                teleopHashMap.put("ScoredAmp", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

        missedAmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt((String) missedAmpCounter.getText());
                currentCount++;
                teleopHashMap.put("MissedAmp", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

        notMissedAmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt((String) missedAmpCounter.getText());
                if (currentCount > 0)
                    currentCount--;
                teleopHashMap.put("MissedAmp", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

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
        possessionID.setEnabled(enable);
        possessionDescription.setEnabled(enable);

        pickedUpID.setEnabled(enable);
        pickedUpIncrementButton.setEnabled(enable);
        pickedUpDecrementButton.setEnabled(enable);
        pickedUpCounter.setEnabled(enable);
    }

    private void scoringButtonsEnabledState(boolean enable){
        scoringID.setEnabled(enable);
        scoringDescription.setEnabled(enable);
        IDSpeaker.setEnabled(enable);
        IDAmp.setEnabled(enable);
        IDScoredSpeaker.setEnabled(enable);
        IDScoredAmp.setEnabled(enable);
        IDMissedSpeaker.setEnabled(enable);
        IDMissedAmp.setEnabled(enable);

        scoredSpeakerButton.setEnabled(enable);
        notScoredSpeakerButton.setEnabled(enable);
        scoredAmpButton.setEnabled(enable);
        notScoredAmpButton.setEnabled(enable);
        scoredSpeakerCounter.setEnabled(enable);
        scoredAmpCounter.setEnabled(enable);
        missedSpeakerCounter.setEnabled(enable);
        missedAmpCounter.setEnabled(enable);

        missedSpeakerButton.setEnabled(enable);
        notMissedSpeakerButton.setEnabled(enable);
        missedAmpButton.setEnabled(enable);
        notMissedAmpButton.setEnabled(enable);
    }


    private void allButtonsEnabledState(boolean enable){
        possessionButtonsEnabledState(enable);
        scoringButtonsEnabledState(enable);

        miscID.setEnabled(enable);
        miscDescription.setEnabled(enable);


    }

    private void updateXMLObjects(){
        scoredSpeakerCounter.setText(GenUtils.padLeftZeros(teleopHashMap.get("ScoredSpeaker"), 3));
        missedSpeakerCounter.setText(GenUtils.padLeftZeros(teleopHashMap.get("MissedSpeaker"), 3));
        scoredAmpCounter.setText(GenUtils.padLeftZeros(teleopHashMap.get("ScoredAmp"), 3));
        missedAmpCounter.setText(GenUtils.padLeftZeros(teleopHashMap.get("MissedAmp"), 3));
        pickedUpCounter.setText(GenUtils.padLeftZeros(teleopHashMap.get("NumberPickedUp"), 3));

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

            if(Integer.parseInt((String)pickedUpCounter.getText()) == 0)
                pickedUpDecrementButton.setEnabled(false);
            else
                pickedUpDecrementButton.setEnabled(true);
            if (Integer.parseInt((String) scoredSpeakerCounter.getText()) <= 0)
                notScoredSpeakerButton.setEnabled(false);
            else
                notScoredSpeakerButton.setEnabled(true);
            if (Integer.parseInt((String) scoredAmpCounter.getText()) <= 0)
                notScoredAmpButton.setEnabled(false);
            else
                notScoredAmpButton.setEnabled(true);
            if (Integer.parseInt((String) missedSpeakerCounter.getText()) <= 0)
                notMissedSpeakerButton.setEnabled(false);
            else
                notMissedSpeakerButton.setEnabled(true);
            if (Integer.parseInt((String) missedAmpCounter.getText()) <= 0)
                notMissedAmpButton.setEnabled(false);
            else
                notMissedAmpButton.setEnabled(true);
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
}
