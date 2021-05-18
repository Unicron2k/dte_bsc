package com.example.thelastdeath.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.thelastdeath.R;
import com.example.thelastdeath.adapter.UserProgramSpinnerAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewUserProgramSessionFragment extends Fragment {
    private final Calendar myCalendar = Calendar.getInstance();
    private DataViewModel dataViewModel;
    private Spinner spinner;
    private UserProgramSpinnerAdapter userProgramSpinnerAdapter;
    private List<UserProgram> userPrograms;
    private TextView tvDate;


    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    public NewUserProgramSessionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set view and subscribe to Rest API
        View view = inflater.inflate(R.layout.fragment_new_user_program_session, container, false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        subscribeToErrors();
        subscribeToApiResponse();

        // Initialize layout fields
        spinner = view.findViewById(R.id.spinner_existing_user_program_id);

        Button calendarButton = view.findViewById(R.id.newUserProgramSession_button_calendar);
        tvDate = view.findViewById(R.id.newUserProgarmSession_TV_currentDate);
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        calendarButton.setOnClickListener(v -> new DatePickerDialog(requireActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        Switch switchUseDateToday = view.findViewById(R.id.newuserprogram_useTiming);
        switchUseDateToday.setOnClickListener(v -> Utils.setButtonVisibility(new Button[]{calendarButton}, !switchUseDateToday.isChecked()));

        LinearLayout numberPickerLayout = view.findViewById(R.id.numberPickerLayout);
        NumberPicker hourPicker = view.findViewById(R.id.newUserProgram_NumberPicker_hour);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(24);
        NumberPicker minutePicker = view.findViewById(R.id.newUserProgram_NumberPicker_minute);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        NumberPicker secondPicker = view.findViewById(R.id.newUserProgram_NumberPicker_second);
        secondPicker.setMinValue(1);
        secondPicker.setMaxValue(59);

        EditText etDescription = view.findViewById(R.id.newUserProgramSession_description_et);
        EditText etExtraText = view.findViewById(R.id.newUserProgramSession_extrajsondata_et);

        Button postButton = view.findViewById(R.id.newUserProgramSession_button_post);
        postButton.setOnClickListener(v -> {
            if (Utils.isEditTextFieldsNotEmpty(new EditText[]{etDescription}, getActivity())) {
                if (switchUseDateToday.isChecked())
                    tvDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

                if (tvDate.getText().toString().equals(""))
                    Utils.displayToast(getActivity(), getString(R.string.newUserProgramSession_warning_pick_date));
                else {
                    long user_program_id = userPrograms.get(spinner.getSelectedItemPosition()).getId();
                    String date1 = tvDate.getText().toString();
                    String description = etDescription.getText().toString();
                    String extra_json_data = etExtraText.getText().toString();
                    int time_spent = (hourPicker.getValue() * 60 * 60) + (minutePicker.getValue() * 60) + secondPicker.getValue();

                    dataViewModel.postUserProgramSession(
                            getActivity(),
                            user_program_id,
                            date1,
                            time_spent,
                            description,
                            extra_json_data
                    );
                    Utils.emptyEditTextFields(new EditText[]{etDescription, etExtraText});
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getUserFromServer();
        setUpSpinner();

        // Redirect user to HomeFragment if already signed in
        UtilsFirebase.redirectIfLoggedOut(view);
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tvDate.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * Sets up spinner with AppProgramType
     */
    private void setUpSpinner() {
        userPrograms = new ArrayList<>();
        userProgramSpinnerAdapter = new UserProgramSpinnerAdapter(getActivity(), R.layout.spinner_category_item, userPrograms);
        spinner.setAdapter(userProgramSpinnerAdapter);
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

                    // Populate entity.User object and notify Spinner's adapter
                    User user = apiResponse.getUser();
                    if (user != null) {
                        userPrograms.addAll(user.getUser_programs());
                        userProgramSpinnerAdapter.notifyDataSetChanged();
                    }


                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }

}

/*
_api_key=[din-api-key]
user_program_id (obligatorisk)
date (format:yyyy-mm-dd)
time_spent (desimaltall). Antall sekunder brukt på økta, evt. med en desimal.
description (tekst). Kort beskrivelse av økta.
extra_json_data (tekst).
*/
