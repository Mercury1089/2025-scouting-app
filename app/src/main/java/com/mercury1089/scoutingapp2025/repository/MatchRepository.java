package com.mercury1089.scoutingapp2025.repository;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.mercury1089.scoutingapp2025.api.model.ApiMatch;
import com.mercury1089.scoutingapp2025.api.network.MatchService;
import com.mercury1089.scoutingapp2025.api.network.RetrofitClient;
import com.mercury1089.scoutingapp2025.api.util.ApiUtils;
import com.mercury1089.scoutingapp2025.database.MatchDatabase;
import com.mercury1089.scoutingapp2025.database.dao.MatchDataAccessObject;
import com.mercury1089.scoutingapp2025.database.model.Match;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// Connects match API data and the local SQL database
public class MatchRepository {
    private final MatchService matchService;
    private final MatchDatabase database;
    private final ExecutorService executorService;
    public MatchRepository(Context context) {
        matchService = RetrofitClient.getMatchService();
        database = MatchDatabase.getInstance(context);
        executorService = Executors.newSingleThreadExecutor();
    }
    public void storeMatchesByEvent(String eventKey) {
        MatchDataAccessObject dao = database.matchDao();
        executorService.execute(() -> {
            matchService.fetchAllMatchesByEvent(ApiUtils.getApiAuthorization(), eventKey).enqueue(new Callback<List<ApiMatch>>() {
                // Callback functions are assumed by Android to be run on the main thread so
                // Database operations have to explicitly be in an executorService.execute() block
                // So that they run on a background thread instead
                @Override
                public void onResponse(Call<List<ApiMatch>> call, Response<List<ApiMatch>> response) {
                    if (response.isSuccessful()) {
                        List<Match> matches = new ArrayList<>();
                        for (ApiMatch m : response.body()) {
                            Match databaseMatch = new Match.Builder()
                                    .setMatchKey(m.getKey())
                                    .setRedAllianceTeams(String.join(",", m.getAlliances().getRedAlliance().getTeamKeys()))
                                    .setBlueAllianceTeams(String.join(",", m.getAlliances().getBlueAlliance().getTeamKeys()))
                                    .build();
                            matches.add(databaseMatch);
                        }
                        // Makes sure that the database operation is run on a background thread
                        executorService.execute(() -> dao.storeMatches(matches));
                    } else {
                        try {
                            assert response.errorBody() != null;
                            Log.d("MatchRepository", response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<ApiMatch>> call, Throwable throwable) {
                    Log.d("MatchRepository", "Network Error :: " + throwable.getLocalizedMessage());
                }
            });

        });
    }


}
