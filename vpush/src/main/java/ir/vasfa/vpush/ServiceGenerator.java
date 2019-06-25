package ir.vasfa.vpush;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class ServiceGenerator {
    // ۱
    public static final String API_BASE_URL =
//            "http://79.175.151.98:8010";
            "https://api.bepors.me";
//            "http://79.175.138.119:8010/";

    // ۲
    private static OkHttpClient httpClient =
            new OkHttpClient.Builder().build();

    // ۳
    private static Retrofit.Builder builder;

    static {


        builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL);
    }

    // ۴
    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}
