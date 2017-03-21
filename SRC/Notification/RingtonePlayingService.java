package na.palarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by K2 on 3/14/2017.
 */

public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    int startId;
    boolean isRunning;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId){

        Log.i("LocalService", "Received start Id" + startId + ": " + intent);

        String state = intent.getExtras().getString("extra");

        Log.e("Ringtone extra is: ",state);

        NotificationManager notify_manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0, intent_main_activity, 0);
        Notification notfication_popup = new NotificationCompat.Builder(this)
                .setContentTitle("Alarm going OFF")
                .setContentText("Click me!")
                .setContentIntent(pending_intent_main_activity)
                .setAutoCancel(true)
                .build();

        notify_manager.notify(0, notfication_popup);

        assert state != null;

        switch (state){
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;

        }

        if(!this.isRunning && startId == 1){

            Log.e("there is no music", "and want to start");
            media_song = MediaPlayer.create(this, R.raw.ringtone);
            media_song.start();

            this.isRunning = true;
            this.startId = 0;

        }
        else if(this.isRunning && startId == 0){

            Log.e("there is music", "and you want to end");

            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;

        }
        else if(!this.isRunning && startId == 0){

            Log.e("there is no music", "and you want to end");

            this.isRunning = false;
            this.startId = 0;
        }
        else if(this.isRunning && startId == 1){
            Log.e("there is music", "and you want to start");

            this.isRunning = true;
            this.startId = 1;

        }
        else {
            Log.e("else", "somehow you reached here");

        }


       return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){

       Log.e("On Destroy called","ta da");
        super.onDestroy();
        this.isRunning = false;

    }

}
