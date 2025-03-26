package com.mercury1089.scoutingapp2025.api.network;

import io.github.cdimascio.dotenv.Dotenv;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String API_URL = "https://www.thebluealliance.com/api/v3/";
    private static Retrofit client;
    private static MatchService matchService;

    public synchronized static void initClient() {
        if (client == null) {
            client = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            matchService = client.create(MatchService.class);
        }
    }
    public static Retrofit getClient() {
        if (client == null) {
            initClient();
        }
        return client;
    }

    public static MatchService getMatchService() {
        initClient();
        return matchService;
    }
}
