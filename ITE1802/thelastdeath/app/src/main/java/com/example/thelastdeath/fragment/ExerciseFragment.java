package com.example.thelastdeath.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.thelastdeath.R;
import com.example.thelastdeath.entity.AppExercise;
import com.example.thelastdeath.entity.User;
import com.example.thelastdeath.entity.helper.ApiError;
import com.example.thelastdeath.entity.helper.ApiResponse;
import com.example.thelastdeath.utils.Utils;
import com.example.thelastdeath.utils.api.UtilsAPI;
import com.example.thelastdeath.utils.firebase.UtilsFirebase;
import com.example.thelastdeath.viewmodel.DataViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ExerciseFragment extends Fragment {
    private DataViewModel dataViewModel;

    private List<AppExercise> appExercises;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    public ExerciseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        listUserPrograms();
    }


    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set view and subscribe to Rest API
        View view = inflater.inflate(R.layout.fragment_view_exercises, container, false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        subscribeToErrors();
        subscribeToApiResponse();

        // Initialize layout fields


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getUserFromServer();
        // Redirect user to HomeFragment if already signed in
        UtilsFirebase.redirectIfLoggedOut(view);
    }

    /**
     * List programs in a ListView and add onItemClick listeners to each item
     */
    private void listUserPrograms() {
        final ArrayList<String> userProgramNames = new ArrayList<>();
        if (appExercises != null)
            for (AppExercise userProgram : appExercises)
                userProgramNames.add(userProgram.getName());

        ListView listView = requireView().findViewById(R.id.viewExerciseListView);
        Utils.setAdapterToListView(getActivity(), userProgramNames, listView);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            AppExercise userProgram = appExercises.get(i);
            //UserProgramsFragmentDirections.ActionUserProgramsToUserProgramDetails argWithUserProgramObj = UserProgramsFragmentDirections.actionUserProgramsToUserProgramDetails(userProgram);
            NavController navController = Navigation.findNavController(view);
            //navController.navigate(argWithUserProgramObj);
        });
    }

    /**
     * GET User from server
     */
    private void getUserFromServer() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            dataViewModel.getUserExpandedchildren(getActivity(), firebaseUser.getUid(), true);
    }

    // Abonnerer på feilmeldinger:
    private void subscribeToErrors() {
        if (apiErrorObserver == null) {
            // Observerer endringer i errorMessage:
            apiErrorObserver = apiError -> {
                if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    if (apiError != null)
                        UtilsAPI.displayApiErrorResponseMessage(apiError, getActivity());
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

                    // Populate entity.User object
                    User user = apiResponse.getUser();
                    if (user != null) {
                        // Dersom response på GET, PUT, POST:
                        //appExercises = user.getApp_exercises();
                        listUserPrograms();
                    }
                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }

}