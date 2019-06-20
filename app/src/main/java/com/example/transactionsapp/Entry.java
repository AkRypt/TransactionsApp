package com.example.transactionsapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;

public class Entry extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private EditText amountEditText, person, description;
    private Button received, paid, dateView, timeView;
    private String amount, personName, mode, desc, format, date, time;
    String user;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users");

    // For Date and Time Picker
    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private int minute = calendar.get(Calendar.MINUTE);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Because this is a fragment
        View rootView =  inflater.inflate(R.layout.entry_fragment, container, false);
        getActivity().setTitle("Transactions App - by Akrypt");

        // Getting user uid to refer to particular user in database
        SharedPreferences prefs = this.getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        user = prefs.getString("user", null);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser userx = firebaseAuth.getCurrentUser();
                if (userx != null) {
                    dbRef = dbRef.child(user);
                } else {

                }
            }
        };

        amountEditText = rootView.findViewById(R.id.amount);
        person = rootView.findViewById(R.id.person);
        description = rootView.findViewById(R.id.description);
        received = rootView.findViewById(R.id.received);
        paid = rootView.findViewById(R.id.paid);
        dateView = rootView.findViewById(R.id.date);
        timeView = rootView.findViewById(R.id.time);


        // For choosing Date
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String datex = day+"/"+month+"/"+year;
                        dateView.setText(datex);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        // For choosing Time
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour_i, int minute_i) {
                        selectedTimeFormat(hour_i);
                        String timex = hour+":"+minute_i+" "+format;
                        timeView.setText(timex);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });


        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amountEditText.getText().length() > 0 && person.getText().length() > 0){
                    mode = received.getText() + " from:";
                    amount = amountEditText.getText().toString();
                    personName = person.getText().toString().trim();
                    desc = description.getText().toString().trim();
                    date = dateView.getText().toString();
                    time = timeView.getText().toString();

                    //Pushing to database
                    SingleTransaction singleTransaction = new SingleTransaction(amount, mode, personName, desc, date, time);
                    dbRef.push().setValue(singleTransaction);

                    amountEditText.setText("");
                    person.setText("");
                    description.setText("");
                    Toast.makeText(getContext(), "Your transaction has been recorded!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Please fill the required fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amountEditText.getText().length() > 0 && person.getText().length() > 0){
                    mode = paid.getText()+" to:";
                    amount = amountEditText.getText().toString();
                    personName = person.getText().toString().trim();
                    desc = description.getText().toString().trim();
                    date = dateView.getText().toString();
                    time = timeView.getText().toString();

                    //Pushing to database
                    SingleTransaction singleTransaction = new SingleTransaction(amount, mode, personName, desc, date, time);
                    dbRef.push().setValue(singleTransaction);

                    amountEditText.setText("");
                    person.setText("");
                    description.setText("");
                    Toast.makeText(getContext(), "Your transaction has been recorded!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Please fill the required fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    // For Time Format
    public void selectedTimeFormat(int hourx) {
        hour = hourx;
        if (hourx == 0) {
            hour = hour + 12;
            format = "AM";
        } else if (hourx == 12) {
            format = "PM";
        } else if (hourx > 12) {
            hour = hour - 12;
            format = "PM";
        } else {
            format = "AM";
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}