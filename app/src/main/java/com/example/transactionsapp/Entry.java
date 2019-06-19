package com.example.transactionsapp;

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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Entry extends Fragment {

    private EditText amountEditText, person, description;
    private Button received, paid;

    private String amount, personName, mode, desc;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users");

    String user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.entry_fragment, container, false);
        getActivity().setTitle("Transactions App - by Akrypt");

        amountEditText = rootView.findViewById(R.id.amount);
        person = rootView.findViewById(R.id.person);
        description = rootView.findViewById(R.id.description);
        received = rootView.findViewById(R.id.received);
        paid = rootView.findViewById(R.id.paid);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getUid();
        dbRef = dbRef.child(user);


        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (amountEditText.getText().length() > 0 && person.getText().length() > 0){
                    amount = amountEditText.getText().toString();
                    personName = person.getText().toString().trim();
                    mode = received.getText() + " from:";
                    desc = description.getText().toString().trim();

                    //Pushing to database
                    SingleTransaction singleTransaction = new SingleTransaction(amount, mode, personName, desc);
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

                    //Pushing to database
                    SingleTransaction singleTransaction = new SingleTransaction(amount, mode, personName, desc);
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
}
