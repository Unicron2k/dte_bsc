package com.shadyshrimp.lab9_2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.shadyshrimp.lab9_2.exercise.Exercise;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DetailsFragment extends Fragment {

    private ImageView ivIcon;
    private TextView tvExerciseName;
    private WebView wvExerciseDescription;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_exercise_details, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivIcon = view.findViewById(R.id.ivIcon);
        tvExerciseName = view.findViewById(R.id.tvExerciseName);
        wvExerciseDescription = view.findViewById(R.id.wvExerciseDescription);

        assert getArguments() != null;
        Exercise exercise = DetailsFragmentArgs.fromBundle(getArguments()).getExercise();

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar())
                .setTitle(getResources().getString(R.string.str_details_for_exercise, exercise.getName()));

        String exerciseName = exercise.getName();
        String exerciseDescription = exercise.getDescription();
        String exerciseIcon = exercise.getIcon();
        String exerciseInfoboxColor = exercise.getInfobox_color();

        String iconUrl = "https://tusk.systems/trainingapp/icons/"+exerciseIcon;
        Picasso.get().load(iconUrl).placeholder(R.drawable.ic_hourglass_empty_black_24dp).into(ivIcon);

        view.setBackgroundColor(Color.parseColor(exerciseInfoboxColor));

        tvExerciseName.setText(exerciseName);

        assert exerciseDescription != null;
        String exerciseDetailsDescriptionHTML = "<hr>" + exerciseDescription.replace("\n",exerciseDescription.contains("<p>")?"":"</br>");
        wvExerciseDescription.setBackgroundColor(Color.parseColor(exerciseInfoboxColor));
        wvExerciseDescription.loadData(exerciseDetailsDescriptionHTML, "text/html", "UTF-8");

    }
}
