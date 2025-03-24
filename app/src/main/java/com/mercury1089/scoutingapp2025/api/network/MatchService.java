package com.mercury1089.scoutingapp2025.api.network;

import com.mercury1089.scoutingapp2025.database.model.Match;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MatchService {
    @GET("event/{matchKey}/matches/simple")
    Call<List<Match>> listMatches(@Path("matchKey") String matchKey);
}
