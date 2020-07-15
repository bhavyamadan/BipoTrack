package com.example.bipotrack;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.bipotrack.database.CRUD.NoteMoodsTable;
import com.example.bipotrack.database.CRUD.SelectedMoodTable;
import com.example.bipotrack.database.entites.Mood;
import com.example.bipotrack.database.entites.Moods;
import com.example.bipotrack.database.entites.Note;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

public class AddMoodNotesDialog  extends DialogFragment {

    private Context context ;

    private static String my_notes;
    public static final String TAG = "example_dialog";
    private MoodTextClassificationClient client;
    private Handler handler;
    private static View view;
    private Toolbar toolbar;
    private static String selectedMood;

    private static String title;
    private  TextInputEditText textViewNotes;
    private ImageView iconMoodSelected;
    private TextView textFieldSelectedMood;
    private LinearLayout layoutPredictedTextMoods;

    public static AddMoodNotesDialog display(FragmentManager fragmentManager, String selectedMood,View view, String title) {
        AddMoodNotesDialog exampleDialog = new AddMoodNotesDialog();
        exampleDialog.show(fragmentManager, TAG);
        AddMoodNotesDialog.selectedMood = selectedMood;
        AddMoodNotesDialog.view = view;
        AddMoodNotesDialog.title = title;
        return exampleDialog;
    }

    private static Note note ;
    public static AddMoodNotesDialog display(FragmentManager fragmentManager, Note note, View view, String title) {
        AddMoodNotesDialog exampleDialog = new AddMoodNotesDialog();
        exampleDialog.show(fragmentManager, TAG);
        AddMoodNotesDialog.note = note;
        AddMoodNotesDialog.selectedMood = note.mood;
        AddMoodNotesDialog.view = view;
        AddMoodNotesDialog.title = title;
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
        final View view = inflater.inflate(R.layout.add_mood_notes_dialog, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setTitle(title);
        toolbar.inflateMenu(R.menu.menu_dialog);

        textViewNotes = view.findViewById(R.id.notes_text);
        iconMoodSelected = view.findViewById(R.id.iconSelectedMood);
        textFieldSelectedMood =  view.findViewById(R.id.textFieldSelectedMood);
        layoutPredictedTextMoods =  view.findViewById(R.id.layout_text_predicted_moods);

        if(note != null)
            selectedMood = note.mood;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textFieldSelectedMood.setText( " " +Html.fromHtml(" Your selected mood is <b>" + selectedMood +"</b>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            textFieldSelectedMood.setText( " " +Html.fromHtml(" Your selected mood is <b>"+ selectedMood +"</b>"));
        }

        Utils.setImageMoodIcon(iconMoodSelected,selectedMood);

        if(note != null){
            textViewNotes.setText(note.note);
            Vector<Mood> moods = note.moodsOfNote;

            TextView textV = new TextView(this.getContext());
            textV.setText("Your previous notes contains those feelings");

            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params3.setMargins(0,0,0,20);
            textV.setLayoutParams(params3);
            layoutPredictedTextMoods.addView(textV);

            for (Mood mood: moods){
                LinearLayout linearLayout = new LinearLayout(this.getContext());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                ImageView imageView = new ImageView(this.getContext());
                Utils.setImageMoodIcon(imageView, mood.mood);
                LinearLayout.LayoutParams params =  new LinearLayout
                        .LayoutParams(50, 50);
                imageView.setLayoutParams(params);
                linearLayout.addView(imageView);

                TextView textView = new TextView(this.getContext());
                float confidence = Float.parseFloat(mood.confidence)*100;
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textView.setText(" "+ Html.fromHtml("<b>" +mood.mood +"</b> : "  + df.format(confidence) +"%" , Html.FROM_HTML_MODE_COMPACT));
                } else {
                    textView.setText(" "+ Html.fromHtml("<b>" +mood.mood +"</b> : "  + df.format(confidence) +"%"));
                }

                LinearLayout.LayoutParams paramsText =  new LinearLayout
                        .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(paramsText);
                linearLayout.addView(textView);

                layoutPredictedTextMoods.addView(linearLayout);
            }
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.e("Action", item.getTitle()+"");
                if(item.getTitle().equals("save")) {
                    my_notes = textViewNotes.getText().toString();
                    if(note != null) {
                        classify(my_notes , false);
                    }
                    else{
                        //add
                        classify(my_notes , true);
                    }

                }
                dismiss();
                return false;
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler = new Handler();
        context = view.getContext();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        handler.post(new Runnable() {
            @Override
            public void run() {
                client.unload();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        final Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);

            Log.v(TAG, "onStart");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    client = new MoodTextClassificationClient(context);
                    client.load();
                    Log.e("ddd", "Model loaded");
                }
            });
        }

    }

