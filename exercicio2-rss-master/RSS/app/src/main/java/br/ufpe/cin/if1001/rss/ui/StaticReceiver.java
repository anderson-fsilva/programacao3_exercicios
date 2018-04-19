package br.ufpe.cin.if1001.rss.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class StaticReceiver extends BroadcastReceiver {
    private final String TAG = "StaticReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "INTENT Recebido pelo StaticReceiver", Toast.LENGTH_LONG).show();
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }
}
