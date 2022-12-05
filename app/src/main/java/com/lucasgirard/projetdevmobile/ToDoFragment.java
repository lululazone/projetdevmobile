package com.lucasgirard.projetdevmobile;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
public class ToDoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String UserId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ListView listView;
    List list = new ArrayList();
    ArrayAdapter arrayAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        Bundle args = getArguments();
        UserId = args.getString("userId");
        if(UserId == null || UserId.isEmpty()){
            UserId = SingletonFirebase.getInstance().getUserId();
        }



        readValue();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listView.isItemChecked(position)){

                    //DatabaseReference myRef = database.getReference("users").child(UserId).child("todo").child(String.valueOf(position));
                    //myRef.removeValue();
                }
            }
        });

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);
        // Inflate the layout for this fragment
        return view;
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