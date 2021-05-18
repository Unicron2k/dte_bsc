package com.shadyshrimp.lab9_2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;
import com.shadyshrimp.lab9_2.program.Program;
import com.shadyshrimp.lab9_2.program.ProgramAdapter;
import com.shadyshrimp.lab9_2.program.ProgramData;
import com.shadyshrimp.lab9_2.program.ProgramViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class ProgramsFragment extends Fragment {

    private ListView lvItemList;
    private ProgramViewModel programViewModel;
    private boolean mDownloading = false;
    private ProgressBar loadingIndicator;
    private ProgramAdapter programAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_exercise_list, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavOptions.Builder navOptionsBuilder = new NavOptions.Builder();
        navOptionsBuilder.setEnterAnim(R.anim.slide_in_left);
        navOptionsBuilder.setExitAnim(R.anim.slide_out_right);
        navOptionsBuilder.setPopEnterAnim(R.anim.slide_in_right);
        navOptionsBuilder.setPopExitAnim(R.anim.slide_out_left);
        final NavOptions options = navOptionsBuilder.build();

        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(this.getActivity()));
        boolean userLoggedIn = sharedPreferences.getBoolean("USER_LOGGED_IN", false);
        if (!userLoggedIn) {
            NavHostFragment.findNavController(this).navigate(R.id.userGraph, null, options);
        }

        TextView tvDescription = view.findViewById(R.id.tvDescription);
        tvDescription.setText(getResources().getString(R.string.str_available_workout_programs));
        lvItemList = view.findViewById(R.id.lvItemList);
        loadingIndicator = view.findViewById(R.id.pbLoadingIndicator);
        lvItemList.setOnItemClickListener((parent, lvView, position, id) -> {
            Program program = Objects.requireNonNull(programViewModel.getProgramData().getValue()).getAllPrograms().get(position);
            ProgramsFragmentDirections.ActionExercises actionExercises = ProgramsFragmentDirections.actionExercises(program);
            actionExercises.setProgram(program);
            Navigation.findNavController(view).navigate(actionExercises);
        });
        programViewModel = new ViewModelProvider(this).get(ProgramViewModel.class);
        this.subscribe();

        lvItemList.post(this::download);
    }

    private void subscribe() {
        final Observer<ProgramData> ProgramDataObserver = programData -> {
            if (programData != null && programData.getAllPrograms() != null) {

                ArrayList<Program> alProgramsList = (ArrayList<Program>) programData.getAllPrograms();
                programAdapter = new ProgramAdapter(Objects.requireNonNull(this.getContext()), alProgramsList);

                lvItemList.setAdapter(programAdapter);

                loadingIndicator.setVisibility(View.INVISIBLE);
                mDownloading = false;
            }
        };

        programViewModel.getProgramData().observe(getViewLifecycleOwner(), ProgramDataObserver);

        final Observer<String> errorMessageObserver = errorMessage -> {
            //TODO: Beautify this...
            Log.e(getResources().getString(R.string.str_error_tag), errorMessage);
            loadingIndicator.setVisibility(View.INVISIBLE);
            mDownloading = false;
        };
        programViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessageObserver);
    }

    public void download() {
        if (programViewModel != null && !mDownloading) {
            loadingIndicator.setVisibility(View.VISIBLE);
            mDownloading = true;
            programViewModel.startDownload(Objects.requireNonNull(this.getContext()));
        }
    }
}
