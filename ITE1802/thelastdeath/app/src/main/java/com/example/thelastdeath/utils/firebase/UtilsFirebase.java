package com.example.thelastdeath.utils.firebase;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.example.thelastdeath.R;
import com.example.thelastdeath.utils.Utils;
import com.example.thelastdeath.viewmodel.DataViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UtilsFirebase {

    /**
     * Returns True if user is logged in via Firebase
     */
    public static boolean isUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }


    /**
     * Redirects user to HomeFragment if logged in
     * To be called in @Override onViewCreated method
     */
    public static void redirectIfLoggedIn(View view) {
        if (isUserLoggedIn())
            Navigation.findNavController(view).navigate(R.id.homeFragment);
    }

    /**
     * Redirects user to LoginFragment if logged out
     * To be called in @Override onViewCreated method
     */
    public static void redirectIfLoggedOut(View view) {
        if (!isUserLoggedIn())
            Navigation.findNavController(view).navigate(R.id.loginFragment);
    }

    /**
     * Signs user out of Firebase and displays message when done
     */
    public static void signOut(Activity activity, View view) {
        if (isUserLoggedIn()) {
            AuthUI.getInstance()
                    .signOut(activity)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Utils.displayToast(activity, activity.getResources().getString(R.string.firebase_security_success_signOut));
                            redirectIfLoggedOut(view);
                        }
                    });

        } else
            Utils.displayToast(activity, activity.getResources().getString(R.string.firebase_security_warning_alreadySignedOut));
    }

    /**
     * Checks if given password is correct for current FirebaseUse
     *
     * @param activity
     * @param view
     * @param password
     * @return True if password was correct
     */
    public static void deleteFirebaseAndRestUserWithAutentication(Activity activity, View view, String password, DataViewModel dataViewModel) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), password);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> authenticateTask) {
                            if (authenticateTask.isSuccessful()) {
                                String Uuid = user.getUid();
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // Delete user via REST API and navigate to LoginFragment on success
                                        dataViewModel.deleteUser(activity, Uuid);
                                        Utils.appendLogger("Firebase-/REST-user deleted!");
                                        Navigation.findNavController(view).navigate(R.id.loginFragment);
                                        Utils.displayToast(activity, view.getResources().getString(R.string.userprofile_alert_delete_success));
                                    }
                                });
                            } else
                                Utils.displayToast(activity, "Wrong password!");
                        }
                    });
        }
    }
}