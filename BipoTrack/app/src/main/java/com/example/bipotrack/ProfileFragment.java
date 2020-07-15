package com.example.bipotrack;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bipotrack.database.CRUD.SelectedMoodTable;
import com.example.bipotrack.database.entites.Note;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static Vector<String> notes;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static Boolean created = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    Button shareBtn;
    TextInputEditText password, startDate, endDate;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        created  = true;
        shareBtn =  view.findViewById(R.id.sharebtn);
        password = view.findViewById(R.id.password);
        startDate = view.findViewById(R.id.habit_date_picker);
        endDate = view.findViewById(R.id.habit_endDate_picker);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Eroor","DATE PICKER CLICKED");
                openDatePicker(startDate);
            }
        });
        startDate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                openDatePicker(startDate);
                return false;
            }
        });
        startDate.setOnCapturedPointerListener(new View.OnCapturedPointerListener() {
            @Override
            public boolean onCapturedPointer(View view, MotionEvent event) {
                openDatePicker(startDate);
                return false;
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Eroor","DATE PICKER CLICKED");
                openDatePicker(endDate);
            }
        });
        endDate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                openDatePicker(endDate);
                return false;
            }
        });
        endDate.setOnCapturedPointerListener(new View.OnCapturedPointerListener() {
            @Override
            public boolean onCapturedPointer(View view, MotionEvent event) {
                openDatePicker(endDate);
                return false;
            }
        });


        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        refresh();
    }



    private void openDatePicker(TextInputEditText textfield){

        final MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        //The following block is for to not be able to choose a date greater than today

        CalendarConstraints.Builder calendarConstraintsBuilder = new CalendarConstraints.Builder();
        Calendar today = Calendar.getInstance();
        calendarConstraintsBuilder.setValidator( DateValidatorPointBackward.before(today.getTimeInMillis()));

        builder.setCalendarConstraints(calendarConstraintsBuilder.build());
        Calendar calendar = Calendar.getInstance();
        builder.setSelection(calendar.getTimeInMillis());
        //Log.e("lll" , MaterialDatePicker.todayInUtcMilliseconds() + "");
        final MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                //Put here the action that we want to do after selection the date
                //You can get the date using materialDatePicker.getHeaderText()
                textfield.setText(materialDatePicker.getHeaderText());
            }
        });
        materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
    }

    public static void refresh(){
         notes =  SelectedMoodTable.getInstance().getDatesContainsMood();
    }


    private void share(){


        String passwordtxt = password.getText().toString();
        String  startDateTxt = startDate.getText().toString();
        String  endDateTxt  = endDate.getText().toString();


        if(passwordtxt != null  && startDateTxt  != null && endDateTxt  != null &&
                passwordtxt.length() > 0  &&  startDateTxt.length() > 0 && endDateTxt.length() > 0 )
        {
            Vector<Note>  moods = SelectedMoodTable.getInstance().getMoodsIntervalDate(startDateTxt,endDateTxt);
            // >>>>>> use moods to generate the report between startDateTxt and startDateTxt and use passwordtxt to encryp the pdf


        }
        else {
            Snackbar sb = Snackbar.make(getView(), "Please enter your password, start and end date to generate the report", Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundResource(R.color.danger);
            sb.show();
        }
    }
}
