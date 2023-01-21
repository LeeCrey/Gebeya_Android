package com.example.online_ethio_gebeya.data;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.online_ethio_gebeya.R;
import com.example.online_ethio_gebeya.helpers.ApplicationHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConnectionUtil {
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";
    private static final String TAG = "ServiceGenerator";
    private static final long cacheSize = 5 * 1024 * 1024; // 5 MB
    private static Retrofit retrofit;

    public static synchronized Retrofit getRetrofitInstance(@NonNull Application application) {
        if (null == retrofit) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .followRedirects(false)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .cache(cache(application))
                    .addInterceptor(httpLoggingInterceptor()) // used if network off OR on
                    .addNetworkInterceptor(networkInterceptor()) // only used when network is on
                    .addInterceptor(offlineInterceptor(application))
                    .build();
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.WRAP_ROOT_VALUE);

            String finalUrl = application.getString(R.string.base_url);
            retrofit = new Retrofit.Builder()
                    .baseUrl(finalUrl)
                    .addConverterFactory(JacksonConverterFactory.create(mapper))
                    .client(client).build();
        }
        return retrofit;
    }

    private static Interceptor offlineInterceptor(@NonNull Context context) {
        return new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request();

                // prevent caching when network is on. For that we use the "networkInterceptor"
                if (!ApplicationHelper.isInternetAvailable(context)) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .removeHeader(HEADER_PRAGMA)
                            .removeHeader(HEADER_CACHE_CONTROL)
                            .cacheControl(cacheControl)
                            .build();
                }
                return chain.proceed(request);
            }
        };
    }

    private static Interceptor networkInterceptor() {
        return new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(5, TimeUnit.SECONDS)
                        .build();

                return response.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    private static HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(@NonNull String message) {
//                        Log.d(TAG, "log: http log: " + message);
                    }
                });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private static Cache cache(Application application) {
        return new Cache(new File(application.getCacheDir(), "someIdentifier"), cacheSize);
    }
}
