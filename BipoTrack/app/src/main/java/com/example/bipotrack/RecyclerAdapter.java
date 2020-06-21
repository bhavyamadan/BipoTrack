package com.example.bipotrack;

import android.text.Layout;
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

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";
    List<String> moviesList;

    public RecyclerAdapter(List<String> moviesList) {
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.note.setText(String.valueOf(position));
        holder.textView.setText(moviesList.get(position));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckBox checkBox;
        TextView textView, note;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.check_box);
            textView = itemView.findViewById(R.id.textView);
            note = itemView.findViewById(R.id.note);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), moviesList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }
    }
}
