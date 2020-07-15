package com.example.bipotrack;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.bipotrack.database.CRUD.SelectedMoodTable;
import com.example.bipotrack.database.entites.Note;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static Boolean created = false;
    private static CalendarView calendarView;


    private static CalendarFragment INSTANCE;
    public static CalendarFragment getInstance(){
        return INSTANCE;
    }

    public CalendarFragment() {
        INSTANCE = this;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment INSTANCE = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        INSTANCE.setArguments(args);
        return INSTANCE;
    }

    public static void refresh(View view) {
        Log.e("Clicked", "<<<<<<<<<<<<<<< refresh" );
        Vector<String> datesThatContainMood =  SelectedMoodTable.getInstance().getDatesContainsMood();
        List<EventDay> events = new ArrayList<>();

        for (String  date :  datesThatContainMood) {
            Log.e("Clicked", "<<<<<<<<<<<<<<<" + date);
            Date moodDate  = null;
            try {
                moodDate = java.text.DateFormat.getDateInstance().parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(moodDate);
            events.add(new EventDay(calendar, R.drawable.mood));
        }
        calendarView.setEvents(events);
        refreshNotesList(Utils.getCurrentTimeAndDate().getDate(),view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    public static View myview;

    static TextView  dateOfNotes;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        created  = true;
        myview =  view;
        calendarView = view.findViewById(R.id.calendarView);
        dateOfNotes = getView().findViewById(R.id.text_notes_date);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                Date date = clickedDayCalendar.getTime();
                String myDate  = java.text.DateFormat.getDateInstance().format(date);
                refreshNotesList(myDate,view);
                Log.e("Clicked", myDate);
            }
        });
        refresh(view);
    }


    static RecyclerView recyclerView;
    static  RecyclerAdapter recyclerAdapter;
    static Vector<Note> notesOfTheDay;
    public static void refreshNotesList(String date, View view){
        /***/
        SelectedMoodTable selectedMoodTable = SelectedMoodTable.getInstance();
        notesOfTheDay = selectedMoodTable.getNotesByDate(date);
        dateOfNotes.setText(date);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(notesOfTheDay,R.layout.row_item);
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    static ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(notesOfTheDay, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

}
