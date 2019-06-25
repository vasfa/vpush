package ir.vasfa.vpush.AllNotifications;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;

//import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ir.vasfa.vpush.R;
import ir.vasfa.vpush.model.FirebaseNotificationDTO;


public class MessageReceiver {

    String DataItem = "";
    String title = "";
    String Content = "";
    String imageurl = "";
    int NOTIFICATIONID = -1;
    String ExpireDateTime = "";

    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;
    String Responser_Name = "", Responser_Id = "";

    Context mContext;

    public void mReceiver(Context context, String DataItem) {

        try {

            if(DataItem.equals(""))
            {

                NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.vl) // notification icon
                        .setContentTitle("Notification!") // title for notification
                        .setContentText("Hello word") // message for notification
                        .setAutoCancel(true); // clear notification after click

                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
                return;
            }else{
                mContext = context;
                final notification notification = new notification();
                try {

                    sharedPreferences = context.getSharedPreferences("VASFAPUSH_DATA", Context.MODE_PRIVATE);
                    String Total_Notification = sharedPreferences.getString("Total_Notification", "0");

                    {
                        FirebaseNotificationDTO retDi = new FirebaseNotificationDTO();
//                        try {
//                            final ObjectMapper om = new ObjectMapper();
//                            retDi = om.readValue(DataItem, FirebaseNotificationDTO.class);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        ExpireDateTime = retDi.getDate() + "";
                        String not_ides = sharedPreferences.getString("not_ides", "");
                        String not_timing = sharedPreferences.getString("not_timing", "");
                        if (!ExpireDateTime.equals("")) {
                            if (not_ides.equals("")) {
                                not_ides = NOTIFICATIONID + "";
                                not_timing = ExpireDateTime + "";
                            } else {
                                not_ides += ";" + NOTIFICATIONID + "";
                                not_timing += ";" + ExpireDateTime + "";
                            }

                            editor = sharedPreferences.edit();
                            editor.putString("not_ides", not_ides);
                            editor.putString("not_timing", not_timing);
                            editor.commit();
                        }

                        if (!DismissNotification(retDi.getId() + "")) {
                            String Id = retDi.getType();
                            if (Id.equals("1"))//this is Text Notification
                            {
                                if (retDi.isShowActivity()) {
                                    ShowActivity(Total_Notification, retDi);
                                }
                                if (retDi.isShow())
                                    notification.All_Notification_Those_Are_Only_Text(context, DataItem, retDi.getIcon());
                            } else if (Id.equals("2"))//this is Image Notification
                            {
                                if (retDi.isShowActivity()) {
                                    ShowActivity(Total_Notification, retDi);
                                }
                                if (retDi.isShow())
                                    notification.All_Notification_Those_Are_Image(context, DataItem, retDi.getImage());
                            }
                        }

                    }


                } catch (Exception ex) {
                    String error = "";
                }

            }




        } catch (Exception ex) {

        }


    }

