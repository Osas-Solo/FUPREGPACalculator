package com.ostech.fupregpacalculator;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class AddCoursesDialogFragment extends DialogFragment {
    private static final String TAG = AddCoursesDialogFragment.class.getCanonicalName();

    private static final String ARG_ADDITIONAL_NUMBER_OF_COURSES = "number_of_courses";
    private static final String ARG_RESULT = "dialog_result";

    public AppCompatEditText addCoursesEditText;
    public AddCoursesDialogListener listener;

    public interface AddCoursesDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    public static AddCoursesDialogFragment newInstance() {
        AddCoursesDialogFragment fragment = new AddCoursesDialogFragment();

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_courses, null);

        addCoursesEditText = view.findViewById(R.id.add_courses_dialog_course_number_edit_text);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.add_courses_prompt)
                .setPositiveButton(R.string.add_courses, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogPositiveClick(AddCoursesDialogFragment.this);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddCoursesDialogFragment.this.getDialog().cancel();
                    }
                })
                .create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddCoursesDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement AddCoursesDialogListener");
        }
    }
}