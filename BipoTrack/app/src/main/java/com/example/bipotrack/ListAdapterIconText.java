package com.example.bipotrack;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ListAdapterIconText extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] items;
    private final Integer[] icons;
    private int selectedItem;

    private boolean withImages;

    public ListAdapterIconText(Activity context, String[] items, Integer[] icons, boolean withImages) {
        super(context, R.layout.list_item_icon_text, items);
        this.context = context;
        this.items = items;
        this.icons = icons;
        this.selectedItem = -1;
        this.withImages = withImages;
    }

    public int getSelectedItem(){
        return this.selectedItem;
    }

    @Override
    public int getCount() {
        return items.length; //returns total of items in the list
    }

    @Override
    public String getItem(int position) {
        return items[position]; //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item_icon_text, null, true);
        TextView txtTitle = rowView.findViewById(R.id.txt);
        ImageView imageView = rowView.findViewById(R.id.img);


        imageView.setImageResource(icons[position]);
        if(withImages) {
            imageView.getLayoutParams().height = 100;
            imageView.getLayoutParams().width = 100;
        }
        txtTitle.setText(items[position]);

        return rowView;
    }


}
