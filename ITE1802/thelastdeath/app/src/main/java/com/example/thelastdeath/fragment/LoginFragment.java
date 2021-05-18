package com.example.thelastdeath.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.thelastdeath.MainActivity;
import com.example.thelastdeath.R;
import com.example.thelastdeath.utils.Utils;
import com.example.thelastdeath.utils.firebase.UtilsFirebase;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * TODO LIST:
 *  - Implement:
 *      - (DONE) Simple login-screen with Firebase
 *      - (DONE) Send user to HomeFragment on successful Firebase-login
 *      - (DONE) Create profile via REST API when logging in with firebase
 *  - Fix:
 *      -
 */
public class LoginFragment extends Fragment {
    private static final int RC_SIGN_IN = 123;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize elements in current layout
        Button btnLogin = view.findViewById(R.id.btn_start_login);
        btnLogin.setOnClickListener(view1 -> createSignInIntent());

        // finish() if KEYCODE_BLACK pressed
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            Utils.appendLogger(String.format("KeyPress: %s", keyCode));
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                requireActivity().finish();
                return true;
            }
            return false;
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Redirect user to HomeFragment if already signed in
        UtilsFirebase.redirectIfLoggedIn(view);
        ((MainActivity) requireActivity()).hideBottomNav();

    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        ((MainActivity) requireActivity()).hideBottomNav();
    }
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        ((MainActivity) requireActivity()).showBottomNav();
    }

    // FIREBASE SIGN IN & OUT:
    private void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse.fromResultIntent(data);
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                UtilsFirebase.redirectIfLoggedIn(getView());
            } else {
                Utils.displayToast(getActivity(), getString(R.string.loginfragment_warning_login_failed));
            }
        }
    }
}