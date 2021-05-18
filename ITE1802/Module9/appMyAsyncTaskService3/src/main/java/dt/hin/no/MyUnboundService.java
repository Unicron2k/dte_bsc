package dt.hin.no;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyUnboundService extends Service {

	/*
	@Override
	public void onCreate() { 
        Log.d("MY_UNBOUNDSERVICE", "onCreate()");
	}
	*/
	
	@Override
	public IBinder onBind(Intent arg0) {  
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Context context = this.getApplicationContext();
		
		Toast.makeText(context, "Starter service", Toast.LENGTH_SHORT).show();
	
		MyAsyncTask a = new MyAsyncTask(this);
        a.execute("dette er inndata...", "dette er mer inndata", "osv...");
        
		//START_STICKY intent = null ved omstart (dvs. når service terminerer og starter på nytt ved ressursbehov):
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d("MY_UNBOUNDSERVICE", "onDestroy(...)");		
		// TODO Auto-generated method stub
		Context context = this.getApplicationContext();
		Toast.makeText(context, "Stopper service", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}
}
