package com.lucasgirard.projetdevmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingFragment extends PreferenceFragmentCompat {

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences,rootKey);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String fontName1 = sharedPref.getString("font_choice", "erica_one");
        int fontStyle1 = Typeface.NORMAL;
        Typeface newTypeface = Typeface.create(fontName1, fontStyle1);
        if (fontName1.equals("erica_one")) {
            newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "font/erica_one.ttf");
        } else if (fontName1.equals("neon")) {
            newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "font/neon.ttf");
        }

        //avoid null pointer exception

        if(getView() != null) {
            setTypeface((ViewGroup) getView(), newTypeface);
        }








        preferenceChangeListener = (sharedPreferences, key) -> {


            if (key.equals("font_choice")) {
                String fontName = sharedPreferences.getString(key, "erica_one");
                int fontStyle = Typeface.NORMAL;
                Typeface newTypeface2 = Typeface.create(fontName, fontStyle);
                if (fontName.equals("erica_one")) {
                    newTypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "font/erica_one.ttf");
                } else if (fontName.equals("neon")) {
                    newTypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "font/neon.ttf");
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
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String fontName = sharedPref.getString("font_choice", "erica_one");
        int fontStyle = Typeface.NORMAL;
        Typeface newTypeface = Typeface.create(fontName, fontStyle);
        if (fontName.equals("erica_one")) {
            newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "font/erica_one.ttf");
        } else if (fontName.equals("neon")) {
            newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "font/neon.ttf");
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