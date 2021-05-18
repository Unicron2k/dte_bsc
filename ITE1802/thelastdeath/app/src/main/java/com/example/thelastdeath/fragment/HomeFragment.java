package com.example.thelastdeath.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.thelastdeath.R;
import com.example.thelastdeath.entity.User;
import com.example.thelastdeath.entity.helper.ApiError;
import com.example.thelastdeath.entity.helper.ApiResponse;
import com.example.thelastdeath.utils.Utils;
import com.example.thelastdeath.utils.api.UtilsAPI;
import com.example.thelastdeath.utils.firebase.UtilsFirebase;
import com.example.thelastdeath.viewmodel.DataViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * TODO LIST:
 *  - Implement:
 *      - Registration of user if logged in to Firebase
 *          Post user to server via 'postUserToServer()'
 *      - Redirect to LoginFragment if not logged in to Firebase
 *  - Fix:
 *      -
 */
public class HomeFragment extends Fragment {
    private TextView etName;
    private TextView etTitle;
    private DataViewModel dataViewModel;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private User mUser;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set view and subscribe to Rest API
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        etName  = view.findViewById(R.id.tv_dashboard_user);
        etTitle = view.findViewById(R.id.tv_dashboard_title);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            String title = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            etTitle.setText(title);
        }

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        subscribeToErrors();
        subscribeToApiResponse();

        // Start workout
        CardView cvNewUserProgramSessionButton = view.findViewById(R.id.cv_dashboard_startWorkout);
        cvNewUserProgramSessionButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.newUserProgramSession, null, Utils.getAnimation()));

        // Programs
        CardView cvProgramButton = view.findViewById(R.id.cv_dashboard_programs);
        cvProgramButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.userPrograms, null, Utils.getAnimation()));

        // Add program
        CardView cvNewUserProgramButton = view.findViewById(R.id.cv_dashboard_addProgram);
        cvNewUserProgramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionHomeFragmentToNewUserProgramFragment argWithUserObj = HomeFragmentDirections.actionHomeFragmentToNewUserProgramFragment(mUser);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(argWithUserObj);
            }
        });
//        cvNewUserProgramButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.newUserProgramFragment, null, Utils.getAnimation()));


        /*
        // Settings
        CardView cvSettings = view.findViewById(R.id.cv_dashboard_settings);
        if(cvSettings!=null) {
            cvSettings.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.settingsFragment, null, Utils.getAnimation()));
        }

        // Profile
        CardView cvMyProfile = view.findViewById(R.id.cv_dashboard_myprofile);
        if(cvMyProfile!=null) {
            cvMyProfile.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.userProfileFragment, null, Utils.getAnimation()));
        }
        */

        // Add AppProgramType
        CardView newAppProgramTypeButton = view.findViewById(R.id.cv_dashboard_addAppProgramType);
        newAppProgramTypeButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.newAppProgramType, null, Utils.getAnimation()));
        // Add AppExercise
        //CardView newExerciseButton = view.findViewById(R.id.cv_dashboard_exercises);
        //newExerciseButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.newExerciseFragment, null, Utils.getAnimation()));

        // Edit/delete AppProgramType
        CardView editDeleteAppProgramTypeButton = view.findViewById(R.id.cv_dashboard_editDeleteAppProgramType);
        editDeleteAppProgramTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionHomeFragmentToAppProgramTypes argWithUserObj = HomeFragmentDirections.actionHomeFragmentToAppProgramTypes(mUser);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(argWithUserObj);
//                Navigation.findNavController(view).navigate(R.id.appProgramTypes);
            }
        });

        // Log out
        CardView cvLogoutButton = view.findViewById(R.id.cv_dashboard_logout);
        if(cvLogoutButton!=null) {
            cvLogoutButton.setOnClickListener(v -> UtilsFirebase.signOut(getActivity(), v));
        }

        updateUI();

        // Return inflated layout
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Redirect user to HomeFragment if already signed in
        UtilsFirebase.redirectIfLoggedOut(view);
        // Create a user via REST API (if not already created)
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            dataViewModel.getUserExpandedchildren(getActivity(), firebaseUser.getUid(), false);
    }


    /**
     * Updates UI with Firebase DisplayName
     */
    private void updateUI() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            etName.setText(getString(R.string.home_firebase_displayName, firebaseUser.getDisplayName()));
        }
    }

    // POST: Last opp ny bruker til server
    private void postUserToServer() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null) {
            dataViewModel.postUser(getActivity(), firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getPhoneNumber(), firebaseUser.getEmail(), 1990);
        } else {
            Toast.makeText(getActivity(), "Du er ikke logget inn.", Toast.LENGTH_SHORT).show();
        }
    }

    // Abonnerer på feilmeldinger:
    private void subscribeToErrors() {
        if (apiErrorObserver == null) {
            // Observerer endringer i errorMessage:
            apiErrorObserver = apiError -> {
                if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    if (apiError != null) {
                        UtilsAPI.displayApiErrorResponseMessage(apiError, getActivity());
                        if (apiError.getMessage().equals("Not found.")) {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            dataViewModel.postUser(
                                    getActivity(),
                                    firebaseUser.getUid(),
                                    firebaseUser.getDisplayName(),
                                    "00000000",
                                    firebaseUser.getEmail(),
                                    1980);
                            Navigation.findNavController(requireView()).navigate(R.id.homeFragment);
                            Utils.displayToast(getActivity(), getString(R.string.home_api_newUserCreated));
                        }
                    }
                }
            };
            dataViewModel.getApiError().observe(getViewLifecycleOwner(), apiErrorObserver);
        }
    }

    // Abonnerer på server-respons:
    private void subscribeToApiResponse() {
        if (apiResponseObserver == null) {
            // Observerer endringer:
            apiResponseObserver = apiResponse -> {
                if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    UtilsAPI.displayApiResponseMessage(apiResponse, getActivity());

                    User userFromServer = apiResponse.getUser();
                    if (userFromServer != null)
                        mUser = apiResponse.getUser();
                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }
}
