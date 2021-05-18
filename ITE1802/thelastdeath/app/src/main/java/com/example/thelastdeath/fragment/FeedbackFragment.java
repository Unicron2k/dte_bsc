package com.example.thelastdeath.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.thelastdeath.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class FeedbackFragment extends Fragment {

    private EditText etName;
    private EditText etMail;
    private EditText etFeedback;

    private Spinner spnrFeedbackCategory;

    private Button btCancel;
    private Button btSubmit;

    public FeedbackFragment(){
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
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        etName = view.findViewById(R.id.etName);
        etMail = view.findViewById(R.id.etMail);
        etFeedback = view.findViewById(R.id.etFeedback);
        spnrFeedbackCategory = view.findViewById(R.id.spnrFeedbackCategory);
        btCancel = view.findViewById(R.id.btCancel);
        btSubmit = view.findViewById(R.id.btSubmit);


        ArrayAdapter<CharSequence> feedbackAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.feedback_categories,R.layout.spinner_feedback_item);

        // TODO: Make this fancy, with icons in the dropdown menu etc.
        spnrFeedbackCategory.setAdapter(feedbackAdapter);
        btCancel.setOnClickListener(v -> Navigation.findNavController(Objects.requireNonNull(getView())).navigateUp());

        btSubmit.setOnClickListener( v -> {
            // TODO: Actually implement sending of feedback...
            String name = etName.getText().toString();
            String email = etMail.getText().toString();
            String feedback = etFeedback.getText().toString();
            String category = spnrFeedbackCategory.getSelectedItem().toString();

            Toast.makeText(getContext(), "Feedback submitted!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(Objects.requireNonNull(getView())).navigateUp();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if(fbUser!=null) {
            String name = fbUser.getDisplayName();
            String email = fbUser.getEmail();
            if(name!=null) {
                etName.setText(name);
            }
            if(email!=null) {
                etMail.setText(email);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Redirect user to HomeFragment if already signed in
        //redirectIfLoggedOut(view);
        //((MainActivity) requireActivity()).hideBottomNav();

    }

}
