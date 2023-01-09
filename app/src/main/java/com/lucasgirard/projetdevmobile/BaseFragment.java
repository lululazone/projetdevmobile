package com.lucasgirard.projetdevmobile;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

public abstract class BaseFragment extends Fragment {

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("font_choice")) {
                    String fontName = sharedPreferences.getString(key, "erica_one");

                    int fontStyle = Typeface.NORMAL;
                    Typeface newTypeface = Typeface.create(fontName, fontStyle);
                    if (fontName.equals("erica_one")) {
                        newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "font/erica_one.ttf");
                    } else if (fontName.equals("neon")) {
                        newTypeface = Typeface.createFromAsset(getActivity().getAssets(), "font/neon.ttf");
                    }


                    setTypeface((ViewGroup) getView(), newTypeface);
                } else if (key.equals("background_choice")) {
                    String backgroundName = sharedPreferences.getString(key, "default_background");
                    int backgroundId = getResources().getIdentifier(backgroundName, "drawable", getActivity().getPackageName());
                    getView().setBackgroundResource(backgroundId);
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
