package dt.hin.no;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

//Nå også med egen tråd!
public class MyUnboundService extends Service {

	private Handler handler = new Handler(); //(Looper.getMainLooper());

	@Override
	public void onCreate() { 
        Log.d("MY_SERVICE", "onCreate()");
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Context context = this.getApplicationContext();
		Toast.makeText(context, "Starter service", Toast.LENGTH_SHORT).show();
		Log.d("MY_SERVICE", "onStartCommand(...)");
	
		// Flytter krevede prosessering til egen tråd:
		Thread thread = new Thread(null, doBackgroundThreadProcessing, "Background");
		thread.start();
		
		//START_STICKY sørger for at onStartCommand() kjører ved omstart (dvs. når service terminerer og starter på nytt ved ressursbehov):
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d("MY_SERVICE", "onDestroy(...)");		
		// TODO Auto-generated method stub
		Context context = this.getApplicationContext();
		Toast.makeText(context, "Stopper service", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	// Runnable som inneholder metoden som bakgrunnstråden starter:
	private Runnable doBackgroundThreadProcessing = new Runnable() {
	  public void run() {
	    backgroundThreadProcessing();
	  }
	};

	// Metoden som kjører i tråden.
	private void backgroundThreadProcessing() {
		//[ ... Tidskrevende kode ... ]
		int res = 0;	
		for (int i = 0; i < 10; i++) {
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		handler.post(doShowResult); 
		
		//Avslutt service når "oppdraget" er ferdig!
		this.stopSelf();
	}
	
	private Runnable doShowResult = new Runnable() {
		public void run() {
			showResult();
		}
	};

	private void showResult() {
		// Viser en Toast - kan kun fremvises i GUI-tråden:
		Context context = this.getApplicationContext();
		Toast.makeText(context, "Oppdrag fullført!!!!", Toast.LENGTH_SHORT).show();
		
		//Send beskjed til Activity vha. en broadcast / BroadcastReceiver.
	}
}
