package com.example.transactionsapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Transactions extends Fragment {


    private TransactionsAdapter transAdapter;
    private ListView transactionsList;
    private List<SingleTransaction> singleTransaction = new ArrayList<>();
    private ProgressBar loading;
    private TextView nothing;

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transactions, container, false);
        getActivity().setTitle("Your Transactions");

        SharedPreferences prefs = this.getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        final String user = prefs.getString("user", null);

        loading = rootView.findViewById(R.id.progressBar);
        loading.setVisibility(View.VISIBLE);
        nothing = rootView.findViewById(R.id.nothing);

        transactionsList = rootView.findViewById(R.id.transactionsList);
        transAdapter = new TransactionsAdapter(getContext(), R.layout.transactions_list, singleTransaction);
        transactionsList.setAdapter(transAdapter);


        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot ds : dataSnapshot.child(user).getChildren()) {
                    String mode = ds.child("mode").getValue().toString();
                    String amt = ds.child("amount").getValue().toString();
                    String name = ds.child("personName").getValue().toString();
                    String desc = ds.child("desc").getValue().toString();
                    String date = ds.child("date").getValue().toString();
                    String time = ds.child("time").getValue().toString();
                    if (mode.matches("(.*)Received(.*)")) {
                        amt = "+" + amt;
                    } else {
                        amt = "-" + amt;
                    }
                    singleTransaction.add(0, new SingleTransaction(amt, mode, name, desc, date, time));
                    transAdapter.notifyDataSetChanged();
                }
                loading.setVisibility(View.GONE);
                if (singleTransaction.size() == 0) {
                    nothing.setVisibility(View.VISIBLE);
                } else {
                    nothing.setVisibility(View.GONE);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        return rootView;
    }
}