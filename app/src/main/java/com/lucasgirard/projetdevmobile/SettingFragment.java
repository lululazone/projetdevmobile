package com.lucasgirard.projetdevmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingFragment extends PreferenceFragmentCompat {

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    String fontName;
    Typeface newTypeface;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences,rootKey);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();
        DatabaseReference myRef = database.getReference("users").child(uid).child("font");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fontName = dataSnapshot.getValue(String.class);
                int fontStyle = Typeface.NORMAL;
                if(fontName == "erica_one"){
                    newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/erica_one.ttf");
                }
                else if(fontName == "neon"){
                    newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/neon.ttf");
                }
                if(getView() != null) {
                    setTypeface((ViewGroup) getView(), newTypeface);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
        if(fontName==null){
            fontName = sharedPref.getString("font_choice", "erica_one");
            }
        int fontStyle = Typeface.NORMAL;
        newTypeface = Typeface.create(fontName, fontStyle);
        if (fontName.equals("erica_one")) {
            newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/erica_one.ttf");
        } else if (fontName.equals("neon")) {
            newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/neon.ttf");
        }
        int fontStyle1 = Typeface.NORMAL;
        String fontName1 = sharedPref.getString("font_choice", "erica_one");
        newTypeface = Typeface.create(fontName1, fontStyle1);
        if (fontName1.equals("erica_one")) {
            database.getReference("users").child(uid).child("font").setValue("erica_one");
            newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/erica_one.ttf");
        } else if (fontName1.equals("neon")) {
            database.getReference("users").child(uid).child("font").setValue("neon");
            newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/neon.ttf");
        }

        //avoid null pointer exception

        if(getView() != null) {
            setTypeface((ViewGroup) getView(), newTypeface);
        }










        preferenceChangeListener = (sharedPreferences, key) -> {


            if (key.equals("font_choice")) {
                String fontName2 = sharedPreferences.getString(key, "erica_one");
                int fontStyle2 = Typeface.NORMAL;
                Typeface newTypeface2 = Typeface.create(fontName2, fontStyle);
                if (fontName2.equals("erica_one")) {
                    database.getReference("users").child(uid).child("font").setValue("erica_one");
                    newTypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/erica_one.ttf");
                } else if (fontName2.equals("neon")) {
                    database.getReference("users").child(uid).child("font").setValue("neon");
                    newTypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/neon.ttf");
                }



                setTypeface((ViewGroup) getView(), newTypeface2);
            } else if (key.equals("background_choice")) {
                String backgroundName = sharedPreferences.getString(key, "default_background");
                int backgroundId = getResources().getIdentifier(backgroundName, "drawable", getActivity().getPackageName());
                getView().setBackgroundResource(backgroundId);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = super.onCreateView(inflater, container, savedInstanceState);
        return root;
    }



    @Override
    public void onResume() {
        super.onResume();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String fontName = sharedPref.getString("font_choice", "erica_one");
        int fontStyle = Typeface.NORMAL;
        Typeface newTypeface = Typeface.create(fontName, fontStyle);
        if (fontName.equals("erica_one")) {
            database.getReference("users").child(uid).child("font").setValue("erica_one");
            newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/erica_one.ttf");
        } else if (fontName.equals("neon")) {
            database.getReference("users").child(uid).child("font").setValue("neon");
            newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/neon.ttf");
        }

        //avoid null pointer exception
        if (getView() != null) {
            setTypeface((ViewGroup) getView(), newTypeface);
        }
        // Set up a listener whenever a key changes

        sharedPref.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPref.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
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


}