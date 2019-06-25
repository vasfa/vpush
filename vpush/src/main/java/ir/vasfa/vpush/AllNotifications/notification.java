package ir.vasfa.vpush.AllNotifications;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;


import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import ir.vasfa.vpush.R;
import ir.vasfa.vpush.model.FirebaseNotificationDTO;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.POWER_SERVICE;

/**
 * Created by vali on 2018-02-28.
 */

public class notification {
    Context main_context;

    public void All_Notification_Those_Are_Only_Text(Context main_context,
                                                     String DataItems,
                                                     String NotificationLogo
    ) {
        this.main_context = main_context;

        if (NotificationLogo.equals("")) {
            _Only_Text(DataItems, null);
        } else {
            new sendNotification(main_context, DataItems)
                    .execute(NotificationLogo);
        }

    }

    public void All_Notification_Those_Are_Image(Context main_context,
                                                 String DataItems,
                                                 String NotificationBigImage
    ) {
        this.main_context = main_context;

        new generatePictureStyleNotification(main_context, DataItems).execute(NotificationBigImage);

    }


    private void _Only_Text(String DataItems, Bitmap bitmap) {
        //retDi.DS.get(0)-->notificationId
        //retDi.DS.get(1)-->notifyType
        //retDi.DS.get(2)-->title
        //retDi.DS.get(3)-->small Message
        //retDi.DS.get(4)-->big Message
        //retDi.DS.get(5)-->Notification positive button title
        //retDi.DS.get(6)-->Notification negetive button title
        //retDi.DS.get(7)-->NotificationImage
        //retDi.DS.get(8)-->ExpiteDate

        //retDi.DB.get(0)-->turnOnScreen
        //retDi.DB.get(1)-->have positive button in notification
        //retDi.DB.get(2)-->have negetive button in notification
        //retDi.DB.get(3)-->Show notification


        FirebaseNotificationDTO retDi = null;
        Gson gson = new Gson();
        retDi = gson.fromJson(DataItems, FirebaseNotificationDTO.class);
        if (retDi.isTurnScreenOn()) {//turnOnScreen
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock screenLock = ((PowerManager) main_context.getSystemService(POWER_SERVICE)).newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            screenLock.acquire();
        }
        Notification_sound(main_context, null);
        Intent intent = null;
        switch (retDi.getNotify().getType() + "") {//notifyType

            case "1": {//open application
                //retDi.DS.get(9)-->packageName
                if (isAppInstalled(main_context, retDi.getNotify().getPackageName()))//packageName
                {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + retDi.getNotify().getPackageName())
                            , main_context, OnClickBroadcastReceivers.class);
                    intent.putExtra("TYPE", "1");
                    intent.putExtra("Content", retDi.getNotify().getPackageName());
                    intent.putExtra("noti_id", retDi.getId());
                    intent.putExtra("myclass", "");
                }
            }
            break;
            case "2": {//open site
                //retDi.DS.get(9)-->URL
                if (!retDi.getNotify().getUrl().startsWith("http://") && !retDi.getNotify().getUrl().startsWith("https://"))
                    retDi.getNotify().setUrl("http://" + retDi.getNotify().getUrl());
//                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(retDi.DS.get(5)), main_context, OnClickBroadcastReceivers.class);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(retDi.getNotify().getUrl()), main_context, OnClickBroadcastReceivers.class);
                intent.putExtra("TYPE", "2");
                intent.putExtra("Content", retDi.getNotify().getUrl());
                intent.putExtra("noti_id", retDi.getId());
                intent.putExtra("myclass", "");
            }
            break;
            case "3": {
                //retDi.DS.get(7)-->pName
                //retDi.DS.get(8)-->activityName
                //retDi.DL--->Activity Exteras
                if (retDi.getNotify().getPackageName().equals("") || retDi.getNotify().getActivityName().equals("")) {
                    return;
                } else {
                    Class myclass = null;
                    String paths = "";
                    paths = retDi.getNotify().getPackageName() + "." + retDi.getNotify().getActivityName();

                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""), main_context, OnClickBroadcastReceivers.class);
                    intent.putExtra("TYPE", "3");
                    intent.putExtra("Content", DataItems); //retDi.DL--->Activity Exteras
                    intent.putExtra("noti_id", retDi.getId());
                    intent.putExtra("myclass", paths);
                }


            }
            break;
            case "4": {
                //phone
                //retDi.DS.get(7)-->number

                String content = "";
                if (!retDi.getNotify().getNumber().equals(""))
                    content = retDi.getNotify().getNumber();
                else
                    content = retDi.getNotify().getUssdCode();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""), main_context, OnClickBroadcastReceivers.class);
                intent.putExtra("TYPE", "4");
                intent.putExtra("Content", content);
                intent.putExtra("noti_id", retDi.getId());
                intent.putExtra("myclass", "");
            }
            break;
            case "5": {
                //sms
                //retDi.DS.get(7)-->number
                //retDi.DS.get(8)-->message text

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""), main_context, OnClickBroadcastReceivers.class);
                intent.putExtra("TYPE", "5");
                intent.putExtra("Content", retDi.getNotify().getNumber());
                intent.putExtra("noti_id", retDi.getId());
                intent.putExtra("myclass", retDi.getNotify().getContent());
            }
            break;
            case "6": {
                //retDi.DS.get(7)-->emailAccoutTo
                //retDi.DS.get(8)-->emailAccoutCC
                //retDi.DS.get(9)-->emailTitle
                //retDi.DS.get(10)-->emailmessage
                //retDi.DS.get(11)-->emailChosserTitle

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""), main_context, OnClickBroadcastReceivers.class);
                intent.putExtra("TYPE", "6");
                intent.putExtra("Content", DataItems);
                intent.putExtra("noti_id", retDi.getId());
                intent.putExtra("myclass", "");
            }
            break;
            case "7": {
//                NotificationManager notificationManager = (NotificationManager) main_context.getApplicationContext()
//                        .getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.cancel(NOTIFICATIONID);

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""), main_context, OnClickBroadcastReceivers.class);
                intent.putExtra("TYPE", "7");
                intent.putExtra("Content", "");
                intent.putExtra("noti_id", retDi.getId());
                intent.putExtra("myclass", "");
            }
            break;
            case "8": {//this is for downloading file
//                NotificationManager notificationManager = (NotificationManager) main_context.getApplicationContext()
//                        .getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.cancel(NOTIFICATIONID);

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""), main_context, OnClickBroadcastReceivers.class);
                intent.putExtra("TYPE", "8");
                intent.putExtra("Content", DataItems);
                intent.putExtra("noti_id", retDi.getId());
                intent.putExtra("myclass", "");
            }
            break;


        }

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(main_context,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent deleteintent = new Intent(main_context, OnCancelBroadcastReceiver.class);
        PendingIntent deletependingIntent = PendingIntent.getBroadcast(main_context, 0, deleteintent, 0);

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "abc";// The user-visible name of the channel.
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= 26) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(main_context)
                .setAutoCancel(true)
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(main_context.getResources(), R.drawable.vl))
                .setContentTitle(retDi.getTitle())
                .setContentText(retDi.getSummeryMessage())
                .setContentIntent(pendingIntent)
                .setDeleteIntent(deletependingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setChannelId(CHANNEL_ID)
                .setVibrate(new long[]{500, 500, 700});


        if (bitmap != null)
            builder.setLargeIcon(bitmap);

        Actions(builder, DataItems);


        if (Build.VERSION.SDK_INT >= 21) builder.setVibrate(new long[0]);

        CharSequence cs = retDi.getLongMessage();
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(cs));

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) main_context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(retDi.getId(), notification);


    }


    private class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String DataItems;

        public sendNotification(Context context, String DataItems) {
            super();
            this.ctx = context;
            this.DataItems = DataItems;

        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {

                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            try {
                _Only_Text(DataItems, result);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /////////////////////////////////
    /////////////////////////////////


    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        String DataItems;


        public generatePictureStyleNotification(Context context, String DataItems) {
            super();
            this.mContext = context;
            this.DataItems = DataItems;

        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            {
                try {
                    //retDi.DS.get(0)-->notificationId
                    //retDi.DS.get(1)-->notifyType
                    //retDi.DS.get(2)-->title
                    //retDi.DS.get(3)-->small Message
                    //retDi.DS.get(4)-->big Message
                    //retDi.DS.get(5)-->Notification positive button title
                    //retDi.DS.get(6)-->Notification negetive button title

                    //retDi.DB.get(0)-->turnOnScreen
                    //retDi.DB.get(1)-->have positive button in notification
                    //retDi.DB.get(2)-->have negetive button in notification
                    //retDi.DB.get(3)-->Show notification


                    FirebaseNotificationDTO retDi = null;
                    Gson gson = new Gson();
                    retDi = gson.fromJson(DataItems, FirebaseNotificationDTO.class);

                    if (retDi.isTurnScreenOn()) {//turnOnScreen
                        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock screenLock = ((PowerManager) main_context.getSystemService(POWER_SERVICE)).newWakeLock(
                                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                        screenLock.acquire();
                    }
                    Notification_sound(main_context, null);
                    Intent intent = null;
                    switch (retDi.getNotify().getType() + "") {//notifyType

                        case "1": {//open application
                            //retDi.DS.get(9)-->packageName
                            if (isAppInstalled(main_context, retDi.getNotify().getPackageName()))//packageName
                            {
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + retDi.getNotify().getPackageName())
                                        , main_context, OnClickBroadcastReceivers.class);
                                intent.putExtra("TYPE", "1");
                                intent.putExtra("Content", retDi.getNotify().getPackageName());
                                intent.putExtra("noti_id", retDi.getId());
                                intent.putExtra("myclass", "");
                            }
                        }
                        break;
                        case "2": {//open site
                            //retDi.DS.get(9)-->URL
                            if (!retDi.getNotify().getUrl().startsWith("http://") && !retDi.getNotify().getUrl().startsWith("https://"))
                                retDi.getNotify().setUrl("http://" + retDi.getNotify().getUrl());
//                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(retDi.DS.get(5)), main_context, OnClickBroadcastReceivers.class);
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(retDi.getNotify().getUrl())
                                    , main_context, OnClickBroadcastReceivers.class);
                            intent.putExtra("TYPE", "2");
                            intent.putExtra("Content", retDi.getNotify().getUrl());
                            intent.putExtra("noti_id", retDi.getId());
                            intent.putExtra("myclass", "");
                        }
                        break;
                        case "3": {
                            //retDi.DS.get(7)-->pName
                            //retDi.DS.get(8)-->activityName
                            //retDi.DL--->Activity Exteras
                            if (retDi.getNotify().getPackageName().equals("") || retDi.getNotify().getActivityName().equals("")) {
                                return;
                            } else {
                                Class myclass = null;
                                String paths = "";
                                paths = retDi.getNotify().getPackageName() + "." + retDi.getNotify().getActivityName();

                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""), main_context, OnClickBroadcastReceivers.class);
                                intent.putExtra("TYPE", "3");
                                intent.putExtra("Content", DataItems); //retDi.DL--->Activity Exteras
                                intent.putExtra("noti_id", retDi.getId());
                                intent.putExtra("myclass", paths);
                            }


                        }
                        break;
                        case "4": {
                            //retDi.DS.get(7)-->number
                            String content = "";
                            if (!retDi.getNotify().getNumber().equals(""))
                                content = retDi.getNotify().getNumber();
                            else
                                content = retDi.getNotify().getUssdCode();
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""), main_context, OnClickBroadcastReceivers.class);
                            intent.putExtra("TYPE", "4");
                            intent.putExtra("Content", content);
                            intent.putExtra("noti_id", retDi.getId());
                            intent.putExtra("myclass", "");
                        }
                        break;
                        case "5": {
                            //retDi.DS.get(7)-->number
                            //retDi.DS.get(8)-->message text

                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""), main_context, OnClickBroadcastReceivers.class);
                            intent.putExtra("TYPE", "5");
                            intent.putExtra("Content", retDi.getNotify().getNumber());
                            intent.putExtra("noti_id", retDi.getId());
                            intent.putExtra("myclass", retDi.getNotify().getContent());
                        }
                        break;
                        case "6": {
                            //retDi.DS.get(7)-->emailAccoutTo
                            //retDi.DS.get(8)-->emailAccoutCC
                            //retDi.DS.get(9)-->emailTitle
                            //retDi.DS.get(10)-->emailmessage
                            //retDi.DS.get(11)-->emailChosserTitle

                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""), main_context, OnClickBroadcastReceivers.class);
                            intent.putExtra("TYPE", "6");
                            intent.putExtra("Content", DataItems);
                            intent.putExtra("noti_id", retDi.getId());
                            intent.putExtra("myclass", "");
                        }
                        break;
                        case "7": {
//                NotificationManager notificationManager = (NotificationManager) main_context.getApplicationContext()
//                        .getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.cancel(NOTIFICATIONID);

                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""), main_context, OnClickBroadcastReceivers.class);
                            intent.putExtra("TYPE", "7");
                            intent.putExtra("Content", "");
                            intent.putExtra("noti_id", retDi.getId());
                            intent.putExtra("myclass", "");
                        }
                        break;
                        case "8": {//this is for downloading file
//                NotificationManager notificationManager = (NotificationManager) main_context.getApplicationContext()
//                        .getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.cancel(NOTIFICATIONID);

                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""), main_context, OnClickBroadcastReceivers.class);
                            intent.putExtra("TYPE", "8");
                            intent.putExtra("Content", retDi.getNotify().getUrl());
                            intent.putExtra("noti_id", retDi.getId());
                            intent.putExtra("myclass", "");
                        }
                        break;


                    }


                    Intent deleteintent = new Intent(main_context, OnCancelBroadcastReceiver.class);
                    PendingIntent deletependingIntent = PendingIntent.getBroadcast(main_context, 0, deleteintent, 0);


                    PendingIntent pendingIntent =
                            PendingIntent.getBroadcast(main_context,
                                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    final RemoteViews remoteViews = new RemoteViews(main_context.getPackageName(), R.layout.custome_notif);
                    RemoteViews rv = new RemoteViews(main_context.getPackageName(), R.layout.second_costume_notif);

                    remoteViews.setTextViewText(R.id.nt1, "");
                    remoteViews.setTextViewText(R.id.nt2, "");
                    remoteViews.setImageViewResource(R.id.ni1, R.drawable.vl);
                    rv.setTextViewText(R.id.n2t, retDi.getLongMessage());
                    rv.setTextViewText(R.id.n22t, retDi.getTitle());
                    rv.setImageViewBitmap(R.id.n2i, result);

                    String CHANNEL_ID = "my_channel_01";// The id of the channel.
                    CharSequence name = "abc";// The user-visible name of the channel.
                    NotificationChannel mChannel = null;
                    if (Build.VERSION.SDK_INT >= 26) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    }

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(main_context)
                                    .setSmallIcon(getNotificationIcon())
                                    .setContentTitle(retDi.getTitle())
                                    .setContentText(retDi.getSummeryMessage())
                                    .setCustomBigContentView(remoteViews)
                                    .setContentIntent(pendingIntent)
                                    .setDeleteIntent(deletependingIntent)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setChannelId(CHANNEL_ID)
                                    .setAutoCancel(true);

                    Actions(mBuilder, DataItems);


                    if (Build.VERSION.SDK_INT >= 21)
                        mBuilder.setVibrate(new long[0]);

                    final Notification notification = mBuilder.build();
                    if (Build.VERSION.SDK_INT >= 16) {
                        notification.bigContentView = rv;
                    }
                    NotificationManager mNotificationManager
                            = (NotificationManager) main_context.getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= 26) {
                        mNotificationManager.createNotificationChannel(mChannel);
                    }
                    mNotificationManager.notify(retDi.getId(), notification);

                } catch (Exception ex) {
                    String asd = "";
                }

            }
        }
    }

    public void Actions(NotificationCompat.Builder action_builder,
                        String data_items) {

        FirebaseNotificationDTO retDi = null;
        Gson gson = new Gson();
        retDi = gson.fromJson(data_items, FirebaseNotificationDTO.class);


        //Yes intent
        if (retDi.getButtonOne()) {
            Intent yesReceive = new Intent();
            yesReceive.putExtra("DataItems", data_items);
            yesReceive.putExtra("NotificationId", retDi.getId() + "");
            yesReceive.setAction("ACTION1");
            PendingIntent pendingIntentYes = PendingIntent.getBroadcast(main_context,
                    12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
//            int icon=Integer.parseInt("android.R.drawable.ic_input_add");

            action_builder.addAction(android.R.drawable.ic_input_add, retDi.getButtonTitleOne(), pendingIntentYes);
        }


//No intent
        if (retDi.getButtonTwo()) {
            Intent noReceive = new Intent();
            noReceive.putExtra("NotificationId", retDi.getId() + "");
            noReceive.setAction("ACTION1");
//            noReceive.setAction("ACTION2");
            PendingIntent pendingIntentNo = PendingIntent.getBroadcast(main_context
                    , 12345, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);
//            int icon=Integer.parseInt("android.R.drawable.ic_delete");
            action_builder.addAction(android.R.drawable.ic_delete, retDi.getButtonTitleTwo(), pendingIntentNo);
        }

        if (retDi.getButtonThree()) {
            Intent noReceive = new Intent();
            noReceive.putExtra("NotificationId", retDi.getId() + "");
            noReceive.setAction("ACTION1");
//            noReceive.setAction("ACTION3");
            PendingIntent pendingIntentNo = PendingIntent.getBroadcast(main_context
                    , 12345, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);
//            int isConnected=Integer.parseInt("android.R.drawable.ic_menu_call");
            action_builder.addAction(android.R.drawable.ic_menu_call, retDi.getButtonTitleThree(), pendingIntentNo);
        }

    }

    //////////////////////////////////////
    ////////////////////////////////////////

    private int getNotificationIcon() {//false-->color notification   true---->black and white notification
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M);
        return useWhiteIcon ? R.drawable.vl : R.drawable.vl;
    }

    public void Notification_sound(Context context, File SountPath) {
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


    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            String asdsa = "";
        }
        return false;
    }


}
