package com.mercury1089.scoutingapp2025.repository;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.mercury1089.scoutingapp2025.api.network.MatchService;
import com.mercury1089.scoutingapp2025.api.network.RetrofitClient;
import com.mercury1089.scoutingapp2025.database.MatchDatabase;
import com.mercury1089.scoutingapp2025.database.dao.MatchDataAccessObject;
import com.mercury1089.scoutingapp2025.database.model.Match;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Connects match API data and the local SQL database
public class MatchRepository {
    private final MatchService matchService;
    private final MatchDatabase database;
    public MatchRepository(Context context) {
        matchService = RetrofitClient.getMatchService();
        database = MatchDatabase.getInstance(context);
    }
    public void storeMatchesByEvent(String eventKey) {
        MatchDataAccessObject dao = database.matchDao();
        matchService.fetchAllMatchesByEvent(eventKey).enqueue(new Callback<List<Match>>() {

            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body());
                } else {
                    try {
                        Log.d("MatchRepository", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable throwable) {
                Log.d("MatchRepository", "Network Error :: " + throwable.getLocalizedMessage());
            }
        });


    }
}
