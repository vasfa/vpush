package ir.vasfa.vpush.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface MessageControllerApi {

    @PUT("/api/mobile/authentication/firebase")
    Call<ResponseBody> firebasetokenUsingPut(
            @Query("id") String personid,
            @Query("type") String type,
            @Query("firebase-token") String firebaseToken
    );
}
