package com.mercury1089.scoutingapp2019;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import java.util.LinkedHashMap;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mercury1089.scoutingapp2019.utils.GenUtils;
import com.mercury1089.scoutingapp2019.utils.QRStringBuilder;

public class Stage extends Fragment {
    //HashMaps for sending QR data between screens
    private LinkedHashMap<String, String> setupHashMap;
    private LinkedHashMap<String, String> climbHashMap;

    //Buttons
    private ImageButton scoredTrapButton;
    private ImageButton notScoredTrapButton;
    private ImageButton missedTrapButton;
    private ImageButton notMissedTrapButton;
    private Button generateQRButton;

    //Switches
    private Switch parkSwitch;
    private Switch onstageSwitch;

    //TextViews
    private TextView endgameID;
    private TextView endgameDirections;
    private TextView stageZoneID;
    private TextView stageZoneDirections;
    private TextView parkID;
    private TextView onstageDirections;
    private TextView onstageID;
    private TextView trapScoringID;
    private TextView trapScoringDirections;
    private TextView trapScoredID;
    private TextView trapMissedID;
    private TextView trapScoredCounter;
    private TextView trapMissedCounter;

    //Containers
    private TabLayout stageTabs;

    //other variables
    private ProgressDialog progressDialog;
    private Dialog loading_alert;
    public final static int QRCodeSize = 500;

    public static Stage newInstance() {
        Stage fragment = new Stage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private MatchActivity context;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (MatchActivity) getActivity();
        return inflater.inflate(R.layout.fragment_climb, container, false);
    }

