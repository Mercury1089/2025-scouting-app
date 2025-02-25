package com.mercury1089.scoutingapp2025;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Vibrator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import java.util.LinkedHashMap;
import androidx.fragment.app.Fragment;

import com.mercury1089.scoutingapp2025.databinding.FragmentAutonBinding;
import com.mercury1089.scoutingapp2025.listeners.NumericalDataInputListener;
import com.mercury1089.scoutingapp2025.listeners.UpdateListener;
import com.mercury1089.scoutingapp2025.utils.GenUtils;

public class Auton extends Fragment implements UpdateListener {
    //HashMaps for sending QR data between screens
    private LinkedHashMap<String, String> setupHashMap;
    private LinkedHashMap<String, String> autonHashMap;

    FragmentAutonBinding binding;

    // Instructions
    private TextView scoringDirectionsID;

    // Coral Scoring - Reef
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
    private TextView miscInstructionsID;
    private TextView leaveID, fellOverID;
    private Switch leaveSwitch;
    private Switch fellOverSwitch;

    // Next button
    private Button nextButton;

    // Auton timer views
    private TextView timerID;
    private TextView secondsRemaining;
    private TextView teleopWarning;

    // Auton border image views
    private ImageView topEdgeBar;
    private ImageView bottomEdgeBar;
    private ImageView leftEdgeBar;
    private ImageView rightEdgeBar;

    // other variables
    private static CountDownTimer timer;
    private boolean firstTime = true;
    private boolean running = true;
    private ValueAnimator teleopButtonAnimation;
    private AnimatorSet animatorSet;

    /*
    - Runs when a new instance of this fragment is created (i.e. when it is first loaded in from PregameActivity.java)
     */
    public static Auton newInstance() {
        Auton fragment = new Auton();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    MatchActivity context;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (MatchActivity) getActivity();
        View inflated = null;
        try {
            inflater.inflate(R.layout.fragment_auton, container, false);
        } catch (InflateException e) {
            Log.d("Oncreateview", "ERROR");
            throw e;
        }
        return inflated;
    }