//    public static String BROADCAST_ACTION =
//            "com.unitedcoders.android.broadcasttest.SHOWTOAST";
//
//    public void sendBroadcast() {
//        Intent broadcast = new Intent();
//        broadcast.setAction(BROADCAST_ACTION);
//        broadcast.addCategory(Intent.CATEGORY_DEFAULT);
//        sendBroadcast(broadcast);
//
//    }


    public void ShowActivity(String Total_Notifications, FirebaseNotificationDTO dataItems) {
        try {

            String SubId = dataItems.getActivity().getType() + "";//dataItems.DL.get(0).Id;
            if (SubId.equals("3")) {//DontMissActivity
//                Intent intent = new Intent(getApplicationContext(), DontMissActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("NotificationId", dataItems.getId());
//                startActivity(intent);


            } else if (SubId.equals("4")) {//be special user
//                Setting.S_Content = Content;
//
//                Intent intent = new Intent(getApplicationContext(), BeSpecialUserActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("Content", dataItems.getActivity().getContent());
//                intent.putExtra("NotificationId", dataItems.getId());
//                startActivity(intent);


            } else if (SubId.equals("5")) {// پاسخ دهنده سوال کاربر را جواب می دهد یا ندهد یا رد کند

//                int a = Integer.parseInt(Total_Notifications);
//                a++;
//                editor = sharedPreferences.edit();
//                editor.putString("Total_Notification", a + "");
//                editor.commit();
//                sendBroadcast();
//
//                Setting.S_Content = dataItems.getActivity().getContent();
//
//                Intent intent = new Intent(getApplicationContext(), NotificationResponseActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("Content", dataItems.getActivity().getContent());
//                intent.putExtra("name", dataItems.getActivity().getResponderName());
//                intent.putExtra("Responser_Id",dataItems.getActivity().getResponderID());
//                intent.putExtra("Question_Id",dataItems.getActivity().getQuestionID());
//                intent.putExtra("Question_Status",dataItems.getActivity().getQuestionStatus());
//                intent.putExtra("TYPE", "1");
//                intent.putExtra("NotificationId", dataItems.getId());
//                startActivity(intent);

            } else if (SubId.equals("6")) {//پیغام از طرف تیم بپرس برای کاربر ارسال شده باشد

//                int a = Integer.parseInt(Total_Notifications);
//                a++;
//                editor = sharedPreferences.edit();
//                editor.putString("Total_Notification", a + "");
//                editor.commit();
//                sendBroadcast();
//
//                Setting.S_Content = dataItems.getActivity().getContent();
//
//                Intent intent = new Intent(getApplicationContext(), NotificationResponseActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("Content", dataItems.getActivity().getContent());
//                intent.putExtra("name", "");
//                intent.putExtra("Responser_Id","");
//                intent.putExtra("Question_Id","");
//                intent.putExtra("Question_Status","");
//                intent.putExtra("TYPE", "2");
//                intent.putExtra("NotificationId", dataItems.getId());
//                startActivity(intent);


            } else if (SubId.equals("7")) {//Update Auto -----------> only_text
//                Setting.S_Content = dataItems.getActivity().getContent();
//
//                Intent intent = new Intent(getApplicationContext(), NotificationUpdateActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("Content", dataItems.getActivity().getContent());
//                intent.putExtra("TYPE", "2");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.putExtra("NotificationId", dataItems.getId());
//                startActivity(intent);


            } else if (SubId.equals("8")) {//Update Manual -----------> only_text
//                Setting.S_Content = dataItems.getActivity().getContent();
//
//                Intent intent = new Intent(getApplicationContext(), NotificationUpdateActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("Content", dataItems.getActivity().getContent());
//                intent.putExtra("TYPE", "1");
//                intent.putExtra("NotificationId", dataItems.getId());
//                startActivity(intent);

            } else if (SubId.equals("9")) {

//                Intent POPUPintent = new Intent(getApplicationContext(), MY_CONTENT_POPUP_Activity.class);
//                POPUPintent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                POPUPintent.putExtra("IMAGEURL", dataItems.getActivity().getImageURL());
//                POPUPintent.putExtra("CONTENT", dataItems.getActivity().getContent());
//                POPUPintent.putExtra("LINKTOSHOW", dataItems.getActivity().getLinkToShowSite());
//                POPUPintent.putExtra("NotificationId", dataItems.getId());
//                startActivity(POPUPintent);
            }


        } catch (Exception ex) {

        }
    }


    public boolean DismissNotification(String noti_id) {
        try {
            boolean dismissNotifi = false;
            sharedPreferences = mContext.getSharedPreferences("VASFAPUSH_DATA", Context.MODE_PRIVATE);
            String not_ides = sharedPreferences.getString("not_ides", "");
            String not_timing = sharedPreferences.getString("not_timing", "");
            if (!not_ides.equals("")) {

                String[] iid = not_ides.split(";");
                String[] itiming = not_timing.split(";");
                ArrayList<String> dat = new ArrayList<>();
                int pos = -1;
                int main_pos = -1;
                for (String data : iid) {
                    pos++;
                    if (data.equals(noti_id)) {
                        main_pos = pos;
                        break;
                    }

                }

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String newdate = dateFormat.format(date);
                long timeInMilliseconds = 0L;
                try {
                    Date mDate = dateFormat.parse(newdate);
                    timeInMilliseconds = mDate.getTime();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (main_pos != -1) {
                    if (Long.parseLong(itiming[pos]) < Long.parseLong(timeInMilliseconds + "")) {
                        NotificationManager notificationManager =
                                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(Integer.parseInt(noti_id));

                        dismissNotifi = true;
                        String n_noti_id_new = "";
                        String n_timing_new = "";
                        int fake_pos = -1;
                        for (String data : iid) {
                            fake_pos++;
                            if (main_pos != fake_pos) {
                                if (n_noti_id_new.equals(""))
                                    n_noti_id_new = data;
                                else
                                    n_noti_id_new += ";" + data;
                            }

                        }

                        fake_pos = -1;
                        for (String data : itiming) {
                            fake_pos++;
                            if (main_pos != fake_pos) {
                                if (n_timing_new.equals(""))
                                    n_timing_new = data;
                                else
                                    n_timing_new += ";" + data;
                            }

                        }

                        editor = sharedPreferences.edit();
                        editor.putString("not_ides", n_noti_id_new);
                        editor.putString("not_timing", n_timing_new);
                        editor.commit();

                        return dismissNotifi;
                    } else {
                        return dismissNotifi;
                    }
                } else {
                    return dismissNotifi;
                }


            } else {
                return dismissNotifi;
            }
        } catch (Exception ex) {
            return false;
        }
    }


    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void Notification_sound(Context context) {
        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, defaultRingtoneUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
