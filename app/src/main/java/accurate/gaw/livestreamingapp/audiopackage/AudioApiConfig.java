package accurate.gaw.livestreamingapp.audiopackage;



import java.util.Map;

import accurate.gaw.livestreamingapp.ServerResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;


public interface AudioApiConfig {

    @Multipart
    @POST("shwe_wala/api/audio_list.php")
    Call<ServerResponse> upload(
            /*@Part("action")String actiion,
            @Part("email")String email,
            @Part  MultipartBody.Part video*/
            //@Header("Authorization") String authorization,
            @PartMap Map<String, RequestBody> map
    );

    @Multipart
    @POST("shwe_wala/api/add_video.php")
    Call<ServerResponse> upload2(
            @Part("action")RequestBody actiion,
            @Part("email")RequestBody email,
            @Part  MultipartBody.Part video
            //@Header("Authorization") String authorization,
            //@PartMap Map<String, RequestBody> map
    );

    @Multipart
    @POST("shwe_wala/api/audio_list.php")
    Call<ServerResponse> upload3(
            @Part("action")RequestBody actiion,
            @Part("email")RequestBody email
            //@Part  MultipartBody.Part video
            //@Header("Authorization") String authorization,
            //@PartMap Map<String, RequestBody> map
    );
}