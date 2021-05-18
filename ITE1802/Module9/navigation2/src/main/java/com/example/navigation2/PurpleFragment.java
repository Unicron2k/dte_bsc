package com.example.navigation2;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurpleFragment extends Fragment {

    public PurpleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purple, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {



        // Navigerer vha. DEEP-LINK ACTION:
        Button btnDeep = view.findViewById(R.id.btnDeep);
        btnDeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Intent.ACTION_VIEW);
                //intent.setData(Uri.parse("https://kark.uit.no/deeplink/42"));
                //PurpleFragment.this.getActivity().startActivity(intent);

                //Uri deeplink = Uri.parse("https://www.example.com/77");  //com.example.navigation1/{myarg}
                //Uri deeplink = Uri.parse("https://kark.uit.no/deeplink/42");
                //Navigation.findNavController(view).navigate(deeplink, null);
/*
                Bundle args = new Bundle();
                args.putString("myarg", "From Widget");
                PendingIntent pendingIntent = new NavDeepLinkBuilder(PurpleFragment.this.getContext())
                        .setGraph(R.navigation.app_navigation)
                        .setDestination(R.id.deepLinkDestFragment)
                        .setArguments(args)
                        .createPendingIntent();
*/
                //NavDeepLinkBuilder navDeepLinkBuilder = Navigation.findNavController(view).createDeepLink();

            }
        });
    }
}
