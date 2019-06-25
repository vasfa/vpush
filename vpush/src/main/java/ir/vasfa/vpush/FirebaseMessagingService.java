package ir.vasfa.vpush;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


import ir.vasfa.vpush.AllNotifications.MessageReceiver;
import ir.vasfa.vpush.Connection.NetworkManager;
import ir.vasfa.vpush.api.MessageControllerApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vali on 2017-05-31.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    String DataItem = "";
    private SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        try{

            sharedPreferences = getSharedPreferences("VASFAPUSH_DATA", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("fireBaseToken", token);
            editor.commit();

            intentMessageOther(token);
        }catch (Exception ex)
        {
        }

//        sendRegistrationToServer(token);
    }

    private void intentMessageOther(String token) {

        try{

            Intent sharingIntent=new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, token);
            startActivity(Intent.createChooser(sharingIntent, "به اشتراک گذاشتن با"));

        }catch (Exception ex)
        {

        }

    }

    private void sendRegistrationToServer(String token) {
        try {
            if (NetworkManager.isConnected(getApplicationContext()))
            {
                sharedPreferences = getSharedPreferences("VASFAPUSH_DATA", Context.MODE_PRIVATE);
                String person_id = sharedPreferences.getString("person_id", "");
                if(person_id!=null && token!=null && !person_id.equals("") && !token.equals(""))
                {
                    MessageControllerApi client = ServiceGenerator.createService(MessageControllerApi.class);
                    Call<ResponseBody> call =
                            client.firebasetokenUsingPut(person_id,"user",token);
                    call.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                String asd="";
                            }else{
                                String asd="";
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            String asd="";
                        }
                    });
                }
            }
        } catch (Exception ex) {
            String asd="";
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        DataItem = data.get("data");
        MessageReceiver messageReceiver=new MessageReceiver();
        messageReceiver.mReceiver(getApplicationContext(),DataItem);
    }


}
