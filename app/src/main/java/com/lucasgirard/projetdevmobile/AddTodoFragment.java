package com.lucasgirard.projetdevmobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


    public void writeNewTask(String value){
        String UserId = SingletonFirebase.getInstance().getUserId();
        if(UserId != null){
            DatabaseReference myRef = database.getReference("users").child(UserId).child("todo").child(value);
            myRef.setValue(value);
        }
    }
}