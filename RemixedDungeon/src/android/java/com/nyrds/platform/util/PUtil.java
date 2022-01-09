package com.nyrds.platform.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nyrds.platform.game.Game;

public class PUtil {
    static public boolean isConnectedToInternet() {
        boolean connectionStatus;

        ConnectivityManager connectivityManager
                = (ConnectivityManager) Game.instance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        connectionStatus = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return connectionStatus;
    }

    static public slog(String tag, String txt) {
        Log.i(tag, txt);
    }
}