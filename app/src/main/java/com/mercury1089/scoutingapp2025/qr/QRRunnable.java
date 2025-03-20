package com.mercury1089.scoutingapp2025.qr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.mercury1089.scoutingapp2025.HashMapManager;
import com.mercury1089.scoutingapp2025.PregameActivity;
import com.mercury1089.scoutingapp2025.R;
import com.mercury1089.scoutingapp2025.utils.QRStringBuilder;

import java.util.LinkedHashMap;

public class QRRunnable implements Runnable {
    private final Activity context;
    LinkedHashMap<String, String> setupHashMap = HashMapManager.getSetupHashMap();
    LinkedHashMap<String, String> autonHashMap = HashMapManager.getAutonHashMap();
    LinkedHashMap<String, String> teleopHashMap = HashMapManager.getTeleopHashMap();
    public QRRunnable(Activity ctx) {
        this.context = ctx;
    }
    @Override
    public void run() {
        // Once QR is generated, hashmap values go back to defaults
        HashMapManager.setDefaultValues(HashMapManager.HASH.AUTON);
        HashMapManager.setDefaultValues(HashMapManager.HASH.TELEOP);
        HashMapManager.setDefaultValues(HashMapManager.HASH.CLIMB);

        // Make the actual qr string
        QRStringBuilder.buildQRString();

        try {
            Bitmap bitmap = QRUtils.textToImageEncode(this.context, QRStringBuilder.getQRString());
            context.runOnUiThread(() -> {
                // Show loading alert dialog
                Dialog loading_alert = new Dialog(context);
                loading_alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                loading_alert.setContentView(R.layout.loading_screen);
                loading_alert.setCancelable(false);
                loading_alert.show();

                HashMapManager.putSetupHashMap(setupHashMap);

                // Show the QR and store it in the cache
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_qr);
                QRStringBuilder.storeQRString(context);

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

                goBackToMain.setOnClickListener(v -> {
                    Dialog confirmDialog = new Dialog(context);
                    confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    confirmDialog.setContentView(R.layout.setup_next_match_confirm_popup);

                    Button setupNextMatchButton = confirmDialog.findViewById(R.id.SetupNextMatchButton);
                    Button cancelConfirm = confirmDialog.findViewById(R.id.CancelConfirm);

                    confirmDialog.show();

                    setupNextMatchButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            QRStringBuilder.clearQRString();
                            HashMapManager.setupNextMatch();
                            Intent intent = new Intent(context, PregameActivity.class);
                            context.startActivity(intent);
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
                });
            });
        } catch (WriterException e){}
    }
}
