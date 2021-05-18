package com.example.thelastdeath.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.thelastdeath.R;
import com.example.thelastdeath.entity.UserStats;
import com.example.thelastdeath.entity.helper.ApiError;
import com.example.thelastdeath.entity.helper.ApiResponse;
import com.example.thelastdeath.utils.api.UtilsAPI;
import com.example.thelastdeath.viewmodel.DataViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.thelastdeath.utils.Utils.displayToast;
import static com.example.thelastdeath.utils.firebase.UtilsFirebase.redirectIfLoggedOut;

public class UserStatsFragment extends Fragment {
    private TextView tvLast7DaysSessions, tvLast7DaysTimeSpent;
    private TextView tvCurrentWeekSessions, tvCurrentWeekTimeSpent;
    private TextView tvCurrentMonthSessions, tvCurrentMonthTimeSpent;
    private TextView tvLast30DaysSessions, tvLast30DaysTimeSpent;
    private TextView tvCurrentYearSessions, tvCurrentYearTimeSpent;

    private DataViewModel dataViewModel;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    public UserStatsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set view and subscribe to Rest API
        View view = inflater.inflate(R.layout.fragment_userstats, container, false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        subscribeToErrors();
        subscribeToApiResponse();

        // Initialize TextViews from layout
        tvLast7DaysSessions = view.findViewById(R.id.userStats_Last7Days_sessionCount);
        tvLast7DaysTimeSpent = view.findViewById(R.id.userStats_Last7Days_timeSpent);
        tvCurrentWeekSessions = view.findViewById(R.id.userStats_CurrentWeek_sessionCount);
        tvCurrentWeekTimeSpent = view.findViewById(R.id.userStats_CurrentWeek_timeSpent);
        tvCurrentMonthSessions = view.findViewById(R.id.userStats_CurrentMonth_sessionCount);
        tvCurrentMonthTimeSpent = view.findViewById(R.id.userStats_CurrentMonth_timeSpent);
        tvLast30DaysSessions = view.findViewById(R.id.userStats_Last30Days_sessionCount);
        tvLast30DaysTimeSpent = view.findViewById(R.id.userStats_Last30Days_timeSpent);
        tvCurrentYearSessions = view.findViewById(R.id.userStats_CurrentYear_sessionCount);
        tvCurrentYearTimeSpent = view.findViewById(R.id.userStats_CurrentYear_timeSpent);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Bruk cachet verdi på user, hvis den eksisterer. Hvis ikke last ned.
        getUserStatsFromServer();
        // Redirect to LoginFragment if not logged in
        redirectIfLoggedOut(view);

    }

    /**
     * Download UserStats from server
     */
    private void getUserStatsFromServer() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            dataViewModel.getUserStats(getActivity(), firebaseUser.getUid(), false);
        else
            displayToast(getActivity(), getString(R.string.warning_firebase_not_logged_in));
    }

    /**
     * Subscribe for error Error-codes
     */
    private void subscribeToErrors() {
        if (apiErrorObserver == null) {
            // Observe changes in errorMessage
            apiErrorObserver = apiError -> {
                if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    if (apiError != null)
                        UtilsAPI.displayApiErrorResponseMessage(apiError, getActivity());
                }
            };
            dataViewModel.getApiError().observe(getViewLifecycleOwner(), apiErrorObserver);
        }
    }

    /**
     * Subscribe to server-respons and update UserStats-fields on success
     */
    private void subscribeToApiResponse() {
        if (apiResponseObserver == null) {
            // Observe changes
            apiResponseObserver = apiResponse -> {
                if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    // Display response in Toast
                    UtilsAPI.displayApiResponseMessage(apiResponse, getActivity());
                    UserStats userStats = apiResponse.getUserStats();
                    if (userStats != null) {
                        // Positive response from GET
                        tvLast7DaysSessions.setText(getString(R.string.userstats_sessioncount_substring, String.valueOf(userStats.getLast7Days().getSessionCount())));
                        tvCurrentWeekSessions.setText(getString(R.string.userstats_sessioncount_substring, String.valueOf(userStats.getCurrentWeek().getSessionCount())));
                        tvCurrentMonthSessions.setText(getString(R.string.userstats_sessioncount_substring, String.valueOf(userStats.getCurrentMonth().getSessionCount())));
                        tvLast30DaysSessions.setText(getString(R.string.userstats_sessioncount_substring, String.valueOf(userStats.getLast30days().getSessionCount())));
                        tvCurrentYearSessions.setText(getString(R.string.userstats_sessioncount_substring, String.valueOf(userStats.getCurrentYear().getSessionCount())));
                        tvLast7DaysTimeSpent.setText(getString(R.string.userstats_timespent_substring, String.valueOf(userStats.getLast7Days().getTimeSpent())));
                        tvCurrentWeekTimeSpent.setText(getString(R.string.userstats_timespent_substring, String.valueOf(userStats.getCurrentWeek().getTimeSpent())));
                        tvCurrentMonthTimeSpent.setText(getString(R.string.userstats_timespent_substring, String.valueOf(userStats.getCurrentMonth().getTimeSpent())));
                        tvLast30DaysTimeSpent.setText(getString(R.string.userstats_timespent_substring, String.valueOf(userStats.getLast30days().getTimeSpent())));
                        tvCurrentYearTimeSpent.setText(getString(R.string.userstats_timespent_substring, String.valueOf(userStats.getCurrentYear().getTimeSpent())));
                    } else
                        displayToast(getActivity(), getString(R.string.no_downloading));
                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }
}
