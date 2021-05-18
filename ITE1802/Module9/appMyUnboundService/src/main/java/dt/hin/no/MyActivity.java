package dt.hin.no;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MyActivity extends AppCompatActivity {
	private ComponentName service;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//Knapper for MyUnboundService:
		//Starter service:
		Button startButton = (Button) findViewById(R.id.btnStartService);
		startButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(MyActivity.this, MyUnboundService.class);
				service = startService(myIntent);
			}
		});

		//Stopper service:
		Button stopButton = (Button) findViewById(R.id.btnStop);
		stopButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				stopService(new Intent(MyActivity.this, MyUnboundService.class));
			}
		});
	}

    @Override
    protected void onStart() {
        //Sørger for å starte den UBUNDNE servicen:
        //Intent myIntent = new Intent(MyActivity.this, MyUnboundService.class);
        //service = startService(myIntent);

        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onStop() {
        //Sørger for å stoppe den UBUNDNE servicen (hvis ikke vil den fortsette å kjøre i bakgrunnen:
        //En BUNDET service stopper automatisk når aktiviteten avsluttes.
        //stopService(new Intent(MyActivity.this, MyUnboundService.class));

        // TODO Auto-generated method stub
        super.onStop();
    }

}