package com.example.bipotrack;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bipotrack.database.entites.Note;
import com.example.bipotrack.database.CRUD.SelectedMoodTable;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoodFragment extends Fragment {



    private static MoodFragment INSTANCE;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static String selected_mood = null;

    ImageView depressed_img;
    ImageView angry_img;
    ImageView meh_img;
    ImageView good_img;
    ImageView happy_img;

    MaterialTextView feeling_text;
    MaterialTextView mood_select_text;
    MaterialButton feeling_quick_add;
    MaterialButton feeling_write;


    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    Vector<Note> notesOfTheDay;


    //list of notes
    TextView dateOfNotes;
    MaterialButton btnSelectDateNotes;


    public MoodFragment() {
        // Required empty public constructor
        INSTANCE = this;
    }

    public static MoodFragment getInstance(){
        return INSTANCE;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoodFragment newInstance(String param1, String param2) {
        MoodFragment fragment = new MoodFragment();
                Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        INSTANCE = fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_mood, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initFabButton();
        initMoodsImageViews();
        initFeelingDialog();
        final List<ImageView> moods = getMoods();
        for (ImageView m : moods){
            addMoodIconOnClickActionListener(m);
        }
        initNotesList();
    }

    private void initFeelingDialog(){
        feeling_text = getView().findViewById(R.id.mood_feeling_text);
        mood_select_text = getView().findViewById(R.id.mood_select_text);

        feeling_quick_add = getView().findViewById(R.id.mood_feeling_btn_add);
        feeling_write = getView().findViewById(R.id.mood_feeling_btn_write);

        feeling_quick_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFeelingZoneInvisible();
                if(MoodFragment.selected_mood != null) {
                    SelectedMoodTable selectedMoodTable = SelectedMoodTable.getInstance();
                    selectedMoodTable.insertData(MoodFragment.selected_mood, getView());
                    selectedMoodTable.getAllData();
                    refreshNotesList(Utils.getCurrentTimeAndDate().getDate());
                }
            }
        });
        feeling_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFeelingZoneInvisible();
                openAddMoodNotesDialog("Add Notes");
            }
        });
    }
    private void initMoodsImageViews() {
     depressed_img = getView().findViewById(R.id.depressed);
     angry_img = getView().findViewById(R.id.angry);
     meh_img = getView().findViewById(R.id.meh);
     good_img = getView().findViewById(R.id.good);
     happy_img = getView().findViewById(R.id.happy);
    }

    private List<ImageView> getMoods(){
        List<ImageView> moods = new ArrayList<ImageView>();
        moods.add(depressed_img);
        moods.add(angry_img);
        moods.add(meh_img);
        moods.add(good_img);
        moods.add(happy_img);
        return moods;
    }


    private void addMoodIconOnClickActionListener(final ImageView mood) {
        final List<ImageView> moods = getMoods();
        mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood_select_text.setText("I feel ");
                feeling_quick_add.setVisibility(View.VISIBLE);
                feeling_write.setVisibility(View.VISIBLE);
                String str_mood = getResources().getResourceEntryName(mood.getId()).toUpperCase();
                MoodFragment.selected_mood = str_mood;
                feeling_text.setText(str_mood);
                Log.e("Mood", str_mood);
                for (ImageView m : moods) {
                    if( m != mood) {
                        m.setImageAlpha(100);
                    } else {
                        m.setImageAlpha(255);
                    }
                }
            }
        });
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
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

    public void initNotesList(){
        this.dateOfNotes = getView().findViewById(R.id.text_notes_date);
        this.dateOfNotes.setText(Utils.getCurrentTimeAndDate().getDate());
        this.btnSelectDateNotes = getView().findViewById(R.id.btn_notes_select_date);
        this.btnSelectDateNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        refreshNotesList(Utils.getCurrentTimeAndDate().getDate());
    }

    public void refreshNotesList(String date){
        /***/
        SelectedMoodTable selectedMoodTable = SelectedMoodTable.getInstance();
        this.notesOfTheDay = selectedMoodTable.getNotesByDate(date);

        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(notesOfTheDay,R.layout.row_item);
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    FloatingActionButton fab_add, fab_cal, fab_share;
    Animation fabOpen, fabClose, fabClockWise, fabAntiClockWise;
    Boolean isOpen = false;

    private void initFabButton(){
        /*Fab Button */
        fab_add = getView().findViewById(R.id.add_button);
        fab_cal = getView().findViewById(R.id.share_button);
        fab_share = getView().findViewById(R.id.calendar_button);

        fabOpen = AnimationUtils.loadAnimation(getView().getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getView().getContext(), R.anim.close_fab);
        fabClockWise = AnimationUtils.loadAnimation(getView().getContext(), R.anim.rotate_clockwise);
        fabAntiClockWise = AnimationUtils.loadAnimation(getView().getContext(), R.anim.rotate_anticlockwise);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    fab_cal.startAnimation(fabClose);
                    fab_share.startAnimation(fabClose);
                    fab_add.startAnimation(fabClockWise);
                    fab_cal.setClickable(false);
                    fab_share.setClickable(false);
                    isOpen = false;
                }else{
                    fab_cal.startAnimation(fabOpen);
                    fab_share.startAnimation(fabOpen);
                    fab_add.startAnimation(fabAntiClockWise);
                    fab_cal.setClickable(true);
                    fab_share.setClickable(true);
                    isOpen = true;
                }
            }
        });


        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.menu.selectTab(MainActivity.menu.getTabAt(1));
            }
        });
        fab_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.menu.selectTab(MainActivity.menu.getTabAt(2));
            }
        });

    }


    private void openAddMoodNotesDialog(String title){
        AddMoodNotesDialog.display(getActivity().getSupportFragmentManager(), MoodFragment.selected_mood, getView(),title);
    }

    private void resetFeelingZoneInvisible(){
        feeling_quick_add.setVisibility(View.GONE);
        feeling_write.setVisibility(View.INVISIBLE);
        mood_select_text.setText("Please select your mood");
        feeling_text.setText("");
    }


    private void openDatePicker(){

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
                dateOfNotes.setText(materialDatePicker.getHeaderText());
                Log.e("Tag", "=============>>" + materialDatePicker.getHeaderText() + materialDatePicker.getSelection());
                refreshNotesList(materialDatePicker.getHeaderText());
            }
        });
        materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
    }
}
