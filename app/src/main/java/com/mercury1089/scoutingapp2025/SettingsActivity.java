package com.mercury1089.scoutingapp2025;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.mercury1089.scoutingapp2025.repository.MatchRepository;
import com.mercury1089.scoutingapp2025.utils.ListAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingsActivity extends AppCompatActivity {
    private MatchRepository matchRepository;
    private CompositeDisposable disposables = new CompositeDisposable();
    private LinkedHashMap settingsHashMap;
    private String[] qrList;
    private ListView qrCodeSelector;
    private ListAdapter listAdapter;

    // Password buttons
    private Button createResetPasswordButton, changePasswordButton;
    private TextView passwordPreviewID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        matchRepository = new MatchRepository(getApplicationContext());

        //assigning variables to their equivalent screen elements
        Button localStorageResetButton = findViewById(R.id.LocalStorageResetButton);
        Button backButton = findViewById(R.id.BackButton);
        Button clearQRCache = findViewById(R.id.ClearQRCodesButton);
        createResetPasswordButton = findViewById(R.id.CreateResetPasswordButton);
        changePasswordButton = findViewById(R.id.ChangePasswordButton);
        passwordPreviewID = findViewById(R.id.IDPasswordPreview);
        updatePasswordButtons();

        EditText eventKeyInput = findViewById(R.id.EventKeyInput);

        disposables.add(matchRepository.getStoredEventKey()
                .subscribe(
                        eventKeyInput::setText,
                        throwable -> Log.d("1089", throwable.getMessage())
                ));
        Button fetchMatchesButton = findViewById(R.id.FetchMatchesFromEventButton);
        TextView lastFetchedID = findViewById(R.id.LastFetchedAtID);

        // onComplete (third argument) is called when the action completes but doesn't return a result
        disposables.add(getLastFetchedDate().subscribe(
                str -> lastFetchedID.setText(getString(R.string.LastFetchedAtID, str)),
                throwable -> Log.d("1089", throwable.getMessage()),
                () -> lastFetchedID.setText(getString(R.string.LastFetchedAtID, "Unknown"))
        ));

        qrCodeSelector = findViewById(R.id.QRCodeListView);

        HashMapManager.checkNullOrEmpty(HashMapManager.HASH.SETTINGS);
        settingsHashMap = HashMapManager.getSettingsHashMap();
        qrList = HashMapManager.setupQRList(getApplicationContext());

        listAdapter = new ListAdapter(this, qrList);
        addQRCodes();

        if(qrList.length > 0)
            clearQRCache.setEnabled(true);
        else
            clearQRCache.setEnabled(false);

        localStorageResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMapManager.setDefaultValues(HashMapManager.HASH.SETTINGS);
                HashMapManager.setDefaultValues(HashMapManager.HASH.SETUP);
                HashMapManager.setDefaultValues(HashMapManager.HASH.AUTON);
                HashMapManager.setDefaultValues(HashMapManager.HASH.TELEOP);
                HashMapManager.setDefaultValues(HashMapManager.HASH.CLIMB);
                Toast.makeText(SettingsActivity.this, "All variables successfully reset.", Toast.LENGTH_SHORT).show();
            }
        });

        createResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createResetPasswordButton.getText().equals(getString(R.string.ResetPassword))) {
                    HashMapManager.saveSettingsPassword(new String[] {"", "N"}, getApplicationContext());
                    Toast.makeText(getApplicationContext(),"Successfully reset password!", Toast.LENGTH_SHORT).show();
                    updatePasswordButtons();
                } else {
                    createPasswordChangeDialog();
                }
            }
        });

        changePasswordButton.setOnClickListener(v -> {
            createPasswordChangeDialog();
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, PregameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        clearQRCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(SettingsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.clear_qr_cache_confirm);

                Button clearConfirm = dialog.findViewById(R.id.ClearConfirm);
                Button cancelConfirm = dialog.findViewById(R.id.CancelConfirm);

                dialog.show();

                clearConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMapManager.outputQRList(new String[0], SettingsActivity.this);
                        qrList = HashMapManager.setupQRList(SettingsActivity.this);
                        listAdapter = new ListAdapter(SettingsActivity.this, qrList);
                        qrCodeSelector.setAdapter(listAdapter);
                        listAdapter.notifyDataSetChanged();
                        clearQRCache.setEnabled(false);
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

        fetchMatchesButton.setOnClickListener(view -> {
            String eventKey = eventKeyInput.getText().toString();

            disposables.add(matchRepository.storeMatchesByEvent(eventKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Toast.makeText(getApplicationContext(), "Matches fetched and stored!", Toast.LENGTH_SHORT).show();
                            Disposable setLastFetchedTextAgain = getLastFetchedDate().subscribe(
                                    str -> lastFetchedID.setText(getString(R.string.LastFetchedAtID, str)),
                                    throwable -> Log.d("1089", throwable.getMessage())
                            );
                        },
                        error -> Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                ));
        });
    }

    private void createPasswordChangeDialog() {
        Context context = SettingsActivity.this;
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.enter_password_popup);

        TextView passwordField = dialog.findViewById(R.id.PasswordField);
        Switch requirePasswordSwitch = dialog.findViewById(R.id.SettingsPasswordSwitch);
        Button doneButton = dialog.findViewById(R.id.DoneButton);
        Button cancelButton = dialog.findViewById(R.id.CancelButton);

        String[] passwordData = HashMapManager.pullSettingsPassword(context);
        try {
            passwordField.setText(passwordData[0]);
            requirePasswordSwitch.setChecked(passwordData[1].equals("Y"));
        } catch (Exception e) {
        }

        passwordField.setHint(settingsHashMap.get("DefaultPassword").toString());

        dialog.show();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordField.getText().toString(), requiredPassword = requirePasswordSwitch.isChecked() ? "Y" : "N";
                HashMapManager.saveSettingsPassword(new String[]{password, requiredPassword}, context);
                dialog.dismiss();
                HashMapManager.saveSettingsPassword(new String[]{password, requiredPassword}, context);
                createResetPasswordButton.setSelected(requiredPassword.equals("Y"));
                updatePasswordButtons();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void updatePasswordButtons() {
        boolean passwordSet, usePassword;
        String[] password = HashMapManager.pullSettingsPassword(getApplicationContext());
        if (password != null) {
            passwordSet = !password[0].isEmpty();
            usePassword = password[1].equalsIgnoreCase("y");
        } else {
            passwordSet = false;
            usePassword = false;
        }
        createResetPasswordButton.setSelected(usePassword);
        createResetPasswordButton.setText(passwordSet ? R.string.ResetPassword : R.string.CreatePassword);
        changePasswordButton.setVisibility(passwordSet ? View.VISIBLE : View.INVISIBLE);
        passwordPreviewID.setText(
                passwordSet ? "*".repeat(Math.min(password[0].length(), 16)): getString(R.string.DefaultPasswordPreview));
    }

    private Maybe<String> getLastFetchedDate() {
        return matchRepository.getLastFetchedTime()
                .subscribeOn(Schedulers.io())
                .map(longTime -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss", Locale.getDefault());
                    return sdf.format(new Date(longTime));
                })
                .onErrorReturnItem("Unknown")
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void addQRCodes(){
        qrCodeSelector.setAdapter(listAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposables.clear();
        HashMapManager.putSettingsHashMap(settingsHashMap);
    }
}