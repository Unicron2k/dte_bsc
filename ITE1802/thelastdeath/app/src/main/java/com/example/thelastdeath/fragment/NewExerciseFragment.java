package com.example.thelastdeath.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.thelastdeath.R;
import com.example.thelastdeath.adapter.IconFileNameSpinnerAdapter;
import com.example.thelastdeath.entity.AppExercise;
import com.example.thelastdeath.entity.IconFileNames;
import com.example.thelastdeath.entity.User;
import com.example.thelastdeath.entity.UserProgram;
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
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;

import static com.example.thelastdeath.utils.Utils.displayToast;


public class NewExerciseFragment extends Fragment {
    private DataViewModel dataViewModel;

    private List<AppExercise> appExercises;
    private UserProgram userProgram;
    private List<String> iconName;
    private IconFileNameSpinnerAdapter iconFileNameSpinnerAdapter;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;
    private IconFileNames iconFileNames;
    private Spinner spinner;
    private String chosenColor;
    private String description;


    public NewExerciseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        userProgram = NewExerciseFragmentArgs.fromBundle(getArguments()).getUserProgramNewExercise();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_exercise, container, false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        subscribeToErrors();
        subscribeToApiResponse();
        //dataViewModel.getAllAppExercises(getActivity(), false);

        //Initialize layout fields
        spinner = view.findViewById(R.id.spinner_exercise_icon_png);
        EditText editTextName = view.findViewById(R.id.et_newExercise_name);
        EditText editTextDesc = view.findViewById(R.id.et_newExercise_description);
        Button showColorButton = view.findViewById(R.id.newExercise_button_color);
        ArrayList<String> colors = new ArrayList<>(Arrays.asList("#82B926", "#6a3ab2", "#666666", "#FFFF00", "#3C8D2F", "#FA9F00", "#FF0000", "#a276eb", "#34deeb", "#00ff6a", "#62eb34", "#c1c7b9", "#001aff", "#ff00e6", "#fff67a"));
        final String[] chosenColor = {colors.get(0)};
        showColorButton.setBackgroundColor(Color.parseColor(chosenColor[0]));
        showColorButton.setOnClickListener(v -> {
            final ColorPicker colorPicker = new ColorPicker(requireActivity());
            colorPicker
                    .setDefaultColorButton(Color.parseColor("#F84C44"))
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


        Button postExerciseButton = view.findViewById(R.id.newExercise_button_post);
        postExerciseButton.setOnClickListener(v -> {
            getUserFromServer();
            if (Utils.isEditTextFieldsNotEmpty(new EditText[]{editTextName, editTextDesc}, getActivity())) {
                String name = editTextName.getText().toString();
                description = editTextDesc.getText().toString();
                String icon = iconName.get(spinner.getSelectedItemPosition());
                String color = chosenColor[0];

                //POST new Exercise
                dataViewModel.postAppExercise(
                        getActivity(),
                        name,
                        description,
                        icon,
                        color
                );

                new AlertDialog.Builder(requireActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Do you want to create this exercise")
                        .setMessage("Click yes to create this exercise")
                        .setPositiveButton(getString(R.string.userprofile_alert_yes), (dialogInterface, i) -> {
                            // GJØR DETTE HVIS YES TRYKKES
                            dataViewModel.getAllAppExercises(getActivity(), false);
//

                        })
                        .setNegativeButton(getString(R.string.colorpicker_dialog_cancel), (dialogInterface, i) -> {
                            // GJØR DETTE HVIS NO TRYKKES

                        })
                        .show();

                Utils.emptyEditTextFields(new EditText[]{editTextName, editTextDesc});
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//getAllAppProgramTypesFromServer();
        Utils.displayToast(getActivity(), userProgram.getName());
        //setUpSpinner();

        setUpSpinner();
        dataViewModel.getIconFileNames(getActivity(), false);

        UtilsFirebase.redirectIfLoggedOut(view);
    }

    /**
     * Sets up spinner with color and icon
     */
    private void setUpSpinner() {
        iconName = new ArrayList<>();
        iconFileNameSpinnerAdapter = new IconFileNameSpinnerAdapter(getActivity(), R.layout.spinner_category_item, iconName);
        spinner.setAdapter(iconFileNameSpinnerAdapter);
    }


    /**
     * GET User from server
     */
    private void getUserFromServer() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            dataViewModel.getUser(getActivity(), firebaseUser.getUid(), true);
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

                    // Download all AppProgramTypes and notify Spinner
                    List<AppExercise> appExercises = apiResponse.getAllAppAxercises();

                    //TODO: Mildertidlig løsning, her bure man lage en ascyn metode..
                    if (appExercises != null) {
                        for (int i = 0; i < appExercises.size(); i++) {
                            if (appExercises.get(i).getDescription().equals(description)) {
                                long programId = userProgram.getId();
                                long exercises = appExercises.get(i).getId();

                                dataViewModel.postUserProgramExercise(getActivity(), programId, exercises);
                            }


                        }
                        //appExerciseSpinnerAdapter.notifyDataSetChanged();
                    }

                    // Populate entity.User object
                    User user = apiResponse.getUser();
                    if (user != null) {
                        // Dersom response på GET, PUT, POST:

                        Utils.appendLogger("user downloaded");
                    } else {
                        // Dersom response på DELETE
                        displayToast(getActivity(), getString(R.string.no_downloading));
                    }

                    IconFileNames iconFileNamesFromServer = apiResponse.getIconFileNames();
                    if (iconFileNamesFromServer != null) {
                        iconFileNames = iconFileNamesFromServer;
                        Utils.displayToast(getActivity(), iconFileNames.getIcons8_filenames().get(0));
                        iconFileNameSpinnerAdapter.addAll(iconFileNamesFromServer.getIcons8_filenames());
                        iconFileNameSpinnerAdapter.notifyDataSetChanged();
                        Utils.displayToast(getActivity(), "icons was downloaded");
                    }
                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }
}