    public void onStart(){
        super.onStart();

        //linking variables to XML elements on the screen
        timerID = getView().findViewById(R.id.IDAutonSeconds1);
        secondsRemaining = getView().findViewById(R.id.AutonSeconds);
        teleopWarning = getView().findViewById(R.id.TeleopWarning);

        scoringDirectionsID = getView().findViewById(R.id.IDPossessionDirections);
        coralID = getView().findViewById(R.id.IDCoral);
        reefID = getView().findViewById(R.id.IDReef);
        L4ReefID = getView().findViewById(R.id.IDL4Coral);
        L3ReefID = getView().findViewById(R.id.IDL3Coral);
        L2ReefID = getView().findViewById(R.id.IDL2Coral);
        L1ReefID = getView().findViewById(R.id.IDL1Coral);

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

        miscInstructionsID = getView().findViewById(R.id.IDMiscDirections);
        leaveID = getView().findViewById(R.id.IDLeave);
        fellOverID = getView().findViewById(R.id.IDFellOver);
        leaveSwitch = getView().findViewById(R.id.LeaveSwitch);
        fellOverSwitch = getView().findViewById(R.id.FellOverSwitch);

        topEdgeBar = getView().findViewById(R.id.topEdgeBar);
        bottomEdgeBar = getView().findViewById(R.id.bottomEdgeBar);
        leftEdgeBar = getView().findViewById(R.id.leftEdgeBar);
        rightEdgeBar = getView().findViewById(R.id.rightEdgeBar);

        nextButton = getView().findViewById(R.id.NextTeleopButton);

        //get HashMap data (fill with defaults if empty or null)
        HashMapManager.checkNullOrEmpty(HashMapManager.HASH.SETUP);
        HashMapManager.checkNullOrEmpty(HashMapManager.HASH.AUTON);
        setupHashMap = HashMapManager.getSetupHashMap();
        autonHashMap = HashMapManager.getAutonHashMap();

        //fill in counters with data
        updateXMLObjects();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        timer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                secondsRemaining.setText(GenUtils.padLeftZeros("" + millisUntilFinished / 1000, 2));

                if(!running)
                    return;

                if (millisUntilFinished / 1000 <= 3 && millisUntilFinished / 1000 > 0) {  //play the blinking animation
                    teleopWarning.setVisibility(View.VISIBLE);
                    timerID.setTextColor(context.getResources().getColor(R.color.banana));
                    timerID.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.timer_yellow, 0, 0, 0);

                    vibrator.vibrate(500);

                    ObjectAnimator topEdgeLighter = ObjectAnimator.ofFloat(topEdgeBar, View.ALPHA, 0.0f, 1.0f);
                    ObjectAnimator bottomEdgeLighter = ObjectAnimator.ofFloat(bottomEdgeBar, View.ALPHA, 0.0f, 1.0f);
                    ObjectAnimator rightEdgeLighter = ObjectAnimator.ofFloat(rightEdgeBar, View.ALPHA, 0.0f, 1.0f);
                    ObjectAnimator leftEdgeLighter = ObjectAnimator.ofFloat(leftEdgeBar, View.ALPHA, 0.0f, 1.0f);

                    topEdgeLighter.setDuration(500);
                    bottomEdgeLighter.setDuration(500);
                    leftEdgeLighter.setDuration(500);
                    rightEdgeLighter.setDuration(500);

                    topEdgeLighter.setRepeatMode(ObjectAnimator.REVERSE);
                    topEdgeLighter.setRepeatCount(1);
                    bottomEdgeLighter.setRepeatMode(ObjectAnimator.REVERSE);
                    bottomEdgeLighter.setRepeatCount(1);
                    leftEdgeLighter.setRepeatMode(ObjectAnimator.REVERSE);
                    leftEdgeLighter.setRepeatCount(1);
                    rightEdgeLighter.setRepeatMode(ObjectAnimator.REVERSE);
                    rightEdgeLighter.setRepeatCount(1);

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(topEdgeLighter, bottomEdgeLighter, leftEdgeLighter, rightEdgeLighter);
                    animatorSet.start();

                }
            }

            public void onFinish() { //sets the label to display a teleop error background and text
                if(running) {
                    secondsRemaining.setText("00");
                    topEdgeBar.setBackground(getResources().getDrawable(R.drawable.teleop_error));
                    bottomEdgeBar.setBackground(getResources().getDrawable(R.drawable.teleop_error));
                    leftEdgeBar.setBackground(getResources().getDrawable(R.drawable.teleop_error));
                    rightEdgeBar.setBackground(getResources().getDrawable(R.drawable.teleop_error));
                    timerID.setTextColor(context.getResources().getColor(R.color.border_warning));
                    timerID.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.timer_red, 0, 0, 0);
                    teleopWarning.setTextColor(getResources().getColor(R.color.white));
                    teleopWarning.setBackground(getResources().getDrawable(R.drawable.teleop_error));
                    teleopWarning.setText(getResources().getString(R.string.TeleopError));

                    ObjectAnimator topEdgeLighter = ObjectAnimator.ofFloat(topEdgeBar, View.ALPHA, 0.0f, 1.0f);
                    ObjectAnimator bottomEdgeLighter = ObjectAnimator.ofFloat(bottomEdgeBar, View.ALPHA, 0.0f, 1.0f);
                    ObjectAnimator rightEdgeLighter = ObjectAnimator.ofFloat(rightEdgeBar, View.ALPHA, 0.0f, 1.0f);
                    ObjectAnimator leftEdgeLighter = ObjectAnimator.ofFloat(leftEdgeBar, View.ALPHA, 0.0f, 1.0f);

                    int currentButtonColor = GenUtils.getAColor(context, R.color.melon);
                    if(!nextButton.isEnabled())
                        currentButtonColor = GenUtils.getAColor(context, R.color.night);

                    ValueAnimator teleopButtonAnim = ValueAnimator.ofArgb(currentButtonColor, GenUtils.getAColor(context, R.color.fire));
                    teleopButtonAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            nextButton.setBackgroundColor((Integer)animation.getAnimatedValue());
                        }
                    });

                    int currentArrowColor = GenUtils.getAColor(context, R.color.ice);
                    if(!nextButton.isEnabled())
                        currentArrowColor = GenUtils.getAColor(context, R.color.ocean);

                    ValueAnimator teleopArrowAnim = ValueAnimator.ofArgb(currentArrowColor, GenUtils.getAColor(context, R.color.ice));
                    teleopArrowAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            nextButton.getCompoundDrawablesRelative()[2].setColorFilter((Integer)animation.getAnimatedValue(), PorterDuff.Mode.SRC_IN);
                        }
                    });

                    teleopArrowAnim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            nextButton.getCompoundDrawablesRelative()[2].clearColorFilter();
                            nextButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.right,0);
                        }
                    });

                    ValueAnimator teleopTextAnim = ValueAnimator.ofArgb(nextButton.getCurrentTextColor(), GenUtils.getAColor(context, R.color.ice));
                    teleopTextAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            nextButton.setTextColor((Integer)animation.getAnimatedValue());
                        }
                    });

                    topEdgeLighter.setDuration(500);
                    bottomEdgeLighter.setDuration(500);
                    leftEdgeLighter.setDuration(500);
                    rightEdgeLighter.setDuration(500);
                    teleopButtonAnim.setDuration(500);
                    teleopTextAnim.setDuration(500);
                    teleopArrowAnim.setDuration(500);

                    AnimatorSet animatorSet1 = new AnimatorSet();
                    animatorSet1.playTogether(topEdgeLighter, bottomEdgeLighter, leftEdgeLighter, rightEdgeLighter, teleopButtonAnim, teleopTextAnim, teleopArrowAnim);

                    teleopButtonAnimation = ValueAnimator.ofArgb(GenUtils.getAColor(context, R.color.fire), GenUtils.getAColor(context, R.color.ocean));

                    teleopButtonAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            nextButton.setBackgroundColor((Integer)animation.getAnimatedValue());
                        }
                    });

                    teleopButtonAnimation.setDuration(500);;
                    teleopButtonAnimation.setRepeatMode(ValueAnimator.REVERSE);
                    teleopButtonAnimation.setRepeatCount(ValueAnimator.INFINITE);

                    animatorSet = new AnimatorSet();
                    animatorSet.playSequentially(animatorSet1, teleopButtonAnimation);
                    animatorSet.start();
                }
            }
        };

        if(firstTime) {
            firstTime = false;
            timer.start();
        }
        else {
            topEdgeBar.setAlpha(1);
            bottomEdgeBar.setAlpha(1);
            rightEdgeBar.setAlpha(1);
            leftEdgeBar.setAlpha(1);
        }

        //set listeners for buttons and fill the hashmap with data
        pickedUpCoralButton.setOnClickListener(new NumericalDataInputListener(pickedUpCoralCounter, autonHashMap, "CoralPickedUp", true, this));
        notPickedUpCoralButton.setOnClickListener(new NumericalDataInputListener(pickedUpCoralCounter, autonHashMap, "CoralPickedUp", false, this));

        scoredL4Button.setOnClickListener(new NumericalDataInputListener(scoredL4Counter, autonHashMap, "ScoredCoralL4", true, this));
        notScoredL4Button.setOnClickListener(new NumericalDataInputListener(scoredL4Counter, autonHashMap, "ScoredCoralL4", false, this));
        scoredL3Button.setOnClickListener(new NumericalDataInputListener(scoredL3Counter, autonHashMap, "ScoredCoralL3", true, this));
        notScoredL3Button.setOnClickListener(new NumericalDataInputListener(scoredL3Counter, autonHashMap, "ScoredCoralL3", false, this));
        scoredL2Button.setOnClickListener(new NumericalDataInputListener(scoredL2Counter, autonHashMap, "ScoredCoralL2", true, this));
        notScoredL2Button.setOnClickListener(new NumericalDataInputListener(scoredL2Counter, autonHashMap, "ScoredCoralL2", false, this));
        scoredL1Button.setOnClickListener(new NumericalDataInputListener(scoredL1Counter, autonHashMap, "ScoredCoralL1", true, this));
        notScoredL1Button.setOnClickListener(new NumericalDataInputListener(scoredL1Counter, autonHashMap, "ScoredCoralL1", false, this));

        missedL4Button.setOnClickListener(new NumericalDataInputListener(missedL4Counter, autonHashMap, "MissedCoralL4", true, this));
        notMissedL4Button.setOnClickListener(new NumericalDataInputListener(missedL4Counter, autonHashMap, "MissedCoralL4", false, this));
        missedL3Button.setOnClickListener(new NumericalDataInputListener(missedL3Counter, autonHashMap, "MissedCoralL3", true, this));
        notMissedL3Button.setOnClickListener(new NumericalDataInputListener(missedL3Counter, autonHashMap, "MissedCoralL3", false, this));
        missedL2Button.setOnClickListener(new NumericalDataInputListener(missedL2Counter, autonHashMap, "MissedCoralL2", true, this));
        notMissedL2Button.setOnClickListener(new NumericalDataInputListener(missedL2Counter, autonHashMap, "MissedCoralL2", false, this));
        missedL1Button.setOnClickListener(new NumericalDataInputListener(missedL1Counter, autonHashMap, "MissedCoralL1", true, this));
        notMissedL1Button.setOnClickListener(new NumericalDataInputListener(missedL1Counter, autonHashMap, "MissedCoralL1", false, this));

        removedL3Button.setOnClickListener(new NumericalDataInputListener(removedL3Counter, autonHashMap, "RemovedAlgaeL3", true, this));
        notRemovedL3Button.setOnClickListener(new NumericalDataInputListener(removedL3Counter, autonHashMap, "RemovedAlgaeL3", false, this));
        removedL2Button.setOnClickListener(new NumericalDataInputListener(removedL2Counter, autonHashMap, "RemovedAlgaeL2", true, this));
        notRemovedL2Button.setOnClickListener(new NumericalDataInputListener(removedL2Counter, autonHashMap, "RemovedAlgaeL2", false, this));

        attemptedL3Button.setOnClickListener(new NumericalDataInputListener(attemptedL3Counter, autonHashMap, "AttemptedAlgaeL3", true, this));
        notAttemptedL3Button.setOnClickListener(new NumericalDataInputListener(attemptedL3Counter, autonHashMap, "AttemptedAlgaeL3", false, this));
        attemptedL2Button.setOnClickListener(new NumericalDataInputListener(attemptedL2Counter, autonHashMap, "AttemptedAlgaeL2", true, this));
        notAttemptedL2Button.setOnClickListener(new NumericalDataInputListener(attemptedL2Counter, autonHashMap, "AttemptedAlgaeL2", false, this));

        scoredProcessorButton.setOnClickListener(new NumericalDataInputListener(scoredProcessorCounter, autonHashMap, "ScoredAlgaeProcessor", true, this));
        notScoredProcessorButton.setOnClickListener(new NumericalDataInputListener(scoredProcessorCounter, autonHashMap, "ScoredAlgaeProcessor", false, this));
        missedProcessorButton.setOnClickListener(new NumericalDataInputListener(missedProcessorCounter, autonHashMap, "MissedAlgaeProcessor", true, this));
        notMissedProcessorButton.setOnClickListener(new NumericalDataInputListener(missedProcessorCounter, autonHashMap, "MissedAlgaeProcessor", false, this));

        scoredNetButton.setOnClickListener(new NumericalDataInputListener(scoredNetCounter, autonHashMap, "ScoredAlgaeNet", true, this));
        notScoredNetButton.setOnClickListener(new NumericalDataInputListener(scoredNetCounter, autonHashMap, "ScoredAlgaeNet", false, this));
        missedNetButton.setOnClickListener(new NumericalDataInputListener(missedNetCounter, autonHashMap, "MissedAlgaeNet", true, this));
        notMissedNetButton.setOnClickListener(new NumericalDataInputListener(missedNetCounter, autonHashMap, "MissedAlgaeNet", false, this));


        leaveSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autonHashMap.put("Leave", isChecked ? "1" : "0");
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
                context.tabs.getTabAt(1).select();
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
        scoredAmpButton.setEnabled(enable);
        notScoredSpeakerButton.setEnabled(enable);
        notScoredAmpButton.setEnabled(enable);
        scoredSpeakerCounter.setEnabled(enable);
        scoredAmpCounter.setEnabled(enable);
        missedSpeakerCounter.setEnabled(enable);
        missedAmpCounter.setEnabled(enable);

        missedSpeakerButton.setEnabled(enable);
        missedAmpButton.setEnabled(enable);
        notMissedSpeakerButton.setEnabled(enable);
        notMissedAmpButton.setEnabled(enable);
    }

    private void miscButtonsEnabledState(boolean enable){
        miscID.setEnabled(enable);
        miscDescription.setEnabled(enable);
        leaveSwitch.setEnabled(enable);
        leaveID.setEnabled(enable);
        fellOverSwitch.setEnabled(enable);
        fellOverID.setEnabled(enable);
        nextButton.setEnabled(enable);
    }

    private void allButtonsEnabledState(boolean enable){
        possessionButtonsEnabledState(enable);
        scoringButtonsEnabledState(enable);

        miscID.setEnabled(enable);
        miscDescription.setEnabled(enable);
        leaveSwitch.setEnabled(enable);
        leaveID.setEnabled(enable);
    }

    public void updateXMLObjects(){
        scoredSpeakerCounter.setText(GenUtils.padLeftZeros(autonHashMap.get("ScoredSpeaker"), 2));
        scoredAmpCounter.setText(GenUtils.padLeftZeros(autonHashMap.get("ScoredAmp"), 2));
        missedSpeakerCounter.setText(GenUtils.padLeftZeros(autonHashMap.get("MissedSpeaker"), 2));
        missedAmpCounter.setText(GenUtils.padLeftZeros(autonHashMap.get("MissedAmp"), 2));
        pickedUpCounter.setText(GenUtils.padLeftZeros(autonHashMap.get("NumberPickedUp"), 2));
        leaveSwitch.setChecked(autonHashMap.get("Leave").equals("1"));

        if(setupHashMap.get("FellOver").equals("1")) {
            fellOverSwitch.setChecked(true);
            nextButton.setPadding(150, 0, 150, 0);
            nextButton.setText(R.string.GenerateQRCode);
            allButtonsEnabledState(false);
        } else {
            fellOverSwitch.setChecked(false);
            nextButton.setPadding(150, 0, 185, 0);
            nextButton.setText(R.string.TeleopNext);
            allButtonsEnabledState(true);
            // Disables decrement buttons if counter is at 0
            if(Integer.parseInt((String)pickedUpCounter.getText()) <= 0)
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
                autonHashMap = HashMapManager.getAutonHashMap();
                updateXMLObjects();
                // Set all objects in the fragment to their values from the HashMaps
            } else {
                if(teleopButtonAnimation != null) {
                    teleopButtonAnimation.cancel();
                    nextButton.setBackground(getResources().getDrawable(R.drawable.button_next_states));
                    nextButton.setTextColor(new ColorStateList(
                            new int [] [] {
                                    new int [] {android.R.attr.state_enabled},
                                    new int [] {}
                            },
                            new int [] {
                                    GenUtils.getAColor(context, R.color.ice),
                                    GenUtils.getAColor(context, R.color.ocean)
                            }
                    ));
                    nextButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.right_states,0);
                    nextButton.setSelected(true);
                }
                HashMapManager.putSetupHashMap(setupHashMap);
                HashMapManager.putAutonHashMap(autonHashMap);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        running = false;
        timer.cancel();
    }

    @Override
    public void onUpdate() {
        updateXMLObjects();
    }
}
