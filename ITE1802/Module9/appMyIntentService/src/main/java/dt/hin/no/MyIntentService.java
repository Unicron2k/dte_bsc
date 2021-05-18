package dt.hin.no;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import android.widget.Toast;

public class MyIntentService extends IntentService {
	private NotificationManager notificationManager;

	//Bruker en Handler for å kunne vise resultatet i en Toast
	private Handler handler = new Handler();

	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager =(NotificationManager)(getApplicationContext().getSystemService(NOTIFICATION_SERVICE));
		displayNotificationMessage("Kjører bakgrunnstjeneste...");
	}

    //Konstruktør er påkrevd. Må kalle super(...).
	public MyIntentService() {
		super("MyIntentService");
	}

    //Her legger man "arbeidet" som skal gjøres. Kjører i egen tråd.
    //Avslutter når ferdig og ingen flere kall som må håndteres.
	@Override
	protected void onHandleIntent(Intent intent) {
		// Her "sover" vi i 5 sekunder for å simulere litt "arbeid":
		//endTime indikerer hvor lenge tråden skal "sove":
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//Vis resutatet:
		handler.post(doShowResult);
	}

	//Kjøres av GUI-tråden, en Toast må fremvises av GUI-tråden:
	private Runnable doShowResult = new Runnable() {
		public void run() {
			Context context = MyIntentService.this.getApplicationContext();
			Toast.makeText(context, "Oppdrag fullført!!!!", Toast.LENGTH_SHORT).show();
		}
	};
	
	@Override
	public void onDestroy() {

		// TODO Auto-generated method stub
		Context context = this.getApplicationContext();
		//Toast.makeText(context, "Avslutter service", Toast.LENGTH_LONG).show();

		//Fjerner notification:
		notificationManager.cancel(0); //.cancelAll();
		super.onDestroy();
	}

	private void displayNotificationMessage(String message)
	{
		// Nytt! For API >= 26:

		String CHANNEL_ID = "my_channel_01";
		int NOTIFICATION_ID = 0;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			CharSequence name = "my_channel";
			String Description = "This is my channel";
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
			mChannel.setDescription(Description);
			mChannel.enableLights(true);
			mChannel.setLightColor(Color.RED);
			mChannel.enableVibration(true);
			mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
			mChannel.setShowBadge(false);
			notificationManager.createNotificationChannel(mChannel);
		}

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
				.setSmallIcon(R.drawable.image2)
				.setContentTitle("Ting skjer!!!")
				.setContentText(message);

		Intent resultIntent = new Intent(this, MyActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		//stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setContentIntent(resultPendingIntent);

		notificationManager.notify(NOTIFICATION_ID, builder.build());
	}
}
