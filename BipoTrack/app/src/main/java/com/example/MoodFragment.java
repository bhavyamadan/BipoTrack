package com.example.bipotrack;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import java.util.*;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoodFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView depressed_img;
    ImageView angry_img;
    ImageView meh_img;
    ImageView good_img;
    ImageView happy_img;
    ImageView excited_img;


    public MoodFragment() {
        // Required empty public constructor
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
        initMoodsImageViews();
        final List<ImageView> moods = getMoods();
        for (ImageView m : moods){
            addOnClickActionListener(m);
        }
        angry_img.performClick();
    }

    private void initMoodsImageViews() {
     depressed_img = getView().findViewById(R.id.depressed);
     angry_img = getView().findViewById(R.id.angry);
     meh_img = getView().findViewById(R.id.meh);
     good_img = getView().findViewById(R.id.good);
     happy_img = getView().findViewById(R.id.happy);
     excited_img = getView().findViewById(R.id.excited);
    }

    private List<ImageView> getMoods(){
        List<ImageView> moods = new ArrayList<ImageView>();
        moods.add(depressed_img);
        moods.add(angry_img);
        moods.add(meh_img);
        moods.add(good_img);
        moods.add(happy_img);
        moods.add(excited_img);
        return moods;
    }

    private Snackbar snackbar ;
    private void addOnClickActionListener(final ImageView mood) {

        final List<ImageView> moods = getMoods();
        mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_mood = getResources().getResourceEntryName(mood.getId()).toUpperCase();
                String html_str = "I'm feeling  <b><font color='" + "#FF5252" + "'>" + str_mood + " </font></b> ";
                snackbar  = Snackbar.make(getView(),  Html.fromHtml(html_str), Snackbar.LENGTH_INDEFINITE)
                        .setAction("SAVE", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Snackbar succ = Snackbar.make(getView(), "Your mood has been successfully added !",Snackbar.LENGTH_LONG);
                                succ.setTextColor(Color.parseColor("#388E3C"));
                                succ.getView().setBackgroundColor(Color.parseColor("#C8E6C9"));
                                succ.setAnchorView(getActivity().findViewById(R.id.place));
                                succ.show();
                            }})
                        .setAnchorView(getActivity().findViewById(R.id.place));
                snackbar.setTextColor(getResources().getColor(R.color.colorOnSecondary));
                snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorOnPrimary));
                snackbar.show();
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

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (!visible && snackbar != null ){
            snackbar.dismiss();
        }
    }
}
