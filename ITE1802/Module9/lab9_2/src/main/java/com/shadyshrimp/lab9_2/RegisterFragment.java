package com.shadyshrimp.lab9_2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import java.util.Objects;

public class RegisterFragment extends Fragment {
    View inflatedView;

    EditText etUsername;
    EditText etPassword;
    EditText etConfirmPassword;
    Button btRegister;
    Button btBack;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_register, container, false);

        etUsername = inflatedView.findViewById(R.id.etUsername);
        etPassword = inflatedView.findViewById(R.id.etPassword);
        etConfirmPassword = inflatedView.findViewById(R.id.etConfirmPassword);
        btRegister = inflatedView.findViewById(R.id.btRegister);
        btBack = inflatedView.findViewById(R.id.btBack);
        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(this.getActivity()));
        preferenceEditor = sharedPreferences.edit();

        btRegister.setOnClickListener(view->{
            preferenceEditor.putBoolean("USER_LOGGED_IN", true);
            preferenceEditor.commit();
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_global_programsFragment, null);
        });

        btBack.setOnClickListener(view-> Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_registerFragment_to_loginFragment, null));

        return inflatedView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean userLoggedIn = sharedPreferences.getBoolean("USER_LOGGED_IN", false);
        if (userLoggedIn) {
            etUsername.setVisibility(View.INVISIBLE);
            etPassword.setVisibility(View.INVISIBLE);
            etConfirmPassword.setVisibility(View.INVISIBLE);
            btRegister.setVisibility(View.INVISIBLE);
            TextView tvHeaderRegister = view.findViewById(R.id.tvHeaderRegister);
            tvHeaderRegister.setText(getResources().getString(R.string.str_already_registered));
            btBack.setOnClickListener(view1-> Objects.requireNonNull(getActivity()).onBackPressed());
        }
    }
}
