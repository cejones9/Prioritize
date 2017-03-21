package na.palarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by K2 on 3/14/2017.
 */

public class Alarm_Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("Yayy!", "We are in the Receiver");

        String get_your_string = intent.getExtras().getString("extra");
        Log.e("What is the key", get_your_string);

        Intent service_intent = new Intent(context, RingtonePlayingService.class);
        service_intent.putExtra("extra", get_your_string);
        context.startService(service_intent);


    }
}
