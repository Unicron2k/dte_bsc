package com.example.thelastdeath.fragment.userprogram;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.thelastdeath.R;
import com.example.thelastdeath.adapter.IconFileNameSpinnerAdapter;
import com.example.thelastdeath.entity.AppExercise;
import com.example.thelastdeath.entity.IconFileNames;
import com.example.thelastdeath.entity.UserProgramExercise;
import com.example.thelastdeath.entity.helper.ApiError;
import com.example.thelastdeath.entity.helper.ApiResponse;
import com.example.thelastdeath.utils.Utils;
import com.example.thelastdeath.utils.api.UtilsAPI;
import com.example.thelastdeath.utils.firebase.UtilsFirebase;
import com.example.thelastdeath.viewmodel.DataViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;

import static com.example.thelastdeath.utils.Utils.displayToast;

public class UserProgramExerciseDetailsFragment extends Fragment {
    private final Calendar myCalendar = Calendar.getInstance();
    private DataViewModel dataViewModel;
    private UserProgramExercise userProgramExercise;
    private Spinner spinner;
    private List<String> iconNames;
    private IconFileNameSpinnerAdapter iconFileNameSpinnerAdapter;


    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    public UserProgramExerciseDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        userProgramExercise = UserProgramExerciseDetailsFragmentArgs.fromBundle(getArguments()).getUserProgramExerciseObject();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set view and subscribe to Rest API
        View view = inflater.inflate(R.layout.fragment_user_program_exercise_details, container, false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        subscribeToErrors();
        subscribeToApiResponse();

        // Initialize layout fields
        EditText etName = view.findViewById(R.id.userProgramExerciseDetails_name_et);
        etName.setText(userProgramExercise.getApp_exercise().getName());
        EditText etDescription = view.findViewById(R.id.userProgramExerciseDetails_description_et);
        etDescription.setText(userProgramExercise.getApp_exercise().getDescription());
        TextView tvCurentIconName = view.findViewById(R.id.userProgramExerciseDetails_current_icon_tv);
        tvCurentIconName.setText(getString(R.string.userProgramExerciseDetails__tv_icon, Utils.convertFileNameIcon8(userProgramExercise.getApp_exercise().getIcon())));
        spinner = view.findViewById(R.id.spinner_existing_icons);
        Button showColorButton = view.findViewById(R.id.userProgramExerciseDetails_button_color);
        try {
            showColorButton.setBackgroundColor(Color.parseColor(userProgramExercise.getApp_exercise().getInfobox_color()));
        } catch (Exception ex) {
            Utils.appendLogger(ex.toString());
        }
        ArrayList<String> colors = new ArrayList<>(Arrays.asList("#82B926", "#6a3ab2", "#666666", "#FFFF00", "#3C8D2F", "#FA9F00", "#FF0000", "#a276eb", "#34deeb", "#00ff6a", "#62eb34", "#c1c7b9", "#001aff", "#ff00e6", "#fff67a"));
        final String[] chosenColor = {colors.get(0)};

        showColorButton.setOnClickListener(v -> {
            final ColorPicker colorPicker = new ColorPicker(requireActivity());
            colorPicker
                    .setDefaultColorButton(Color.parseColor("#f84c44"))
                    .setColors(colors)
                    .setColumns(5)
                    .setRoundColorButton(true)
                    .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                        @Override
                        public void onChooseColor(int position, int color) {
                            chosenColor[0] = colors.get(position);
                            showColorButton.setBackgroundColor(Color.parseColor(chosenColor[0]));
                            Utils.appendLogger("Color: " + colors.get(position));
                        }

                        @Override
                        public void onCancel() {
                        }
                    }).show();
        });

        Button putExerciseButton = view.findViewById(R.id.userProgramExerciseDetails_button_put);
        putExerciseButton.setOnClickListener(v -> {
            if (Utils.isEditTextFieldsNotEmpty(new EditText[]{etName, etDescription}, getActivity())) {
                AppExercise appExercise = userProgramExercise.getApp_exercise();

                String rid = appExercise.getRid();
                String name = etName.getText().toString();
                String description = etDescription.getText().toString();
                String icon = iconNames.get(spinner.getSelectedItemPosition());
                String infobox_color = chosenColor[0];

                // PUT AppExercise
                dataViewModel.putAppExercise(
                        getActivity(),
                        rid,
                        name,
                        description,
                        icon,
                        infobox_color);
            }
        });

        Button deleteExerciseButton = view.findViewById(R.id.userProgramExerciseDetails_button_delete);
        deleteExerciseButton.setOnClickListener(v -> new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.userProgramExerciseDetails_alert_title))
                .setMessage(getString(R.string.userProgramExerciseDetails_alert_message))
                .setPositiveButton(getString(R.string.userprofile_alert_yes), (dialogInterface, i) -> {
                    // Confirm deletion
                    dataViewModel.deleteUserProgramExercise(
                            getActivity(),
                            userProgramExercise.getRid());
                    Navigation.findNavController(view).navigate(R.id.homeFragment, null, Utils.getAnimation());
                    Utils.displayToast(getActivity(), getString(R.string.userProgramExerciseDetails_deleted));
                })
                .setNegativeButton(getString(R.string.userprofile_alert_no), (dialogInterface, i) -> {
                    // Cancel deletion
                    displayToast(getActivity(), getString(R.string.userProgramExerciseDetails_alert_delete_cancelled));
                })
                .show());
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setUpSpinner();
        dataViewModel.getIconFileNames(getActivity(), false);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            dataViewModel.getUserExpandedchildren(getActivity(), firebaseUser.getUid(), false);
        // Redirect user to HomeFragment if already signed in
        UtilsFirebase.redirectIfLoggedOut(view);
    }


    /**
     * Sets up spinner with Categories
     */
    private void setUpSpinner() {
        iconNames = new ArrayList<>();
        iconFileNameSpinnerAdapter = new IconFileNameSpinnerAdapter(getActivity(), R.layout.spinner_category_item, iconNames);
        spinner.setAdapter(iconFileNameSpinnerAdapter);
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

                    IconFileNames iconFileNamesFromServer = apiResponse.getIconFileNames();
                    if (iconFileNamesFromServer != null) {
                        iconNames.addAll(iconFileNamesFromServer.getIcons8_filenames());
                        iconFileNameSpinnerAdapter.notifyDataSetChanged();
                    }
                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }

}