package com.example.bipotrack;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bipotrack.database.CRUD.NoteMoodsTable;
import com.example.bipotrack.database.CRUD.SelectedMoodTable;
import com.example.bipotrack.database.entites.Mood;
import com.example.bipotrack.database.entites.Note;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DecimalFormat;
import java.util.Vector;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";
    int layout ;
    Vector<Note> notes;


    public RecyclerAdapter(Vector<Note> notes, int layout) {
        this.notes = notes;
        this.layout = layout; //R.layout.row_item
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(this.layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.note.setText(String.valueOf(position));
        Note note = notes.get(position);


        holder.noteObject = note;
        holder.selectedmoodtext.setText("  SELECTED MOOD: " + note.mood);
        Utils.setImageMoodIcon(holder.selectdmoodicon,note.mood);

        if(note != null && note.note != null  && note.note.length() > 0) {
            holder.noteObject = note;

            int max = 30;
            int min = Math.min(note.note.length(), max);

            String notesToDisplay = "";
            if(min == max)
                notesToDisplay = note.note.substring(0,min) + " ...";
            else
                notesToDisplay = note.note;
            holder.textView.setText(notesToDisplay);

            if(note.moodsOfNote != null && note.moodsOfNote.size() != 0) {
                Mood mainMood = note.moodsOfNote.firstElement();
                float confidence = Float.parseFloat(mainMood.confidence)*100;
                holder.note.setText("  MAIN MOOD: " + mainMood.mood + ": " + (int) confidence + "%");
                Utils.setImageMoodIcon(holder.theMoodIcon,mainMood.mood);
            }
        } else{
            holder.textView.setVisibility(View.GONE);
            holder.note.setVisibility(View.GONE);
            holder.theMoodIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.notes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

       //CheckBox checkBox;
        Note noteObject;
        TextView textView, note,selectedmoodtext; //textView = text //note = mood
        ImageView theMoodIcon,selectdmoodicon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //checkBox = itemView.findViewById(R.id.check_box);
            textView = itemView.findViewById(R.id.textView);
            note = itemView.findViewById(R.id.text_note);
            theMoodIcon = itemView.findViewById(R.id.the_mood);

            selectedmoodtext = itemView.findViewById(R.id.selectedMoodtext);
            selectdmoodicon = itemView.findViewById(R.id.selectedMoodicon);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    //Toast.makeText(v.getContext(), moviesList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    int itemSelected = 1;
                    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(v.getContext());
                    dialog.setTitle("Options");

                    if(noteObject.note==null || noteObject.note.length() ==  0  ) {
                        final String[] items = { "Remove"} ;
                        Integer[] imagesId = {R.drawable.ic_baseline_delete_24};
                        final ListAdapterIconText listAdapter = new ListAdapterIconText((Activity) v.getContext(), items, imagesId,false);
                        dialog.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //which == 0 (Remove)
                                if(which == 0){
                                    Log.e("ttt", "====> Note" + noteObject.id);
                                    int moodDeleted = SelectedMoodTable.getInstance().deleteData(noteObject.id);
                                    Log.e("ttt", "====>" + moodDeleted);
                                    MoodFragment.getInstance().refreshNotesList(Utils.getCurrentTimeAndDate().getDate());
                                    CalendarFragment.getInstance().refreshNotesList(Utils.getCurrentTimeAndDate().getDate(),CalendarFragment.myview);
                                }
                                Toast.makeText(v.getContext(), "Selected item:" + items[which], Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.show();
                    }else {
                        final String[] items = { "Remove", "Edit","Moods"} ;
                        Integer[] imagesId = {R.drawable.ic_baseline_delete_24,R.drawable.ic_baseline_edit_24,R.drawable.ic_baseline_list_alt_24};
                        final ListAdapterIconText listAdapter = new ListAdapterIconText((Activity) v.getContext(), items, imagesId,false);
                        dialog.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //which == 0 (Remove)
                                if(which == 0){
                                    Log.e("ttt", "====> Note" + noteObject.id);
                                    int moodDeleted = SelectedMoodTable.getInstance().deleteData(noteObject.id);
                                    Log.e("ttt", "====>" + moodDeleted);
                                    MoodFragment.getInstance().refreshNotesList(Utils.getCurrentTimeAndDate().getDate());
                                    CalendarFragment.getInstance().refreshNotesList(Utils.getCurrentTimeAndDate().getDate(),CalendarFragment.myview);
                                }
                                //which == 1 (Edit)
                                if(which == 1){
                                    openAddMoodNotesDialog(noteObject,"Edit Notes");
                                }
                                //which == 2 (Moods)
                                if(which == 2){
                                    openListMoodDialog(noteObject);
                                }
                                Toast.makeText(v.getContext(), "Selected item:" + items[which], Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.show();
                    }




                    return false;
                }
            });

        }

    }



    private void openAddMoodNotesDialog(Note note, String title){
        AddMoodNotesDialog.display(MoodFragment.getInstance().getActivity().getSupportFragmentManager(), note, MoodFragment.getInstance().getView(),title);
    }

    private void openListMoodDialog(Note note){
        int itemSelected = 1;
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(MoodFragment.getInstance().getContext());
        dialog.setTitle("Moods of your Notes");
        Vector<String> items = new Vector<>();
        Vector<Integer> imagesId = new Vector<>();
        for(Mood mood: note.moodsOfNote){
            float confidence = Float.parseFloat(mood.confidence)*100;
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            items.add(" " + mood.mood + ": " +  df.format(confidence) + "%");
            imagesId.add(Utils.getImageByMood(mood.mood));
        }

        final ListAdapterIconText listAdapter = new ListAdapterIconText((Activity) MoodFragment.getInstance().getContext(), items.toArray(new String[items.size()]),imagesId.toArray(new Integer[imagesId.size()]), true);
        dialog.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
