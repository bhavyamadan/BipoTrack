package com.example.bipotrack;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class HabitDialog extends DialogFragment {

    public static final String TAG = "example_dialog";

    private Toolbar toolbar;

    public static HabitDialog display(FragmentManager fragmentManager) {
        HabitDialog exampleDialog = new HabitDialog();
        exampleDialog.show(fragmentManager, TAG);
        return exampleDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.habit_dialog, container, false);

        toolbar = view.findViewById(R.id.toolbar);

        return view;
    }
    private TextInputEditText datepicker,mytimePicker;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mytimePicker = view.findViewById(R.id.habit_time_picker);
        datepicker = view.findViewById(R.id.habit_date_picker);
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Eroor","DATE PICKER CLICKED");
                openDatePicker();
            }
        });
        datepicker.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                openDatePicker();
                return false;
            }
        });
        datepicker.setOnCapturedPointerListener(new View.OnCapturedPointerListener() {
            @Override
            public boolean onCapturedPointer(View view, MotionEvent event) {
                openDatePicker();
                return false;
            }
        });
        mytimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Eroor","DATE PICKER CLICKED");
                openTimePicker();
            }
        });
        mytimePicker.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                openTimePicker();
                return false;
            }
        });
        mytimePicker.setOnCapturedPointerListener(new View.OnCapturedPointerListener() {
            @Override
            public boolean onCapturedPointer(View view, MotionEvent event) {
                openTimePicker();
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setTitle("Create Habit");
        toolbar.inflateMenu(R.menu.menu_dialog);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.e("Action", item.getTitle()+"");
                dismiss();
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }
    
    
    private void openDatePicker(){

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        //The following block is for to not be able to choose a date greater than today
        CalendarConstraints.Builder calendarConstraintsBuilder = new CalendarConstraints.Builder();
        calendarConstraintsBuilder.setValidator(DateValidatorPointBackward.now());
        builder.setCalendarConstraints(calendarConstraintsBuilder.build());
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        final MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                //Put here the action that we want to do after selection the date
                //You can get the date using materialDatePicker.getHeaderText()
                datepicker.setText(materialDatePicker.getHeaderText());
            }
        });
        materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
    }

    private void openTimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                //After OK button
                mytimePicker.setText(selectedHour + ":" + selectedMinute);
            }
        }, minute ,hour , false);//Yes 24 hour time
        //mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}