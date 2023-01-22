package com.lucasgirard.projetdevmobile;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTodoFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String fontName;
    private Typeface newTypeface2;

    public AddTodoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTodoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTodoFragment newInstance(String param1, String param2) {
        AddTodoFragment fragment = new AddTodoFragment();
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("font");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    fontName = dataSnapshot.getValue(String.class);
                    if (fontName.equals("erica_one")) {
                        newTypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/erica_one.ttf");
                        setTypeface((ViewGroup) getView(), newTypeface2);
                    } else if (fontName.equals("neon")) {
                        newTypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/neon.ttf");
                        setTypeface((ViewGroup) getView(), newTypeface2);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        int fontStyle = Typeface.NORMAL;
        if(newTypeface2!=null){
            if (getView() != null) {
                setTypeface((ViewGroup) getView(), newTypeface2);
            }}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_todo, container, false);
        Button button = view.findViewById(R.id.addList);
        TextView textView = view.findViewById(R.id.tastInput);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = textView.getText().toString();
                if (task.isEmpty()) {
                    textView.setError("Please enter a task");
                } else {
                    textView.setText("");
                    writeNewTask(task);
                }
            }
        });
        return view;
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


    public void writeNewTask(String value){
        String UserId = SingletonFirebase.getInstance().getUserId();
        if(UserId != null){
            DatabaseReference myRef = database.getReference("users").child(UserId).child("todo").child(value);
            myRef.setValue(value);
        }
    }
}