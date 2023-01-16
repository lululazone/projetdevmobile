package com.lucasgirard.projetdevmobile;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToDoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToDoFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;
    public String UserId;
    public SharedPreferences preferences;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ListView listView;
    List list = new ArrayList();
    ArrayAdapter arrayAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String fontName;

    public ToDoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToDoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToDoFragment newInstance(String param1, String param2) {
        ToDoFragment fragment = new ToDoFragment();
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
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        fontName = preferences.getString("font_choice", "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("font");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fontName = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        int fontStyle = Typeface.NORMAL;
        Typeface newTypeface2 = Typeface.create(fontName, fontStyle);
        if (fontName.equals("erica_one")) {
            newTypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/erica_one.ttf");
        } else if (fontName.equals("neon")) {
            newTypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/neon.ttf");
        }
        if (getView() != null) {
            setTypeface((ViewGroup) getView(), newTypeface2);
        }






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        rotateOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.to_bottom_anim);
        FloatingActionButton more_floating_button = view.findViewById(R.id.more_floating_button);
        FloatingActionButton add_floating_button = view.findViewById(R.id.add_floating_button);
        FloatingActionButton delete_floating_button = view.findViewById(R.id.remove_floating_button);
        more_floating_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickMore();

            }


        });
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        Bundle args = getArguments();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();
        if(UserId == null || UserId.isEmpty()){
            UserId = SingletonFirebase.getInstance().getUserId();
        }



        readValue();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //check if one or more items are checked
                int count = listView.getCheckedItemCount();
                if (count > 0) {
                    //if one or more items are checked, then show the delete button
                    more_floating_button.show();
                } else {
                    //if no items are checked, then hide the delete button
                    more_floating_button.hide();

                }


                //get checked items
            }


        });

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_multiple_choice, list);
        listView.setAdapter(arrayAdapter);


        delete_floating_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    OnClickDelete();
            }
        });

        add_floating_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickAdd();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void OnClickMore(){
        FloatingActionButton more_floating_button = getView().findViewById(R.id.more_floating_button);
        FloatingActionButton add_floating_button = getView().findViewById(R.id.add_floating_button);
        FloatingActionButton delete_floating_button = getView().findViewById(R.id.remove_floating_button);
        if(more_floating_button.getRotation() == 0){
            more_floating_button.startAnimation(rotateOpen);
            add_floating_button.startAnimation(fromBottom);
            delete_floating_button.startAnimation(fromBottom);
            add_floating_button.setVisibility(View.VISIBLE);
            delete_floating_button.setVisibility(View.VISIBLE);
            add_floating_button.setClickable(true);
            delete_floating_button.setClickable(true);
            more_floating_button.setRotation(45);
        }else{
            more_floating_button.startAnimation(rotateClose);
            add_floating_button.startAnimation(toBottom);
            delete_floating_button.startAnimation(toBottom);
            add_floating_button.setClickable(false);
            delete_floating_button.setClickable(false);
            add_floating_button.setVisibility(View.INVISIBLE);
            delete_floating_button.setVisibility(View.INVISIBLE);
            more_floating_button.setRotation(0);
        }
    }

    private void setTypeface(ViewGroup root, Typeface typeface) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTypeface(typeface);
            } else if (child instanceof ViewGroup) {
                setTypeface((ViewGroup) child, typeface);
            }
        }
    }


    public void OnClickDelete(){
        //get checked items text
        SparseBooleanArray checked = listView.getCheckedItemPositions();

        for (int i = 0; i < checked.size(); i++) {
            Log.d("checked", checked.toString());
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i))
                //list.remove(position);
                //remove from database
                Log.d("Trying to delete: ",String.valueOf(position));
                DatabaseReference myRef = database.getReference("users").child(UserId).child("todo").child(list.get(position).toString());
                myRef.removeValue();
        }


    }

    public void OnClickAdd(){
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddTodoFragment()).commit();
    }



    public void readValue(){
        if(UserId != null){
            DatabaseReference myRef = database.getReference("users").child(UserId).child("todo");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String value = ds.getValue(String.class);
                        list.add(value);
                        Log.d(TAG, list.toString());
                    }
                    arrayAdapter = new ArrayAdapter(listView.getContext(), android.R.layout.simple_list_item_checked, list);

                    if(list != null && arrayAdapter != null && !list.isEmpty() && !arrayAdapter.isEmpty() && listView != null){
                        listView.setAdapter(arrayAdapter);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }


            });
        }

    }





}