    public void onStart(){
        super.onStart();

        //linking variables to XML elements on the screen
        endgameID = getView().findViewById(R.id.IDEndgame);
        endgameDirections = getView().findViewById(R.id.IDEndgameDirections);
        stageZoneID = getView().findViewById(R.id.IDStageZone);
        stageZoneDirections = getView().findViewById(R.id.IDStageZoneDirections);

        parkID = getView().findViewById(R.id.IDPark);
        parkSwitch = getView().findViewById(R.id.parkSwitch);

        onstageID = getView().findViewById(R.id.IDOnstage);
        onstageSwitch = getView().findViewById(R.id.onstageSwitch);
        onstageDirections = getView().findViewById(R.id.IDOnstageDirections);
        stageTabs = getView().findViewById(R.id.stageTabs);

        trapScoringID = getView().findViewById(R.id.IDTrapScoring);
        trapScoredID = getView().findViewById(R.id.IDTrapScored);
        trapScoredCounter = getView().findViewById(R.id.scoredTrapCounter);
        trapMissedID = getView().findViewById(R.id.IDTrapMissed);
        trapMissedCounter = getView().findViewById(R.id.missedTrapCounter);

        scoredTrapButton = getView().findViewById(R.id.scoredTrapButton);
        notScoredTrapButton = getView().findViewById(R.id.notScoredTrapButton);
        missedTrapButton = getView().findViewById(R.id.missedTrapButton);
        notMissedTrapButton = getView().findViewById(R.id.notMissedTrapButton);

        generateQRButton = getView().findViewById(R.id.GenerateQRButton);

        //Removes tab indicator because climb switch starts out as null
        stageTabs.setSelectedTabIndicator(null);

        //set listeners for buttons

        parkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                climbHashMap.put("Park", isChecked ? "1" : "0");
                updateXMLObjects();
            }
        });

        onstageSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                climbHashMap.put("Onstage", isChecked ? "1" : "0");
                //Default option for rung is LOW
                if (isChecked) {
                    //Sets tab indicator to built-in default
                    stageTabs.setSelectedTabIndicator(R.drawable.mtrl_tabs_default_indicator);
                    stageTabs.getTabAt(0).select();
                    climbHashMap.put("Stage", "L");
                } else {
                    //Removes tab indicator
                    stageTabs.setSelectedTabIndicator(null);

                }
                updateXMLObjects();
            }
        });

        stageTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String text = (String) tab.getText();
                if (text.equals(getResources().getString(R.string.LeftStage)))
                    climbHashMap.put("Stage", "L");
                else if (text.equals(getResources().getString(R.string.CenterStage)))
                    climbHashMap.put("Stage", "C");
                else if (text.equals(getResources().getString(R.string.RightStage)))
                    climbHashMap.put("Stage", "R");
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { climbHashMap.put("Stage", "N"); }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        scoredTrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt((String) trapScoredCounter.getText());
                currentCount++;
                climbHashMap.put("ScoredTrap", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

        notScoredTrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt((String) trapScoredCounter.getText());
                if (currentCount > 0)
                    currentCount--;
                climbHashMap.put("ScoredTrap", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

        missedTrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt((String) trapMissedCounter.getText());
                currentCount++;
                climbHashMap.put("MissedTrap", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });

        notMissedTrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt((String) trapMissedCounter.getText());
                if (currentCount > 0)
                    currentCount--;
                climbHashMap.put("MissedTrap", String.valueOf(currentCount));
                updateXMLObjects();
            }
        });


        generateQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.generate_qrcode_confirm_popup);

                Button generateQRButton = dialog.findViewById(R.id.GenerateQRButton);
                Button cancelConfirm = dialog.findViewById(R.id.CancelConfirm);

                dialog.show();

                generateQRButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loading_alert = new Dialog(context);
                        loading_alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        loading_alert.setContentView(R.layout.loading_screen);
                        loading_alert.setCancelable(false);
                        loading_alert.show();

                        HashMapManager.putSetupHashMap(setupHashMap);
                        HashMapManager.putClimbHashMap(climbHashMap);

                        Stage.QRRunnable qrRunnable = new Stage.QRRunnable();
                        new Thread(qrRunnable).start();
                        dialog.dismiss();
                    }
                });

                cancelConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void onstageTabsEnabledState(boolean enable) {
        if (!enable)
            climbHashMap.put("Stage", "N");
        LinearLayout tabStrip = ((LinearLayout) stageTabs.getChildAt(0));
        tabStrip.setEnabled(enable);
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setEnabled(enable);
            tabStrip.getChildAt(i).setClickable(enable);
        }
    }

    private void stageButtonsEnabledState(boolean enable) {
        parkSwitch.setEnabled(enable);
        onstageSwitch.setEnabled(enable);

        //Always want the climbed switch and "climb" text next to switch to be enabled unless fell over/died is checked
        onstageTabsEnabledState(enable);
    }

    private void stageZoneEnabledState(boolean enable) {
        // "Post Match" title and directions
        endgameID.setEnabled(enable);
        endgameDirections.setEnabled(enable);
        // Other TextViews
        stageZoneID.setEnabled(enable);
        stageZoneDirections.setEnabled(enable);
        onstageDirections.setEnabled(enable);
        parkID.setEnabled(enable);
        onstageID.setEnabled(enable);

        // Buttons
        stageButtonsEnabledState(enable);
    }

    private void updateXMLObjects() {
        // Update counters
        trapScoredCounter.setText(GenUtils.padLeftZeros(climbHashMap.get("ScoredTrap"), 3));
        trapMissedCounter.setText(GenUtils.padLeftZeros(climbHashMap.get("MissedTrap"), 3));

        if (setupHashMap.get("FellOver").equals("1")) {
            stageButtonsEnabledState(false);
            onstageSwitch.setChecked(false);
            climbHashMap.put("Stage", "N");
            stageZoneEnabledState(false);
        } else if (setupHashMap.get("FellOver").equals("0")) {
            stageZoneEnabledState(true);
        }

        if (climbHashMap.get("Onstage").equals("0")) {
            climbHashMap.put("Stage", "N");
            onstageSwitch.setChecked(false);
            onstageTabsEnabledState(false);
        } else if (climbHashMap.get("Onstage").equals("1")) {
            // If robot is onstage, they cannot be parked
            parkSwitch.setChecked(false);
            climbHashMap.put("Park", "Y");
            onstageSwitch.setChecked(true);
            onstageTabsEnabledState(true);
        }

        if (Integer.parseInt((String) trapScoredCounter.getText()) <= 0)
            notScoredTrapButton.setEnabled(false);
        else
            notScoredTrapButton.setEnabled(true);
        if (Integer.parseInt((String) trapMissedCounter.getText()) <= 0)
            notMissedTrapButton.setEnabled(false);
        else
            notMissedTrapButton.setEnabled(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming visible, then...
            if (isVisibleToUser) {
                setupHashMap = HashMapManager.getSetupHashMap();
                climbHashMap = HashMapManager.getClimbHashMap();
                updateXMLObjects();
                //set all objects in the fragment to their values from the HashMaps
            } else {
                HashMapManager.putSetupHashMap(setupHashMap);
                HashMapManager.putClimbHashMap(climbHashMap);
            }
        }
    }

    //QR Generation
    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRCodeSize, QRCodeSize, null
            );
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        GenUtils.getAColor(context, R.color.black) : GenUtils.getAColor(context, R.color.white);
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    class QRRunnable implements Runnable {
        @Override
        public void run() {
            HashMapManager.checkNullOrEmpty(HashMapManager.HASH.AUTON);
            HashMapManager.checkNullOrEmpty(HashMapManager.HASH.TELEOP);
            HashMapManager.checkNullOrEmpty(HashMapManager.HASH.CLIMB);

            QRStringBuilder.buildQRString();

            try {
                Bitmap bitmap = TextToImageEncode(QRStringBuilder.getQRString());
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.popup_qr);

                        ImageView imageView = dialog.findViewById(R.id.imageView);
                        TextView scouterName = dialog.findViewById(R.id.ScouterNameQR);
                        TextView teamNumber = dialog.findViewById(R.id.TeamNumberQR);
                        TextView matchNumber = dialog.findViewById(R.id.MatchNumberQR);
                        Button goBackToMain = dialog.findViewById(R.id.GoBackButton);
                        imageView.setImageBitmap(bitmap);

                        dialog.setCancelable(false);

                        scouterName.setText(setupHashMap.get("ScouterName"));
                        teamNumber.setText(setupHashMap.get("TeamNumber"));
                        matchNumber.setText(setupHashMap.get("MatchNumber"));

                        loading_alert.dismiss();

                        dialog.show();

                        goBackToMain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Dialog confirmDialog = new Dialog(context);
                                confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                confirmDialog.setContentView(R.layout.setup_next_match_confirm_popup);

                                Button setupNextMatchButton = confirmDialog.findViewById(R.id.SetupNextMatchButton);
                                Button cancelConfirm = confirmDialog.findViewById(R.id.CancelConfirm);

                                confirmDialog.show();

                                setupNextMatchButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        QRStringBuilder.clearQRString(context);
                                        HashMapManager.setupNextMatch();
                                        Intent intent = new Intent(context, PregameActivity.class);
                                        startActivity(intent);
                                        context.finish();
                                        dialog.dismiss();
                                        confirmDialog.dismiss();
                                    }
                                });

                                cancelConfirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        confirmDialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                });
            } catch (WriterException e){}
        }
    }
}
