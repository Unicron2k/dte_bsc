package com.example.thelastdeath.fragment.add;

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
import com.example.thelastdeath.entity.IconFileNames;
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

public class newAppProgramTypeFragment extends Fragment {
    private DataViewModel dataViewModel;

    private List<String> iconNames;
    private IconFileNameSpinnerAdapter iconFileNameSpinnerAdapter;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;
    private Spinner spinner;

    public newAppProgramTypeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View view = inflater.inflate(R.layout.fragment_new_app_program_type, container, false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        subscribeToErrors();
        subscribeToApiResponse();

        // Initialize layout fields
        spinner = view.findViewById(R.id.spinner_existing_icons);
        EditText etDescription = view.findViewById(R.id.newAppProgramType_description_et);

        Button showColorButton = view.findViewById(R.id.newAppProgramType_button_color);
        ArrayList<String> colors = new ArrayList<>(Arrays.asList("#82B926", "#6a3ab2", "#666666", "#FFFF00", "#3C8D2F", "#FA9F00", "#FF0000", "#a276eb", "#34deeb", "#00ff6a", "#62eb34", "#c1c7b9", "#001aff", "#ff00e6", "#fff67a"));
        final String[] chosenColor = {colors.get(0)};
        showColorButton.setBackgroundColor(Color.parseColor(chosenColor[0]));
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

        Button postAppProgramType = view.findViewById(R.id.newAppProgramType_button_post);
        postAppProgramType.setOnClickListener(v -> {
            if (Utils.isEditTextFieldsNotEmpty(new EditText[]{etDescription}, getActivity())) {
                String icon = iconNames.get(spinner.getSelectedItemPosition());
                String description = etDescription.getText().toString();
                String back_color = chosenColor[0];

                // POST AppProgramType
                dataViewModel.postAppProgramType(
                        getActivity(),
                        description,
                        icon,
                        back_color);

                Utils.emptyEditTextFields(new EditText[]{etDescription});
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setUpSpinner();
        dataViewModel.getIconFileNames(getActivity(), false);
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