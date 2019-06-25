package ir.vasfa.vpush.Connection;

/**
 * Created by Vn on 28/03/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkManager extends BroadcastReceiver {

    // LOG
    protected static final String   TAG                 = NetworkManager.class.getSimpleName();
    // DATA
    protected Context               mContext;
    protected boolean               mNoConnectivity;
    protected String                mReason;
    protected boolean               mIsFailover;
    protected static boolean        mIsConnected        = false;
    protected static boolean        mIsConnectivityGood = true;

    public NetworkManager(Context context) {
        this.mContext = context;
    }

    public void registerReceivers() {
        mContext.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        try {
            NetworkInfo info = getNetworkInfo(context);
            return (info != null && info.isConnected());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = getNetworkInfo(context);

        return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
    }

    protected static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {

            return true;
        }
        else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mNoConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        mReason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
        mIsFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
        //
        if (mNoConnectivity) {
            mIsConnected = false;
        }
        else {
            if (isConnectedFast(mContext)) {
                mIsConnectivityGood = true;
            }
            else {
                mIsConnectivityGood = false;
            }
            mIsConnected = true;
        }
    }
}
