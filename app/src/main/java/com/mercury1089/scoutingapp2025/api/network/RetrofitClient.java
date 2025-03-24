package com.mercury1089.scoutingapp2025.api.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String API_URL = "https://www.thebluealliance.com/api/v3/";
    private static Retrofit client;
    private static MatchService matchService;

    public static Retrofit getClient() {
        if (client == null) {
            synchronized(RetrofitClient.class) {
                client = new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                matchService = client.create(MatchService.class);
            }
        }
        return client;
    }
}
