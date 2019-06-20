package com.example.transactionsapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Entry extends Fragment {

    private EditText amountEditText, person, description;
    private Button received, paid, dateView, timeView;



    private String amount, personName, mode, desc, format, date, time;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users");

    Calendar calendar = Calendar.getInstance();
    private int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private int minute = calendar.get(Calendar.MINUTE);

    String user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.entry_fragment, container, false);
        getActivity().setTitle("Transactions App - by Akrypt");

        amountEditText = rootView.findViewById(R.id.amount);
        person = rootView.findViewById(R.id.person);
        description = rootView.findViewById(R.id.description);
        dateView = rootView.findViewById(R.id.date);
        timeView = rootView.findViewById(R.id.time);
        received = rootView.findViewById(R.id.received);
        paid = rootView.findViewById(R.id.paid);

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getUid();
        dbRef = dbRef.child(user);

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
                    amount = amountEditText.getText().toString();
                    personName = person.getText().toString().trim();
                    mode = received.getText() + " from:";
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
                    amount = amountEditText.getText().toString();
                    personName = person.getText().toString().trim();
                    mode = paid.getText()+" to:";
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
}