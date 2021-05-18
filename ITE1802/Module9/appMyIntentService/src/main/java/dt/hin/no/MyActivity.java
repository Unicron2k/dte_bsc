package dt.hin.no;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/*
    Eksemplet viser bruk av en enkel IntentService.
    Startes ved å kalle startService(). Når service-metoden onHandleIntent() er ferdig termineres servicen
    automatisk (dersom det ikke ligger flere forespørsler om å kjøre denne i "intent-køen").
 */
public class MyActivity extends AppCompatActivity {
	//private ComponentName service;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Starter service:
		Button startButton = (Button) findViewById(R.id.btnStartService);
		startButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(MyActivity.this, MyIntentService.class);
				startService(myIntent);
			}
		});
	}
}