package com.shadyshrimp.lab9_2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class LoginFragment extends Fragment {
    View inflatedView;

    EditText etUsername;
    EditText etPassword;
    Button btLogIn;
    Button btRegister;
    Button btLogOut;

    NavOptions.Builder navOptionsBuilder;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;
            
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_login, container, false);

        etUsername = inflatedView.findViewById(R.id.etUsername);
        etPassword = inflatedView.findViewById(R.id.etPassword);
        btLogIn = inflatedView.findViewById(R.id.btLogIn);
        btRegister = inflatedView.findViewById(R.id.btRegister);
        btLogOut = inflatedView.findViewById(R.id.btLogOut);

        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(this.getActivity()));
        preferenceEditor = sharedPreferences.edit();

        navOptionsBuilder = new NavOptions.Builder();
        navOptionsBuilder.setEnterAnim(R.anim.slide_in_right);
        navOptionsBuilder.setExitAnim(R.anim.slide_out_left);
        navOptionsBuilder.setPopEnterAnim(R.anim.slide_in_left);
        navOptionsBuilder.setPopExitAnim(R.anim.slide_out_right);
        final NavOptions options = navOptionsBuilder.build();

        btLogIn.setOnClickListener(view->{
            preferenceEditor.putBoolean("USER_LOGGED_IN", true);
            preferenceEditor.commit();
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_global_programsFragment, null, options);
        });

        btRegister.setOnClickListener(view-> Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_loginFragment_to_registerFragment, null, options));

        btLogOut.setOnClickListener(view->{
            preferenceEditor.putBoolean("USER_LOGGED_IN", false);
            preferenceEditor.commit();
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.userGraph, null, options);
        });
        
        return inflatedView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean userLoggedIn = sharedPreferences.getBoolean("USER_LOGGED_IN", false);
        if (userLoggedIn) {
            etUsername.setVisibility(View.INVISIBLE);
            etPassword.setVisibility(View.INVISIBLE);
            btLogIn.setVisibility(View.INVISIBLE);
            btRegister.setVisibility(View.INVISIBLE);
            TextView tvHeaderLogin = inflatedView.findViewById(R.id.tvHeaderLogin);
            tvHeaderLogin.setText(getResources().getText(R.string.str_already_logged_in));
        } else {
            btLogOut.setVisibility(View.INVISIBLE);
        }
    }

}
