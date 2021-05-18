package com.example.thelastdeath.fragment.userprogram;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
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
import com.example.thelastdeath.adapter.UserProgramSpinnerAdapter;
import com.example.thelastdeath.entity.AppExercise;
import com.example.thelastdeath.entity.User;
import com.example.thelastdeath.entity.UserProgram;
import com.example.thelastdeath.entity.UserProgramSession;
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
import java.util.List;
import java.util.Locale;

import static com.example.thelastdeath.utils.Utils.displayToast;

public class UserProgramSessionDetailsFragment extends Fragment {
    private final Calendar myCalendar = Calendar.getInstance();
    private DataViewModel dataViewModel;
    private UserProgramSession userProgramSession;
    private Spinner spinner;
    private TextView tvDate;
    private List<UserProgram> userPrograms;
    private UserProgramSpinnerAdapter userProgramSpinnerAdapter;


    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private List<AppExercise> appExercises;

    public UserProgramSessionDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        userProgramSession = UserProgramSessionDetailsFragmentArgs.fromBundle(getArguments()).getUserProgramSessionObject();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set view and subscribe to Rest API
        View view = inflater.inflate(R.layout.fragment_user_program_session_details, container, false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        subscribeToErrors();
        subscribeToApiResponse();

        // Initialize layout fields
        spinner = view.findViewById(R.id.userProgramSessionDetails_spinner_existing_user_program_id);
        tvDate = view.findViewById(R.id.userProgramSessionDetails_TV_currentDate);
        tvDate.setText(userProgramSession.getDate());
        Button calendarButton = view.findViewById(R.id.userProgramSessionDetails_button_calendar);
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        calendarButton.setOnClickListener(v -> new DatePickerDialog(requireActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        int time_spent = userProgramSession.getTime_spent();
        NumberPicker secondPicker = view.findViewById(R.id.newUserProgram_NumberPicker_second);
        secondPicker.setMinValue(1);
        secondPicker.setMaxValue(59);
        secondPicker.setValue(time_spent % 60);

        NumberPicker minutePicker = view.findViewById(R.id.newUserProgram_NumberPicker_minute);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setValue((time_spent / 60) % 60);

        NumberPicker hourPicker = view.findViewById(R.id.newUserProgram_NumberPicker_hour);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(24);
        hourPicker.setValue((time_spent / 60) / 60);

        EditText etDescription = view.findViewById(R.id.newUserProgramSession_description_et);
        etDescription.setText(userProgramSession.getDescription());
        EditText etExtraText = view.findViewById(R.id.newUserProgramSession_extrajsondata_et);
        etExtraText.setText(userProgramSession.getExtra_json_data());
        Button putUserProgramSession = view.findViewById(R.id.newUserProgramSession_button_post);
        putUserProgramSession.setOnClickListener(v -> {
            if (Utils.isEditTextFieldsNotEmpty(new EditText[]{etDescription}, getActivity()))
                if (tvDate.getText().toString().equals(""))
                    Utils.displayToast(getActivity(), getString(R.string.newUserProgramSession_warning_pick_date));
                else {
                    long user_program_id = userPrograms.get(spinner.getSelectedItemPosition()).getId();
                    String date1 = tvDate.getText().toString();
                    String description = etDescription.getText().toString();
                    String extra_json_data = etExtraText.getText().toString();
                    int time_spent1 = (hourPicker.getValue() * 60 * 60) + (minutePicker.getValue() * 60) + secondPicker.getValue();

                    Utils.displayToast(getActivity(), "hei");
                    dataViewModel.putUserProgramSession(
                            getActivity(),
                            userProgramSession.getRid(),
                            user_program_id,
                            date1,
                            time_spent1,
                            description,
                            extra_json_data);
                }
        });

        Button deleteSessionButton = view.findViewById(R.id.userProgramSessionDetails_delete_button);
        deleteSessionButton.setOnClickListener(v ->
                new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.userProgramSessionDetails_alert_title))
                .setMessage(getString(R.string.userProgramSessionDetails_alert_message))
                .setPositiveButton(getString(R.string.userprofile_alert_yes), (dialogInterface, i) -> {
                    // Confirm deletion
                    dataViewModel.deleteUserProgramSession(
                            getActivity(),
                            userProgramSession.getRid());
                    Utils.displayToast(getActivity(), getString(R.string.userProgramSessionDetails_deleted));
                    Navigation.findNavController(view).navigate(R.id.homeFragment, null, Utils.getAnimation());
                })
                .setNegativeButton(getString(R.string.userprofile_alert_no), (dialogInterface, i) -> {
                    // Cancel deletion
                    displayToast(getActivity(), getString(R.string.userProgramSessionDetails_alert_delete_cancelled));
                })
                .show());

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        dataViewModel.getAllAppExercises(getActivity(), false);
        setUpSpinner();
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
        userPrograms = new ArrayList<>();
        userProgramSpinnerAdapter = new UserProgramSpinnerAdapter(getActivity(), R.layout.spinner_category_item, userPrograms);
        spinner.setAdapter(userProgramSpinnerAdapter);
    }


    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tvDate.setText(sdf.format(myCalendar.getTime()));
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
                        userPrograms.addAll(user.getUser_programs());
                        userProgramSpinnerAdapter.notifyDataSetChanged();
                        TextView tvCurrentProgram = requireView().findViewById(R.id.userProgramSessionDetails_tv_currentUserProgram);
                        for (UserProgram userProgram : userPrograms)
                            if (userProgram.getId() == userProgramSession.getUser_program_id())
                                tvCurrentProgram.setText(userProgram.getName());
                    }

                    List<AppExercise> appExercisesFromServer = apiResponse.getAllAppAxercises();
                    if (appExercisesFromServer != null)
                        appExercises = appExercisesFromServer;


                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }

}