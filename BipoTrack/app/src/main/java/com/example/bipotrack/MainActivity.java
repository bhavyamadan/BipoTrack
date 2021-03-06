package com.example.bipotrack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.bipotrack.database.entites.DatabaseHelper;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {


    DatabaseHelper myDb;
    static TabLayout menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Database */
        myDb = new DatabaseHelper(this);
        myDb.onCreate(myDb.getWritableDatabase());


        //myDb.onUpgrade(myDb.getWritableDatabase(),0,0);

        /** NoteTable
        Log.e("DATABESE", "------------ NoteTable ----------");
        NoteTable noteTable = new NoteTable(myDb.getWritableDatabase(),getBaseContext());
        long noteId = noteTable.insertData("I feel too bad");
        noteTable.getAllData();


        /** NoteMoodTable
        Log.e("DATABESE", "------------ NoteMoodTable ----------");
        NoteMoodsTable noteMoodsTable = new NoteMoodsTable(myDb.getWritableDatabase(),getBaseContext());
        HashMap<Integer,Integer> noteMoods = new HashMap<>();
        noteMoods.put(Moods.DEPRESSED, 10);
        noteMoods.put(Moods.MEH, 100);
        noteMoodsTable.insertData(noteMoods,noteId);
        noteMoodsTable.getAllData();
         */

        /** Menu **/
        menu = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.view_page);
        MoodFragment moodFrag = new MoodFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        CalendarFragment calendarFragment = new CalendarFragment();


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(moodFrag);
        viewPagerAdapter.addFragment(calendarFragment);
        viewPagerAdapter.addFragment(profileFragment);
        viewPager.setAdapter(viewPagerAdapter);

        menu.setupWithViewPager(viewPager);

        menu.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        menu.getTabAt(1).setIcon(R.drawable.ic_calendar_black_24dp);
        menu.getTabAt(2).setIcon(R.drawable.ic_person_black_24dp);
        menu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position =  menu.getSelectedTabPosition();
                if(position  == 1 && CalendarFragment.created == true) {
                    Log.e("Okay","selected Calendar");
                    CalendarFragment.refresh(CalendarFragment.myview);
                }
                if(position  == 2 && ProfileFragment.created == true) {
                    Log.e("Okay","share");
                    ProfileFragment.refresh();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
        /*

        // Calendar Date Picker
        Button datePickerBtn = findViewById(R.id.date_picker_btn);
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        //The following block is for to not be able to choose a date greater than today
        CalendarConstraints.Builder calendarConstraintsBuilder = new CalendarConstraints.Builder();
        calendarConstraintsBuilder.setValidator(DateValidatorPointBackward.now());
        builder.setCalendarConstraints(calendarConstraintsBuilder.build());
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());

        final MaterialDatePicker materialDatePicker = builder.build();
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                //Put here the action that we want to do after selection the date
                //You can get the date using materialDatePicker.getHeaderText()
                Toast.makeText(MainActivity.this,
                        materialDatePicker.getHeaderText() + " or" +
                                selection
                        , Toast.LENGTH_SHORT).show();
            }
        });
        // Calendar Range Date Picker
        Button rangeDatePickerBtn = findViewById(R.id.date_range_picker_btn);
        MaterialDatePicker.Builder<Pair<Long,Long>> rangeBuilder = MaterialDatePicker.Builder.dateRangePicker();
        rangeBuilder.setTitleText("SELECT RANGE DATE");
        final MaterialDatePicker materialRangeDatePicker = rangeBuilder.build();
        rangeDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open date picker when we click an the button
                materialRangeDatePicker.show(getSupportFragmentManager(), "DATE_RANGE_PICKER");
            }
        });
        materialRangeDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long,Long> rangeDate = (Pair<Long,Long>) selection;
                //Put here the action that we want to do after selection the date
                //You can get the date using materialDatePicker.getHeaderText()
                Toast.makeText(MainActivity.this,
                        materialRangeDatePicker.getHeaderText() + " or" +
                                rangeDate.first + "-" + rangeDate.second
                        , Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton floatingAddButton = findViewById(R.id.floating_action_button);
        floatingAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Add button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Time Picker
        Button timePickerBtn = findViewById(R.id.time_picker);
        timePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //After OK button
                        Toast.makeText(MainActivity.this, selectedHour + ":" + selectedMinute , Toast.LENGTH_SHORT).show();
                    }
                }, minute ,hour , false);//Yes 24 hour time
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        // Dialog confirmation
        Button confirmationDialogBtn = findViewById(R.id.confirmation_dialog_btn);
        confirmationDialogBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("This is the title")
                        .setMessage("This is a new message");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "CANCEL", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });

        // Dialog List with icon
        Button showDialogBtn = findViewById(R.id.list_dialog_with_item_btn);
        showDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemSelected = 1;
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(MainActivity.this);
                dialog.setTitle("This is the title");
                final String[] items = { "Doctor", "Family","Private"} ;
                Integer[] imagesId = {R.drawable.ic_add_24dp,R.drawable.ic_add_24dp,R.drawable.ic_add_24dp};
                final ListAdapterIconText listAdapter = new ListAdapterIconText(MainActivity.this, items, imagesId);
                dialog.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Selected item:" + items[which], Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });

        //Switcher
        final SwitchMaterial switcher = findViewById(R.id.switcher);
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, switcher.isChecked() + "", Toast.LENGTH_SHORT).show();

            }
        });

        //Slider
        Slider slider = findViewById(R.id.slider);
        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Toast.makeText(MainActivity.this, "New Slider value:" + slider.getValue() , Toast.LENGTH_SHORT).show();
            }
        });
        //Range Slider
        RangeSlider rangeSlider = findViewById(R.id.range_slider);
        rangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                Toast.makeText(MainActivity.this, "New Slider value:" + slider.getValues() , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static void showSnackbar(View view, String messageRes) {
        Snackbar.make(view, messageRes, Snackbar.LENGTH_SHORT).show();
    }
    */

}
