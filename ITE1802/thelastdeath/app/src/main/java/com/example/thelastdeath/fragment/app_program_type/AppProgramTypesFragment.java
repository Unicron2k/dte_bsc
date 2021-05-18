package com.example.thelastdeath.fragment.app_program_type;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.thelastdeath.R;
import com.example.thelastdeath.adapter.AppProgramTypeSpinnerAdapter;
import com.example.thelastdeath.adapter.IconFileNameSpinnerAdapter;
import com.example.thelastdeath.entity.AppProgramType;
import com.example.thelastdeath.entity.IconFileNames;
import com.example.thelastdeath.entity.User;
import com.example.thelastdeath.entity.helper.ApiError;
import com.example.thelastdeath.entity.helper.ApiResponse;
import com.example.thelastdeath.utils.Utils;
import com.example.thelastdeath.utils.api.UtilsAPI;
import com.example.thelastdeath.utils.firebase.UtilsFirebase;
import com.example.thelastdeath.viewmodel.DataViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;

import static com.example.thelastdeath.utils.Utils.displayToast;

public class AppProgramTypesFragment extends Fragment {
    private DataViewModel dataViewModel;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private User mUser;
    private AppProgramTypeSpinnerAdapter appProgramTypeSpinnerAdapter;
    private IconFileNameSpinnerAdapter iconFileNameSpinnerAdapter;
    private Spinner spinnerCurrentAppProgramTypes;
    private Spinner spinnerIconFileNames;
    private List<AppProgramType> appProgramTypes;
    private EditText etDescription;
    private Button pickColorButton;
    private IconFileNames iconFileNames;
    private AppProgramType currentAppProgramType;

    public AppProgramTypesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = AppProgramTypesFragmentArgs.fromBundle(getArguments()).getUserObject();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set view and subscribe to Rest API
        View view = inflater.inflate(R.layout.fragment_appprogramtypes, container, false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        subscribeToErrors();
        subscribeToApiResponse();

        // Initialize layout fields
        spinnerCurrentAppProgramTypes = view.findViewById(R.id.appPRogramTypes_spinner_existing);
        EditText etDescription = view.findViewById(R.id.appProgramTypes_et_name);
        spinnerIconFileNames = view.findViewById(R.id.appProgramTypes_spinner_icon);

        Button showColorButton = view.findViewById(R.id.appProgramTypes_button_color);
        ArrayList<String> colors = new ArrayList<>(Arrays.asList("#82B926", "#6a3ab2", "#666666", "#FFFF00", "#3C8D2F", "#FA9F00", "#FF0000", "#a276eb", "#34deeb", "#00ff6a", "#62eb34", "#c1c7b9", "#001aff", "#ff00e6", "#fff67a"));
        final String[] chosenColor = {colors.get(0)};
//        showColorButton.setBackgroundColor(Color.parseColor(chosenColor[0]));
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

        spinnerCurrentAppProgramTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update fields
                currentAppProgramType = appProgramTypeSpinnerAdapter.getItem(position);
                try {
                    etDescription.setText(currentAppProgramType.getDescription());
                    for (int i = 0; i < iconFileNames.getIcons8_filenames().size(); i++)
                        if (iconFileNames.getIcons8_filenames().get(i).equals(currentAppProgramType.getIcon())) {
                            spinnerIconFileNames.setSelection(i);
                        }
                    pickColorButton.setBackgroundColor(Color.parseColor(currentAppProgramType.getBack_color()));
                } catch (Exception e) {
                    Utils.appendLogger(e.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button buttonPutAppProgramType = view.findViewById(R.id.appProgramTypes_button_put);
        buttonPutAppProgramType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rid = currentAppProgramType.getRid();
                String description = etDescription.getText().toString();
                String icon = iconFileNames.getIcons8_filenames().get(spinnerIconFileNames.getSelectedItemPosition());
                String back_color = chosenColor[0];

                // PUT AppProgramType
                dataViewModel.putAppProgramType(
                        getActivity(),
                        rid,
                        description,
                        icon,
                        back_color);

                dataViewModel.getAllAppProgramTypes(
                        getActivity(),
                        false);

                Utils.emptyEditTextFields(new EditText[]{etDescription});
            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        dataViewModel.getAllAppProgramTypes(getActivity(), false);
        // Redirect user to HomeFragment if already signed in
        UtilsFirebase.redirectIfLoggedOut(view);
    }

    /**
     * Sets up spinner with AppProgramType's
     */
    private void setUpSpinnerAppProgramType() {
        appProgramTypeSpinnerAdapter = new AppProgramTypeSpinnerAdapter(getActivity(), R.layout.spinner_category_item, appProgramTypes);
        spinnerCurrentAppProgramTypes.setAdapter(appProgramTypeSpinnerAdapter);
    }


    /**
     * Sets up spinner with icons
     */
    private void setUpSpinnerIcons() {
        iconFileNameSpinnerAdapter = new IconFileNameSpinnerAdapter(getActivity(), R.layout.spinner_category_item, iconFileNames.getIcons8_filenames());
        spinnerIconFileNames.setAdapter(iconFileNameSpinnerAdapter);
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

                    List<AppProgramType> appProgramTypesFromServer = apiResponse.getAllAppProgramTypes();
                    if (appProgramTypesFromServer != null) {
                        appProgramTypes = appProgramTypesFromServer;
                        setUpSpinnerAppProgramType();
                        if (iconFileNames == null)
                            dataViewModel.getIconFileNames(getActivity(), false);
                    }

                    if (apiResponse.getIconFileNames() != null) {
                        iconFileNames = apiResponse.getIconFileNames();
                        setUpSpinnerIcons();
                    }

                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }

}