package com.example.bipotrack;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bipotrack.database.CRUD.SelectedMoodTable;
import com.example.bipotrack.database.entites.Mood;
import com.example.bipotrack.database.entites.Note;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Objects;
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
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

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


        String passwordtxt = Objects.requireNonNull(password.getText()).toString();
        String  startDateTxt = Objects.requireNonNull(startDate.getText()).toString();
        String  endDateTxt  = Objects.requireNonNull(endDate.getText()).toString();


        if(passwordtxt != null  && startDateTxt  != null && endDateTxt  != null &&
                passwordtxt.length() > 0  &&  startDateTxt.length() > 0 && endDateTxt.length() > 0 )
        {
            Vector<Note>  moods = SelectedMoodTable.getInstance().getMoodsIntervalDate(startDateTxt,endDateTxt);
            String strPdf  = "";
            for(Note  note: moods){
                //String str = "Mood selected on " + note.date +  " " +  note.time + ":" + note.mood + "\n";
                String str = note.date +  " , " +  note.time + " , " + note.mood + "\n";
                if(note.note != null && note.note.length()> 0) {
                    str += "\n Note:  "+ note.note + "\n"
                            +  "\n Note mood predicted:  ";
                    for(Mood mood  : note.moodsOfNote){
                        str += mood.mood + ":" +  mood.confidence + "\n";
                    }
                }
                strPdf += str + "\n\n";
            }
            // >>>>>> use moods to generate the report between startDateTxt and startDateTxt and use passwordtxt to encryp the pdf
            System.out.println(strPdf);
            try {
                createPdf(strPdf , passwordtxt);
            } catch (GeneralSecurityException | IOException | DocumentException e) {
                e.printStackTrace();
            }
        }
        else {
            Snackbar sb = Snackbar.make(getView(), "Please enter your password, start and end date to generate the report", Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundResource(R.color.danger);
            sb.show();
        }
    }

    private void createPdf(String data, String password) throws GeneralSecurityException, IOException, DocumentException {
        String file_new = "test";
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/bipotrack/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        int x = 10, y=25;
        canvas.drawText("Mood Log\t - ", x, y, paint);
        y=50;
        int page_num=2;
        for (String line:data.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y+=paint.descent()-paint.ascent();
            if(y > 600){
                document.finishPage(page);
                pageInfo = new PdfDocument.PageInfo.Builder(300, 600, page_num).create();
                page = document.startPage(pageInfo);
                canvas = page.getCanvas();
                y=25;
                page_num += 1;
            }
        }
        //canvas.drawText("Comment\t - " + sometext, 8, 75, paint);
        // finish the page
        document.finishPage(page);

        // write the document content
        String targetPdf = directory_path + file_new + ".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getContext(), "Created", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(getContext(), "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();

        // Creating password protected file
        PdfReader reader = new PdfReader(directory_path + file_new + ".pdf");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(directory_path + file_new + "-enc.pdf"));
        stamper.setEncryption(password.getBytes(),
                "group4".getBytes(),
                PdfWriter.ALLOW_COPY,
                PdfWriter.ENCRYPTION_AES_256 );
        stamper.close();
        reader.close();
        System.out.println("Successfully Done");

        File file_d = new File(targetPdf);
        file_d.delete();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(requireContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                new File(Environment.getExternalStorageDirectory().getPath() +
                        "/bipotrack/" + file_new + "-enc.pdf"));
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("*/*");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share Via");
        startActivity(shareIntent);

    }
}
