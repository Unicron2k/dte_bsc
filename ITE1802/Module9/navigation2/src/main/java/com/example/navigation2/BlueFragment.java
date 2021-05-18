package com.example.navigation2;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class BlueFragment extends Fragment {

    public BlueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blue, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final EditText etNumber = view.findViewById(R.id.etNumber);

        Button btnOrange = view.findViewById(R.id.btnOrange);
        btnOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tallSomSkalSendes = Integer.parseInt(etNumber.getText().toString());

                // Sender med argument vha SafeArgs-genererte klasser:
                BlueFragmentDirections.ActionOrange actionOrange = BlueFragmentDirections.actionOrange();
                actionOrange.setEtLiteTall(tallSomSkalSendes);

                NavController navController = Navigation.findNavController(view);
                navController.navigate(actionOrange);

            }
        });
    }
}
