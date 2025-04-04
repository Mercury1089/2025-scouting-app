package com.mercury1089.scoutingapp2025.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.mercury1089.scoutingapp2025.api.model.ApiMatch;
import com.mercury1089.scoutingapp2025.api.network.MatchService;
import com.mercury1089.scoutingapp2025.api.network.RetrofitClient;
import com.mercury1089.scoutingapp2025.api.util.ApiUtils;
import com.mercury1089.scoutingapp2025.database.AppDatabase;
import com.mercury1089.scoutingapp2025.database.dao.MatchDataAccessObject;
import com.mercury1089.scoutingapp2025.database.dao.MetadataDataAccessObject;
import com.mercury1089.scoutingapp2025.database.model.Match;
import com.mercury1089.scoutingapp2025.database.model.Metadata;
import com.mercury1089.scoutingapp2025.database.util.DBUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Connects match API data and the local SQL database
public class MatchRepository {
    private Context context;
    private final MatchService matchService;
    private final AppDatabase database;
    private final ExecutorService executorService;
    public MatchRepository(Context context) {
        this.context = context;
        matchService = RetrofitClient.getMatchService();
        database = AppDatabase.getInstance(context);
        executorService = Executors.newSingleThreadExecutor();
    }
    public void storeMatchesByEvent(String eventKey) {
        if (!hasInternetConnection(context)) {
            Log.d("MR", "No internet connection");
            return;
        }
        MatchDataAccessObject dao = database.matchDao();
        MetadataDataAccessObject metaDao = database.metadataDao(); // For storing "last fetched" time
        executorService.execute(() -> {
            matchService.fetchAllMatchesByEvent(ApiUtils.getApiAuthorization(), eventKey).enqueue(new Callback<List<ApiMatch>>() {
                // Callback functions are assumed by Android to be run on the main thread so
                // Database operations have to explicitly run on a background thread
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
                            metaDao.upsertMetadata(
                                    new Metadata(DBUtil.LAST_API_FETCH_KEY, String.valueOf(System.currentTimeMillis()))
                            );
                        }
                        // Makes sure that the database operation is run on a background thread
                        executorService.execute(() -> dao.storeMatches(matches));
                    } else {
                        try {
                            assert response.errorBody() != null;
                            Log.d("MR", response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<ApiMatch>> call, Throwable throwable) {
                    Log.d("MR", "Network Error :: " + throwable.getLocalizedMessage());
                }
            });

        });
    }

    public Maybe<Match> getStoredMatch(String matchKey) {
        MatchDataAccessObject matchDao = database.matchDao();
        // Uses RxJava's Maybe for async database operations that may or may not return a result
        return Maybe.fromCallable(() -> matchDao.fetchByMatchKey(matchKey))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<List<Match>> getAllStoredMatches() {
        MatchDataAccessObject matchDao = database.matchDao();
        return Maybe.fromCallable(() -> matchDao.fetchAll())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<Long> getLastFetchedTime() {
        MetadataDataAccessObject metaDao = database.metadataDao();
        return Maybe.fromCallable(() -> Long.parseLong(metaDao.fetchMetadata(DBUtil.LAST_API_FETCH_KEY).getValue()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        if (network == null) return false;
        NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(network);
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }
}
