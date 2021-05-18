package com.example.navigation2;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DeepLinkDestFragment extends Fragment {
    public DeepLinkDestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DeepLinkDestFragment newInstance(String param1, String param2) {
        DeepLinkDestFragment fragment = new DeepLinkDestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deep_link_dest, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = view.findViewById(R.id.text);
        if (null != getArguments()) {
            textView.setText(getArguments().getString("myarg"));
        }

        Button notificationButton = view.findViewById(R.id.send_notification_button);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                EditText editArgs = view.findViewById(R.id.args_edit_text);
                Bundle args = new Bundle();
                args.putString("myarg", editArgs.getText().toString());

                PendingIntent deeplink = Navigation.findNavController(getView()).createDeepLink()
                        .setDestination(R.id.deepLinkDestFragment)
                        .setArguments(args)
                        .createPendingIntent();

                NotificationManager notificationManager =
                        (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(new NotificationChannel(
                            "deeplink", "Deep Links", NotificationManager.IMPORTANCE_HIGH));
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        getContext(), "deeplink");
                builder.setContentTitle("Navigation")
                        .setContentText("Deep link to Android")
                        .setSmallIcon(R.drawable.ic_link_black_24dp)
                        .setContentIntent(deeplink)
                        .setAutoCancel(true);
                notificationManager.notify(0, builder.build());
            }
        });
        {
        }
    }
}
