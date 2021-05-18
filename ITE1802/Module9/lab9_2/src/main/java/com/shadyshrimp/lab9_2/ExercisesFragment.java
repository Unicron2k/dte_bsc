package com.shadyshrimp.lab9_2;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.shadyshrimp.lab9_2.exercise.Exercise;
import com.shadyshrimp.lab9_2.exercise.ExerciseAdapter;
import com.shadyshrimp.lab9_2.exercise.ExerciseData;
import com.shadyshrimp.lab9_2.exercise.ExerciseViewModel;
import com.shadyshrimp.lab9_2.program.Program;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class ExercisesFragment extends Fragment {


    private ListView lvItemList;
    private ExerciseViewModel exerciseViewModel;
    private boolean mDownloading = false;
    private ProgressBar pbLoadingIndicator;
    private ExerciseAdapter exerciseAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_exercise_list, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvItemList = view.findViewById(R.id.lvItemList);
        pbLoadingIndicator = view.findViewById(R.id.pbLoadingIndicator);
        TextView tvDescription = view.findViewById(R.id.tvDescription);


        assert getArguments() != null;
        Program program = ExercisesFragmentArgs.fromBundle(getArguments()).getProgram();
        long programId = program.getId();
        String programName = program.getName();
        String programDescription = program.getDescription();

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(programName);
        tvDescription.setText(programDescription);

        lvItemList.setOnItemClickListener((parent, lvView, position, id) -> {
            Exercise exercise = Objects.requireNonNull(exerciseViewModel.getExerciseData().getValue()).getAllExercises().get(position);
            ExercisesFragmentDirections.ActionDetails actionDetails = ExercisesFragmentDirections.actionDetails(exercise);
            actionDetails.setExercise(exercise);
            Navigation.findNavController(view).navigate(actionDetails);
        });

        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        this.subscribe();

        lvItemList.post(()-> download(programId));

    }

    private void subscribe() {
        final Observer<ExerciseData> ExerciseDataObserver = exerciseData -> {
            if (exerciseData != null && exerciseData.getAllExercises() != null) {

                ArrayList<Exercise> allExercisesList = (ArrayList<Exercise>) exerciseData.getAllExercises();
                exerciseAdapter = new ExerciseAdapter(Objects.requireNonNull(getContext()), allExercisesList);

                lvItemList.setAdapter(exerciseAdapter);

                pbLoadingIndicator.setVisibility(View.INVISIBLE);
                mDownloading = false;
            }
        };

        exerciseViewModel.getExerciseData().observe(getViewLifecycleOwner(), ExerciseDataObserver);

        final Observer<String> errorMessageObserver = errorMessage -> {
            //TODO: Beautify this...
            Log.e(getString(R.string.str_error_tag), errorMessage);
            pbLoadingIndicator.setVisibility(View.INVISIBLE);
            mDownloading = false;
        };
        exerciseViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessageObserver);
    }

    public void download(long exerciseProgramId) {
        if (exerciseViewModel != null && !mDownloading) {
            pbLoadingIndicator.setVisibility(View.VISIBLE);
            mDownloading = true;
            exerciseViewModel.startDownload(getContext(), exerciseProgramId);
        }
    }
}
