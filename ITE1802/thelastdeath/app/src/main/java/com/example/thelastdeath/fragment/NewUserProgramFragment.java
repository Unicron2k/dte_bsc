package com.example.thelastdeath.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.thelastdeath.R;
import com.example.thelastdeath.adapter.AppProgramTypeSpinnerAdapter;
import com.example.thelastdeath.entity.AppProgramType;
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
import java.util.List;

import static com.example.thelastdeath.utils.Utils.emptyEditTextFields;

public class NewUserProgramFragment extends Fragment {
    private DataViewModel dataViewModel;
    private List<AppProgramType> appProgramTypes;

    private AppProgramTypeSpinnerAdapter appProgramTypeSpinnerAdapter;
    private Spinner spinner;
    private long user_id;


    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    public NewUserProgramFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        user_id = NewUserProgramFragmentArgs.fromBundle(getArguments()).getUserObject().getId();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set view and subscribe to Rest API
        View view = inflater.inflate(R.layout.fragment_new_user_program, container, false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        subscribeToErrors();
        subscribeToApiResponse();

        // Initialize layout fields
        spinner = view.findViewById(R.id.spinner_existing_app_program_type_id);
        EditText editTextName = view.findViewById(R.id.newuserprogram_name);
        EditText editTextDescription = view.findViewById(R.id.newuserprogram_description);
        Switch switchUseTiming = view.findViewById(R.id.newuserprogram_useTiming);
        Button postUserProgramButton = view.findViewById(R.id.newUserProgram_button_post);
        postUserProgramButton.setOnClickListener(v -> {
            getUserFromServer();
            if (Utils.isEditTextFieldsNotEmpty(new EditText[]{editTextName, editTextDescription}, getActivity())) {
                String name = editTextName.getText().toString();
                String description = editTextDescription.getText().toString();
                int useTiming = switchUseTiming.isChecked() ? 1 : 0;

                // POST new UserProgram
                dataViewModel.postUserProgram(
                        getActivity(),
                        appProgramTypes.get(spinner.getSelectedItemPosition()).getId(),
                        user_id,
                        name,
                        description,
                        useTiming);

                // Empty fields
                emptyEditTextFields(new EditText[]{editTextName, editTextDescription});
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getAllAppProgramTypesFromServer();
        setUpSpinner();

        // Redirect user to HomeFragment if already signed in
        UtilsFirebase.redirectIfLoggedOut(view);
    }

    /**
     * Sets up spinner with Categories
     */
    private void setUpSpinner() {
        appProgramTypes = new ArrayList<>();
        appProgramTypeSpinnerAdapter = new AppProgramTypeSpinnerAdapter(getActivity(), R.layout.spinner_category_item, appProgramTypes);
        spinner.setAdapter(appProgramTypeSpinnerAdapter);
    }


    /**
     * GET all AppProgramTypes from server
     * Use 'apiResponse.getAllAppProgramTypes();' in subscribeToApiResponse();
     */
    private void getAllAppProgramTypesFromServer() {
        dataViewModel.getAllAppProgramTypes(getActivity(), true);
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
                    List<AppProgramType> appProgramTypeList = apiResponse.getAllAppProgramTypes();
                    if (appProgramTypeList != null) {
                        appProgramTypes.addAll(apiResponse.getAllAppProgramTypes());
                        appProgramTypeSpinnerAdapter.notifyDataSetChanged();
                    }

                    // Populate entity.User object
                    User user = apiResponse.getUser();
                    if (user != null)
                        // Dersom response på GET, PUT, POST:
                        user_id = user.getId();

                    UserProgram userProgramFromServer = apiResponse.getUserProgram();
                    if (userProgramFromServer != null)
                        Utils.appendLogger("From server: " + userProgramFromServer.getDescription());
                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }

}