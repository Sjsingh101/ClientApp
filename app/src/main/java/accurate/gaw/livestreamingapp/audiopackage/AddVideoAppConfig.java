package accurate.gaw.livestreamingapp.audiopackage;
import accurate.gaw.livestreamingapp.retrofitpack.retroconfig.AppConfig;
import retrofit2.Retrofit;


import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
public class AddVideoAppConfig {
    public static String BASE_URL = "http://test-demo.co.in/";

    private static final String URL = "http://test-demo.co.in/shwe_wala/";

    public static Retrofit getRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

