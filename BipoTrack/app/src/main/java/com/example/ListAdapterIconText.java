package com.example.bipotrack;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapterIconText extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] items;
    private final Integer[] icons;
    private int selectedItem;

    public ListAdapterIconText(Activity context, String[] items, Integer[] icons) {
        super(context, R.layout.list_item_icon_text, items);
        this.context = context;
        this.items = items;
        this.icons = icons;
        this.selectedItem = -1;
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
        txtTitle.setText(items[position]);
        imageView.setImageResource(icons[position]);
        return rowView;
    }
}
