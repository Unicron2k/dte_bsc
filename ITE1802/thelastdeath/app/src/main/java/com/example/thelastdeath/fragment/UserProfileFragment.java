package com.example.thelastdeath.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

import static com.example.thelastdeath.utils.Utils.displayToast;
import static com.example.thelastdeath.utils.Utils.isEditTextFieldsNotEmpty;
import static com.example.thelastdeath.utils.Utils.setButtonVisibility;
import static com.example.thelastdeath.utils.Utils.setEditTextVisibility;
import static com.example.thelastdeath.utils.firebase.UtilsFirebase.isUserLoggedIn;
import static com.example.thelastdeath.utils.firebase.UtilsFirebase.redirectIfLoggedOut;

public class UserProfileFragment extends Fragment {
    private EditText etName, etEmail, etPhone, etBirthYear;
    private TextView tvNameTitle;

    private DataViewModel dataViewModel;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    public UserProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_userprofile, container, false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        subscribeToErrors();
        subscribeToApiResponse();

        // Fetch fields
        tvNameTitle = view.findViewById(R.id.tv_userprofile_Title);
        etName = view.findViewById(R.id.et_userprofile_name);
        etEmail = view.findViewById(R.id.et_userProfile_email);
        etPhone = view.findViewById(R.id.et_userProfile_phone);
        etBirthYear = view.findViewById(R.id.et_userprofile_birthYear);
        Button buttonConfirm = view.findViewById(R.id.btn_userprofile_updateUser);
        Button buttonDeleteAccount = view.findViewById(R.id.btn_userprofile_deleteUser);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserParamsViaRestAPI();
            }
        });

        // Assign listeners to buttons
        buttonConfirm.setOnClickListener(v -> updateUserParamsViaRestAPI());
        buttonDeleteAccount.setOnClickListener(v -> deleteUser());

        // Set availability on input-fields if user logged in
        setButtonVisibility(new Button[]{buttonConfirm, buttonDeleteAccount}, isUserLoggedIn());
        setEditTextVisibility(new EditText[]{etName, etEmail, etPhone, etBirthYear}, isUserLoggedIn());

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Bruk cachet verdi på user, hvis den eksisterer. Hvis ikke last ned.
        getUserFromServer();
        // Redirect to LoginFragment if not logged in
        redirectIfLoggedOut(view);
    }

    /**
     * Deletes Firebase-account and user from REST API
     *  Password authorization is prompted
     */
    private void deleteUser() {
        final EditText passwordInput = new EditText(getActivity());
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        if (!isUserLoggedIn()) {
            displayToast(getActivity(), getString(R.string.warning_firebase_not_logged_in));
        } else
            new AlertDialog.Builder(requireActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.userprofile_alert_title_deleteuser))
                    .setMessage(getString(R.string.userprofile_alert_message_deleteuser))
                    .setPositiveButton(getString(R.string.userprofile_alert_yes), (dialogInterface, i) -> {
                        // Confirm deletion
                        new AlertDialog.Builder(requireActivity())
                                .setTitle(getString(R.string.userprofile_alert_password_write))
                                .setIcon(android.R.drawable.ic_menu_delete)
                                .setView(passwordInput)
                                .setPositiveButton(getString(R.string.userprofile_alert_password_confirm), (innerDialogInterface, k) -> {
                                    // Confirm password
                                    if (passwordInput.getText().toString().equals(""))
                                        Utils.displayToast(getActivity(), getString(R.string.empty_input_fields));
                                    else
                                        UtilsFirebase.deleteFirebaseAndRestUserWithAutentication(getActivity(), getView(), passwordInput.getText().toString(), dataViewModel);
                                })
                                .setNegativeButton(getString(R.string.userprofile_alert_password_cancel), (innerDialogInterface, k) -> {
                                    // Cancel deletion
                                    displayToast(getActivity(), getString(R.string.userprofile_alert_delete_canceled));
                                })
                                .show();
                    })
                    .setNegativeButton(getString(R.string.userprofile_alert_no), (dialogInterface, i) -> {
                        // Cancel deletion
                        displayToast(getActivity(), getString(R.string.userprofile_alert_delete_canceled));
                    })
                    .show();
    }


    /**
     * Updates user-details via REST API
     */
    private void updateUserParamsViaRestAPI() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            if (isEditTextFieldsNotEmpty(new EditText[]{etName, etEmail, etPhone, etBirthYear}, getActivity()))
                dataViewModel.putUser(
                        getActivity(),
                        firebaseUser.getUid(),
                        etName.getText().toString(),
                        etPhone.getText().toString(),
                        etEmail.getText().toString(),
                        Integer.parseInt(etBirthYear.getText().toString()));
        } else
            displayToast(getActivity(), getString(R.string.warning_firebase_not_logged_in));
    }

    /**
     * GET User from server
     */
    private void getUserFromServer() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            dataViewModel.getUser(getActivity(), firebaseUser.getUid(), false);
        else
            displayToast(getActivity(), getString(R.string.warning_firebase_not_logged_in));
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
                    // Display response in Toast
                    UtilsAPI.displayApiResponseMessage(apiResponse, getActivity());
                    User user = apiResponse.getUser();
                    if (user != null) {
                        // Dersom response på GET, PUT, POST:
                        tvNameTitle.setText(user.getName());
                        etName.setText(user.getName());
                        etEmail.setText(user.getEmail());
                        etPhone.setText(user.getPhone());
                        etBirthYear.setText(String.valueOf(user.getBirth_year()));
                        Utils.appendLogger("user downloaded");
                    } else {
                        // Dersom response på DELETE
                        displayToast(getActivity(), getString(R.string.no_downloading));
                    }
                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }
}