    /** Send input text to TextClassificationClient and get the classify messages. */
    private void classify(final String text, final boolean isNew) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Run text classification with TF Lite.
                List<MoodTextClassificationClient.Result> results = client.classify(text);

                // Show classification result on screen
                showResult(text, results, isNew);
            }
        });
    }

    /** Show classification result on the screen. */
    private void showResult(final String inputText, final List<MoodTextClassificationClient.Result> results, boolean isNew) {
        // Run on UI thread as we'll updating our app UI

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String textToShow = "Your selected mood: " + selectedMood + "\n\n\n";

                textToShow += "Your different text mood percentage\n\n";
                for (int i = 0; i < results.size(); i++) {
                    MoodTextClassificationClient.Result result = results.get(i);
                    textToShow += result.getTitle().toUpperCase() + ": " + result.getConfidence() + "\n\n";
                }

                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context)
                                .setTitle("YOUR MOOD REPORT")
                                .setMessage(textToShow);
                if(note == null) {
                    dialog.setNeutralButton("IGNORE TEXT MOOD AND SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SelectedMoodTable.getInstance().insertData(selectedMood,view);
                            dialog.dismiss();
                            dismiss();
                            Log.e("too", Utils.getCurrentTimeAndDate().getTime());
                        }
                    });
                }


                String positiveButtonName = "SAVE";
                if(note != null) {
                    positiveButtonName = "UPDATE";
                }
                dialog.setPositiveButton(positiveButtonName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /**Add selectedMood with text*/
                        long selectedMoodAndTextID ;
                        if(note == null)
                             selectedMoodAndTextID = SelectedMoodTable.getInstance().insertData(selectedMood,my_notes, AddMoodNotesDialog.view);
                        else {
                            selectedMoodAndTextID = SelectedMoodTable.getInstance().updateNoteById(note,my_notes, AddMoodNotesDialog.view);
                            NoteMoodsTable.getInstance().deleteDataByNote(note.id); //delete all related moods
                        }

                        /**Add predicted text moods*/
                        boolean isInserted = false;
                        for (int i = 0; i < results.size(); i++) {
                            MoodTextClassificationClient.Result result = results.get(i);
                            String moodDetected = result.getTitle().toUpperCase() ;
                            Float confidence = result.getConfidence() ;
                            NoteMoodsTable noteMoodsTable = NoteMoodsTable.getInstance();
                            isInserted = noteMoodsTable.insertData(moodDetected,confidence,selectedMoodAndTextID);
                        }

                        dialog.dismiss();
                        dismiss();
                        if(isInserted == true) {
                            Snackbar sb = Snackbar.make(view, "Your mood and notes has been inserted successfully", Snackbar.LENGTH_SHORT);
                            sb.getView().setBackgroundResource(R.color.success);
                            sb.show();
                            MoodFragment.getInstance().refreshNotesList(Utils.getCurrentTimeAndDate().getDate());
                            CalendarFragment.getInstance().refreshNotesList(Utils.getCurrentTimeAndDate().getDate(),CalendarFragment.myview);
                        }
                        else {
                            Snackbar sb = Snackbar.make(view, "Your mood and notes has not been inserted", Snackbar.LENGTH_SHORT);
                            sb.getView().setBackgroundResource(R.color.danger);
                            sb.show();
                        }

                    }
                });
                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

}
