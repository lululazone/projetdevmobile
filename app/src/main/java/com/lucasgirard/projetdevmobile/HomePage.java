package com.lucasgirard.projetdevmobile;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucasgirard.projetdevmobile.databinding.ActivityMainBinding;

public class HomePage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    public float x1,x2,y1,y2;
    public MainActivity ma = new MainActivity();

    public String UserId;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.todo);
        mAuth = FirebaseAuth.getInstance();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            UserId = extras.getString("userId");
            //The key argument here must match that used in the other activity
        }
        else{
            UserId = SingletonFirebase.getInstance().getUserId();
        }
    }

    @Override
    public void onBackPressed() {
        // Créer une boîte de dialogue de confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Êtes-vous sûr de vouloir quitter ?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Si l'utilisateur appuie sur "Oui", on ferme l'application
                        finishAffinity();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Si l'utilisateur appuie sur "Non", on annule la boîte de dialogue
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }






    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                if (x1 < x2) {
                    if(bottomNavigationView.getSelectedItemId() == R.id.todo){
                        bottomNavigationView.setSelectedItemId(R.id.setting);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingFragment()).commit();
                    }
                    if(bottomNavigationView.getSelectedItemId() == R.id.credits){
                        bottomNavigationView.setSelectedItemId(R.id.todo);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToDoFragment()).commit();
                    }
                    if(bottomNavigationView.getSelectedItemId() == R.id.addTodo){
                        bottomNavigationView.setSelectedItemId(R.id.credits);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreditsFragment()).commit();
                    }
                }
                if(x2<x1){
                    if(bottomNavigationView.getSelectedItemId() == R.id.setting){
                        bottomNavigationView.setSelectedItemId(R.id.todo);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToDoFragment()).commit();
                    }
                    if(bottomNavigationView.getSelectedItemId() == R.id.todo){
                        bottomNavigationView.setSelectedItemId(R.id.credits);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreditsFragment()).commit();
                    }
                    if(bottomNavigationView.getSelectedItemId() == R.id.credits){
                        bottomNavigationView.setSelectedItemId(R.id.addTodo);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddTodoFragment()).commit();
                    }
                }
                break;
        }
        return false;
    }


    public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.todo:
                ToDoFragment toDoFragment = new ToDoFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, toDoFragment).commit();
                Bundle args = new Bundle();
                args.putString("userId", UserId);
                toDoFragment.setArguments(args);
                return true;
            case R.id.credits:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreditsFragment()).commit();
                return true;
            case R.id.setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingFragment()).commit();
                return true;

            case R.id.addTodo:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddTodoFragment()).commit();
                return true;


        }
        return false;
    }
}

