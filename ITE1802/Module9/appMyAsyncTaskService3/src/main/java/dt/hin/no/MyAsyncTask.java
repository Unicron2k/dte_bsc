package dt.hin.no;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyAsyncTask extends AsyncTask<String, Integer, Integer> {
	private Context context = null;

	//Bruk av Notifications:
	public static final int NOT_ID = 1; 
	private Notification notification;
	private NotificationManager notificationManager;
	
	public MyAsyncTask(Context context) {
		this.context = context;	    
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		// Tre punktumer bak String indikerer at metoden kan ta 0 eller flere strenger evt. et string-array
		//Viser parametrene i loggen_
		try {
			Log.d("MYASYNCTASK", params[0]);
			Log.d("MYASYNCTASK", params[1]);
			Log.d("MYASYNCTASK", params[2]);
		} catch (Exception e) {
			Log.d("MYASYNCTASK", "Mindre enn 3 parametre...");
		}

		// Her gjør man det som evt. tar tid, dvs. laste ned fra nett m.m.
 	    int res = 0;
		for (int i = 0; i < 20000; i++) {
			for (int j = 0; j < 1000; j++) {
				res += 1.0;
			}
			//Trigger onProgressUpdate:
			publishProgress(new Integer(res));
		}

		// Det som returneres herfra kommer som parameter til onPostExecute();
		return new Integer(res);
	}

	@Override
	protected void onPostExecute(Integer result) {
		// Oppdaterer GUI med svaret: Bruk f.eks. Toast, Activity, Dialog, notification e.l.
		String svcName = Context.NOTIFICATION_SERVICE;
        notificationManager = (NotificationManager)context.getSystemService(svcName);
        
        //Viser en toast...
		Toast.makeText(context, "Resultat av asynctask = " + result.intValue(), Toast.LENGTH_LONG).show();
		super.onPostExecute(result);
		
		//... og sender Broadcast:
		Intent intent = new Intent("dt.hin.no.MY_RESULTRECEIVER");
		intent.putExtra("resultat", "Svaret er " + result.intValue());
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// [... Update progress bar, Notification, or other UI element ...]

		super.onProgressUpdate(values);
	}
}
