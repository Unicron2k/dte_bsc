package com.example.thelastdeath.fragment.userprogram;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.thelastdeath.R;
import com.example.thelastdeath.entity.User;
import com.example.thelastdeath.entity.UserProgram;
import com.example.thelastdeath.entity.UserProgramExercise;
import com.example.thelastdeath.entity.UserProgramSession;
import com.example.thelastdeath.entity.helper.ApiError;
import com.example.thelastdeath.entity.helper.ApiResponse;
import com.example.thelastdeath.utils.Utils;
import com.example.thelastdeath.utils.api.UtilsAPI;
import com.example.thelastdeath.utils.firebase.UtilsFirebase;
import com.example.thelastdeath.viewmodel.DataViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import static com.example.thelastdeath.utils.Utils.displayToast;

public class UserProgramDetailsFragment extends Fragment {
    private DataViewModel dataViewModel;

    private UserProgram userProgram;
    private User mUser;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    public UserProgramDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProgram = UserProgramDetailsFragmentArgs.fromBundle(getArguments()).getUserProgramObject();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set view and subscribe to Rest API
        View view = inflater.inflate(R.layout.fragment_user_program_details, container, false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        subscribeToErrors();
        subscribeToApiResponse();

        // Initialize layout fields
        TextView tvDescription = view.findViewById(R.id.UserProgramDetails_tv_description);
        tvDescription.setText(userProgram.getDescription());
        TextView tvUseTiming = view.findViewById(R.id.UserProgramDetails_tv_useTiming);
        tvUseTiming.setText((userProgram.getUse_timing() == 1) ? getString(R.string.userProgramsDetails_yes) : getString(R.string.userProgramsDetails_no));
        TextView tvAppProgramType = view.findViewById(R.id.UserProgramDetails_tv_appProgramType);
        tvAppProgramType.setText(userProgram.getApp_program_type().getDescription());

        TextView alertNoExercisesTV = view.findViewById(R.id.userProgramDetails_alert_exercises);
        if (userProgram.getUser_program_exercises().isEmpty())
            alertNoExercisesTV.setText(getString(R.string.userProgramsDetails_tv_alert_no_Exercises));

        TextView alertNoSessionsTv = view.findViewById(R.id.userProgramDetails_alert_sessions);
        if (userProgram.getUser_program_sessions().isEmpty())
            alertNoSessionsTv.setText(getString(R.string.userProgramsDetails_tv_alert_no_Sessions));

        Button addExerciseButton = view.findViewById(R.id.userProgramDetails_button_addExercise);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProgram = UserProgramDetailsFragmentArgs.fromBundle(getArguments()).getUserProgramObject();
                UserProgramDetailsFragmentDirections.ActionUserProgramDetailsToNewExerciseFragment argUserProgram = UserProgramDetailsFragmentDirections.actionUserProgramDetailsToNewExerciseFragment(userProgram);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(argUserProgram);
            }
        });


        Button deleteProgramButton = view.findViewById(R.id.userProgramDetails_button_delete);
        deleteProgramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.userProgramSessionDetails_alert_title))
                        .setMessage(getString(R.string.userProgramSessionDetails_alert_message))
                        .setPositiveButton(getString(R.string.userprofile_alert_yes), (dialogInterface, i) -> {
                            // Confirm deletion
                            dataViewModel.deleteUserProgram(
                                    getActivity(),
                                    userProgram.getRid());
                            Navigation.findNavController(view).navigate(R.id.homeFragment, null, Utils.getAnimation());
                        })
                        .setNegativeButton(getString(R.string.userprofile_alert_no), (dialogInterface, i) -> {
                            // Cancel deletion
                            displayToast(getActivity(), getString(R.string.userProgramSessionDetails_alert_delete_cancelled));
                        })
                        .show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        dataViewModel.getAppProgramTypeFromRid(getActivity(), userProgram.getApp_program_type().getRid(), false);
        listUserProgramSessions();
        listUserProgramExercises();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            dataViewModel.getUserExpandedchildren(getActivity(), user.getUid(), false);

        // Redirect user to HomeFragment if already signed in
        UtilsFirebase.redirectIfLoggedOut(view);
    }


    /**
     * List UserProgramSessions in a ListView and add onItemClick listeners to each item
     */
    private void listUserProgramSessions() {
        final ArrayList<String> userProgramSessionText = new ArrayList<>();
        if (userProgram.getUser_program_exercises() != null)
            for (UserProgramSession session : userProgram.getUser_program_sessions())
                userProgramSessionText.add(String.format("%s: %ss", session.getDate(), session.getTime_spent()));

        ListView listView = requireView().findViewById(R.id.userProgramSessionsListView);
        Utils.setAdapterToListView(getActivity(), userProgramSessionText, listView);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            UserProgramSession userProgramSession = userProgram.getUser_program_sessions().get(i);
            UserProgramDetailsFragmentDirections.ActionUserProgramDetailsToUserProgramSessionDetails argWithUserProgramSessionObj = UserProgramDetailsFragmentDirections.actionUserProgramDetailsToUserProgramSessionDetails(userProgramSession);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(argWithUserProgramSessionObj);
        });
    }

    /**
     * List UserProgramExercises in a ListView and add onItemClick listeners to each item
     */
    private void listUserProgramExercises() {
        final ArrayList<String> userProgramExerciseText = new ArrayList<>();

        if (userProgram.getUser_program_exercises() != null)
            for (UserProgramExercise userProgramExercise : userProgram.getUser_program_exercises())
                if (userProgramExercise.getApp_exercise() != null)
                    userProgramExerciseText.add(userProgramExercise.getApp_exercise().getName());
                else
                    Utils.displayToast(getActivity(), "appExercise was null");
        ListView listView = requireView().findViewById(R.id.userProgramExercisesListView);
        Utils.setAdapterToListView(getActivity(), userProgramExerciseText, listView);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Utils.displayToast(getActivity(), "" + userProgramExerciseText.get(0));
//            AppExercise appExercise = userProgram.getUser_program_exercises().get(i).getApp_exercise();
            UserProgramExercise userProgramExercise = userProgram.getUser_program_exercises().get(i);
            UserProgramDetailsFragmentDirections.ActionUserProgramDetailsToUserProgramExerciseDetails argWithUserProgramExerciseObj = UserProgramDetailsFragmentDirections.actionUserProgramDetailsToUserProgramExerciseDetails(userProgramExercise);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(argWithUserProgramExerciseObj);
        });
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
                        mUser = user;
//                        listUserProgramExercises();

//                        userPrograms = user.getUser_programs();
//                        listUserPrograms();
                    }
                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }

}