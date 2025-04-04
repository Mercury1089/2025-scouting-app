package com.mercury1089.scoutingapp2025;

import com.mercury1089.scoutingapp2025.database.model.Match;
import com.mercury1089.scoutingapp2025.database.util.DBUtil;
import com.mercury1089.scoutingapp2025.qr.QRRunnable;
import com.mercury1089.scoutingapp2025.repository.MatchRepository;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PregameActivity extends AppCompatActivity {
    // Strategy was here
    //Set the default password in HashMapManager.setDefaultValues();
    String password;
    CompositeDisposable disposables = new CompositeDisposable();

    //variables that store elements of the screen for the output variables
    //Buttons
    private ImageButton settingsButton;
    private Button blueButton;
    private Button redButton;
    private Button clearButton;
    private Button autofillButton;
    private Button startButton;

    //Text Fields
    private EditText scouterNameInput;
    private EditText matchNumberInput;
    private EditText teamNumberInput;
    private EditText firstAlliancePartnerInput;
    private EditText secondAlliancePartnerInput;
    private TextView startDirectionsToast;

    //Switches
    private Switch noShowSwitch;
    private Switch preloadSwitch;

    //HashMaps
    private LinkedHashMap<String, String> settingsHashMap;
    private LinkedHashMap<String, String> setupHashMap;

    private Dialog loading_alert;
    private ProgressDialog progressDialog;


    Bitmap bitmap;
    //ProgressDialog progressDialog;
    boolean isQRButton = false;

    //others
    private MediaPlayer rooster;
    private ImageView slackCenter;

    /*
    - This is where you initialize the activity:
        - Define views (screen elements) here
        - Make sure relevant HashMaps aren't empty using checkNullOrEmpty()
        - Define EventListeners -> Not using lambda to make it easier for newcomers
        -
        -
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregame);

        // Initialize views here
        scouterNameInput = findViewById(R.id.ScouterNameInput);
        matchNumberInput = findViewById(R.id.MatchNumberInput);
        teamNumberInput = findViewById(R.id.TeamNumberInput);
        firstAlliancePartnerInput = findViewById(R.id.FirstAlliancePartnerInput);
        secondAlliancePartnerInput = findViewById(R.id.SecondAlliancePartnerInput);
        blueButton = findViewById(R.id.BlueButton);
        redButton = findViewById(R.id.RedButton);
        noShowSwitch = findViewById(R.id.NoShowSwitch);
        preloadSwitch = findViewById(R.id.PreloadedCargoSwitch);
        clearButton = findViewById(R.id.ClearButton);
        autofillButton = findViewById(R.id.AutofillButton);
        startButton = findViewById(R.id.StartButton);
        settingsButton = findViewById(R.id.SettingsButton);
        startDirectionsToast = findViewById(R.id.IDStartDirections);

        rooster = MediaPlayer.create(PregameActivity.this, R.raw.sound);

        // Make sure hash maps are not empty/null, then get HashMaps and set password for settings screen
        HashMapManager.checkNullOrEmpty(HashMapManager.HASH.SETTINGS);
        HashMapManager.checkNullOrEmpty(HashMapManager.HASH.SETUP);
        settingsHashMap = HashMapManager.getSettingsHashMap();
        setupHashMap = HashMapManager.getSetupHashMap();
        password = settingsHashMap.get("DefaultPassword");

        //setting group buttons to default state
        updateXMLObjects(true);

        scouterNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setupHashMap.put("ScouterName", scouterNameInput.getText().toString());
                updateXMLObjects(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        matchNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setupHashMap.put("MatchNumber", matchNumberInput.getText().toString());
                updateXMLObjects(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        teamNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setupHashMap.put("TeamNumber", teamNumberInput.getText().toString());
                updateXMLObjects(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        firstAlliancePartnerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setupHashMap.put("AlliancePartner1", firstAlliancePartnerInput.getText().toString());
                updateXMLObjects(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        secondAlliancePartnerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setupHashMap.put("AlliancePartner2", secondAlliancePartnerInput.getText().toString());
                updateXMLObjects(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //starting listener to check the status of the switch
        noShowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    setupHashMap.put("PreloadNote", "N");
                setupHashMap.put("NoShow", isChecked ? "Y" : "N");
                updateXMLObjects(false);
            }
        });

        //starting listener to check status of switch
        preloadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                setupHashMap.put("PreloadNote", isChecked ? "Y" : "N");
                updateXMLObjects(false);
            }
        });

        //click methods
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] passwordData = HashMapManager.pullSettingsPassword(PregameActivity.this);
                final String password, requiredPassword;
                String tempPassword, tempRequired;
                try {
                    tempPassword = passwordData[0];
                    tempRequired = passwordData[1];
                } catch (Exception e) {
                    tempPassword = PregameActivity.this.password;
                    tempRequired = "N";
                }

                password = tempPassword;
                requiredPassword = tempRequired;

                if (requiredPassword.equals("N")) {
                    HashMapManager.putSetupHashMap(setupHashMap);
                    Intent intent = new Intent(PregameActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

                Dialog dialog = new Dialog(PregameActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.settings_password);

                TextView passwordField = dialog.findViewById(R.id.PasswordField);
                Button confirm = dialog.findViewById(R.id.ConfirmButton);
                Button cancel = dialog.findViewById(R.id.CancelButton);
                ImageView topEdgeBar = dialog.findViewById(R.id.topEdgeBar);
                ImageView bottomEdgeBar = dialog.findViewById(R.id.bottomEdgeBar);
                ImageView leftEdgeBar = dialog.findViewById(R.id.leftEdgeBar);
                ImageView rightEdgeBar = dialog.findViewById(R.id.rightEdgeBar);

                dialog.show();

                passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                            //do what you want on the press of 'done'
                            confirm.performClick();
                        }
                        return false;
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String savedPassword = !password.equals("") ? password : PregameActivity.this.password;
                        if (passwordField.getText().toString().equals(savedPassword)) {
                            HashMapManager.putSetupHashMap(setupHashMap);
                            Intent intent = new Intent(PregameActivity.this, SettingsActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        } else {
                            Toast.makeText(PregameActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

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

                            /*
                            ObjectAnimator topEdgeLighterOn = ObjectAnimator.ofFloat(topEdgeBar, View.ALPHA, 0.0f, 1.0f);
                            ObjectAnimator bottomEdgeLighterOn = ObjectAnimator.ofFloat(bottomEdgeBar, View.ALPHA, 0.0f, 1.0f);
                            ObjectAnimator rightEdgeLighterOn = ObjectAnimator.ofFloat(rightEdgeBar, View.ALPHA, 0.0f, 1.0f);
                            ObjectAnimator leftEdgeLighterOn = ObjectAnimator.ofFloat(leftEdgeBar, View.ALPHA, 0.0f, 1.0f);

                            ObjectAnimator topEdgeLighterOff = ObjectAnimator.ofFloat(topEdgeBar, View.ALPHA, 1.0f, 0.0f);
                            ObjectAnimator bottomEdgeLighterOff = ObjectAnimator.ofFloat(bottomEdgeBar, View.ALPHA, 1.0f, 0.0f);
                            ObjectAnimator rightEdgeLighterOff = ObjectAnimator.ofFloat(rightEdgeBar, View.ALPHA, 1.0f, 0.0f);
                            ObjectAnimator leftEdgeLighterOff = ObjectAnimator.ofFloat(leftEdgeBar, View.ALPHA, 1.0f, 0.0f);

                            topEdgeLighterOn.setDuration(250);
                            bottomEdgeLighterOn.setDuration(250);
                            rightEdgeLighterOn.setDuration(250);
                            leftEdgeLighterOn.setDuration(250);

                            topEdgeLighterOff.setDuration(200);
                            bottomEdgeLighterOff.setDuration(200);
                            rightEdgeLighterOff.setDuration(200);
                            leftEdgeLighterOff.setDuration(200);

                            AnimatorSet animateOn = new AnimatorSet();
                            AnimatorSet animateOff = new AnimatorSet();
                            AnimatorSet animatorSet = new AnimatorSet();

                            animateOn.playTogether(topEdgeLighterOn, bottomEdgeLighterOn, rightEdgeLighterOn, leftEdgeLighterOn);

                            animateOff.playTogether(topEdgeLighterOff, bottomEdgeLighterOff, rightEdgeLighterOff, leftEdgeLighterOff);

                            animatorSet.playSequentially(animateOn, animateOff);
                            animatorSet.start();
                             */
                        }
                    }
                });

                cancel.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }));
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupHashMap.put("AllianceColor", setupHashMap.get("AllianceColor").equals("Blue") ? "" : "Blue");
                updateXMLObjects(false);
            }
        });

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupHashMap.put("AllianceColor", setupHashMap.get("AllianceColor").equals("Red") ? "" : "Red");
                updateXMLObjects(false);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure that any async operations (like fetching from database) are cancelled
                disposables.clear();
                if (isQRButton) {

                    Dialog dialog = new Dialog(PregameActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.generate_qrcode_confirm_popup);

                    Button generateQRButton = dialog.findViewById(R.id.GenerateQRButton);
                    Button cancelConfirm = dialog.findViewById(R.id.CancelConfirm);

                    dialog.show();

                    generateQRButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HashMapManager.putSetupHashMap(setupHashMap);
                            dialog.dismiss(); // Dismiss the confirmation dialogue
                            // Show the loading dialog
                            loading_alert = new Dialog(PregameActivity.this);
                            loading_alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            loading_alert.setContentView(R.layout.loading_screen);
                            loading_alert.setCancelable(false);
                            loading_alert.show();

                            QRRunnable runnable = new QRRunnable(PregameActivity.this, loading_alert);
                            new Thread(runnable).start();
                        }
                    });

                    cancelConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    HashMapManager.putSetupHashMap(setupHashMap);
                    if (scouterNameInput.getText().toString().equals("Mercury") && matchNumberInput.getText().toString().equals("1") && teamNumberInput.getText().toString().equals("0") && firstAlliancePartnerInput.getText().toString().equals("8") && secondAlliancePartnerInput.getText().toString().equals("9")) {
                        settingsHashMap.put("NothingToSeeHere", "1");
                        HashMapManager.setDefaultValues(HashMapManager.HASH.SETUP);
                        setupHashMap = HashMapManager.getSetupHashMap();

                        updateXMLObjects(true);
                        return;
                    } else if (scouterNameInput.getText().toString().equals("0x") && matchNumberInput.getText().toString().equals("441") && teamNumberInput.getText().toString().equals("1089") && firstAlliancePartnerInput.getText().toString().equals("1089") && secondAlliancePartnerInput.getText().toString().equals("1089")) {
                        settingsHashMap.put("Slack", "1");
                        HashMapManager.setDefaultValues(HashMapManager.HASH.SETUP);
                        setupHashMap = HashMapManager.getSetupHashMap();

                        updateXMLObjects(true);
                        return;
                    } else if (scouterNameInput.getText().toString().equals("admin") && matchNumberInput.getText().toString().equals("1") && teamNumberInput.getText().toString().equals("0") && firstAlliancePartnerInput.getText().toString().equals("8") && secondAlliancePartnerInput.getText().toString().equals("9")) {
                        HashMapManager.saveSettingsPassword(new String[]{"", "N"}, PregameActivity.this);
                        HashMapManager.setDefaultValues(HashMapManager.HASH.SETUP);
                        setupHashMap = HashMapManager.getSetupHashMap();

                        updateXMLObjects(true);
                        return;
                    } else if (settingsHashMap.get("NothingToSeeHere").equals("1")) {
                        rooster.start();
                    } else if (teamNumberInput.getText().toString().equals(firstAlliancePartnerInput.getText().toString()) || teamNumberInput.getText().toString().equals(secondAlliancePartnerInput.getText().toString())) {
                        Toast.makeText(PregameActivity.this, "A team cannot be its own partner.", Toast.LENGTH_SHORT).show();
                        setupHashMap.put("TeamNumber", "");
                        setupHashMap.put("AlliancePartner1", "");
                        setupHashMap.put("AlliancePartner2", "");
                        teamNumberInput.requestFocus();
                        updateXMLObjects(true);
                        return;

                    }
                    Intent intent = new Intent(PregameActivity.this, MatchActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(PregameActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.clear_confirm_popup);

                Button clearConfirm = dialog.findViewById(R.id.ClearConfirm);
                Button cancelConfirm = dialog.findViewById(R.id.CancelConfirm);

                dialog.show();

                cancelConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                clearConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        HashMapManager.setDefaultValues(HashMapManager.HASH.SETUP);
                        setupHashMap = HashMapManager.getSetupHashMap();
                        updateXMLObjects(true);
                    }
                });
            }
        });

        autofillButton.setOnClickListener(view -> {
            if (matchNumberInput.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Match number is required.", Toast.LENGTH_SHORT).show();
                return;
            }
            int matchNumber = Integer.parseInt(matchNumberInput.getText().toString());
            MatchRepository mr = new MatchRepository(getApplicationContext());
            disposables.add(mr.getStoredEventKey().subscribe(
                    eventKey -> {
                        Log.d("MR", "Event key: " + eventKey);
                        disposables.add(mr.getStoredMatch(DBUtil.createQualificationMatchKey(eventKey, matchNumber)).subscribe(
                                match -> autofillMatchInfo(match, "R1"),
                                throwable -> Toast.makeText(getApplicationContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show()
                        ));
                    },
                    throwable -> Toast.makeText(getApplicationContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show()
            ));

        });


    }

    public void autofillMatchInfo(Match match, String assignment) {
        boolean allianceColor = assignment.toLowerCase().charAt(0) == 'r'; // 1 = red, 0 = blue
        List<Integer> teams = allianceColor ? match.getRedAllianceTeams() : match.getBlueAllianceTeams();
        Log.d("MR", "Teams: " + teams);
        int assignmentNumber = Integer.parseInt(String.valueOf(assignment.charAt(assignment.length()-1))) - 1; // because teams is zero-indexed
        for (int i = 0; i < teams.size(); i++) {
            int team = teams.get(i);
            if (i == assignmentNumber) teamNumberInput.setText(String.valueOf(team));
            else if (firstAlliancePartnerInput.getText().toString().isEmpty()) {
                firstAlliancePartnerInput.setText(String.valueOf(team));
            }
            else {
                secondAlliancePartnerInput.setText(String.valueOf(team));
            }
        }
        Toast.makeText(getApplicationContext(), "Successfully autofilled match info!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateXMLObjects(true);
    }

    //call methods
    /*public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    /*
    - Validate text field input and toggle values to make sure it is safe (and necessary) to
    - move into the MatchActivity
     */
    private boolean readyToStart() {
        return scouterNameInput.getText().length() > 0 &&
                matchNumberInput.getText().length() > 0 &&
                teamNumberInput.getText().length() > 0 &&
                firstAlliancePartnerInput.getText().length() > 0 &&
                secondAlliancePartnerInput.getText().length() > 0 &&
                !setupHashMap.get("AllianceColor").isEmpty() &&
                (Objects.equals(setupHashMap.get("NoShow"), "Y") || Objects.equals(setupHashMap.get("NoShow"), "N"));
    }

    /*
    - Check to see if there's any values that need to be cleared
    - (if nothing is filled out, clear button should be disabled)
     */
    private boolean canClearInputs() {
        return scouterNameInput.getText().length() > 0 ||
                matchNumberInput.getText().length() > 0 ||
                teamNumberInput.getText().length() > 0 ||
                noShowSwitch.isChecked() ||
                firstAlliancePartnerInput.getText().length() > 0 ||
                secondAlliancePartnerInput.getText().length() > 0 ||
                blueButton.isSelected() || redButton.isSelected();
    }

    /*
    - Big complicated looking function, so let's break it down
        - This is called on most events (so in all the View EventListeners)
        - It updates hashmaps and the visual appearance of Views
     */
    private void updateXMLObjects(boolean updateText) {
        boolean readyToStart = readyToStart();
        boolean canClear = canClearInputs();

        autofillButton.setEnabled(!matchNumberInput.getText().toString().isEmpty());

        /*
        - updateText should only be true if you want to reset the basic info fields to the stored hashmap values
            - e.g. if you're returning from SettingsActivity or if you used the "Clear" button
         */
        if (updateText) {
            scouterNameInput.setText(setupHashMap.get("ScouterName"));
            matchNumberInput.setText(setupHashMap.get("MatchNumber"));
            teamNumberInput.setText(setupHashMap.get("TeamNumber"));
            firstAlliancePartnerInput.setText(setupHashMap.get("AlliancePartner1"));
            secondAlliancePartnerInput.setText(setupHashMap.get("AlliancePartner2"));
        }

        blueButton.setSelected(setupHashMap.get("AllianceColor").equals("Blue"));
        redButton.setSelected(setupHashMap.get("AllianceColor").equals("Red"));

        if (settingsHashMap.get("Slack").equals("1"))
            slackCenter.setVisibility(View.VISIBLE);

        preloadSwitch.setChecked(setupHashMap.get("PreloadNote").equals("Y"));

        if (setupHashMap.get("NoShow").equals("Y")) {
            preloadSwitch.setEnabled(false);
            noShowSwitch.setChecked(true);

            startButton.setPadding(185, 0, 185, 0);
            startButton.setText(R.string.GenerateQRCode);
            startButton.setCompoundDrawablesRelativeWithIntrinsicBounds(this.getDrawable(R.drawable.qr), null, null, null);
            isQRButton = true;
        } else {
            preloadSwitch.setEnabled(true);
            noShowSwitch.setChecked(false);
            startButton.setCompoundDrawablesRelativeWithIntrinsicBounds(this.getDrawable(R.drawable.start_button_symbol_states), null, null, null);
            startButton.setPadding(234, 0, 234, 0);
            startButton.setText(R.string.Start);
            isQRButton = false;
        }

        startButton.setEnabled(readyToStart);
        startDirectionsToast.setEnabled(readyToStart && !isQRButton);
        clearButton.setEnabled(canClear);
    }
}