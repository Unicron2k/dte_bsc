package com.example.navigation2;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainFragment extends Fragment {

    private NavOptions.Builder navOptionsBuilder;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        navOptionsBuilder = new NavOptions.Builder();
        navOptionsBuilder.setEnterAnim(R.anim.slide_in_right);
        navOptionsBuilder.setExitAnim(R.anim.slide_out_left);
        navOptionsBuilder.setPopEnterAnim(R.anim.slide_in_left);
        navOptionsBuilder.setPopExitAnim(R.anim.slide_out_right);
        final NavOptions options = navOptionsBuilder.build();

        // Navigasjon vha. ID:
        Button btnBlue = view.findViewById(R.id.btnBlue);
        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigation.findNavController(getView()).navigate(R.id.blueFragment, null);
                // Med animasjon:
                Navigation.findNavController(getView()).navigate(R.id.blueFragment, null, options);
            }
        });

        /* Navigasjon vha. ACTION
          dvs. koplingspilene mellom de ulike destinations:
        Navigation by actions has the following benefits over navigation by destination:
            => You can visualize the navigation paths through your app
            => Actions can contain additional associated attributes you can set, such as a transition animation, arguments values, and backstack behavior
            => Actions allow you to attach NavOptions in the navigation XML file, rather than specifying them programmatically.
            => You can use the plugin safe args to navigate.
             I fragment_main.xml (MERK id-navn: action_mainFragment_to_child**1**Fragment)
             . . .
             <action
                android:id="@+id/action_mainFragment_to_child1Fragment"
                app:destination="@id/blueFragment" />
        */
        Button yellowButton = view.findViewById(R.id.btnYellow);
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_yellow, null);
            }
        });
    }
}
