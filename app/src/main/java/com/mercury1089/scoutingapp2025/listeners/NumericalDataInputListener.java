package com.mercury1089.scoutingapp2025.listeners;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.concurrent.ConcurrentHashMap;

public class NumericalDataInputListener implements View.OnClickListener {
    private final TextView counterView;
    private ConcurrentHashMap<String, String> map;
    private final String key;
    public NumericalDataInputListener(TextView counterView, ConcurrentHashMap<String, String> map, String key) {
        this.counterView = counterView;
        this.map = map;
        this.key = key;
    }
    @Override
    public void onClick(View v) {
        int currentCount = Integer.parseInt((String) counterView.getText());
        currentCount++;
        map.put(key, String.valueOf(currentCount));
        updateXMLObjects();
    }
}
