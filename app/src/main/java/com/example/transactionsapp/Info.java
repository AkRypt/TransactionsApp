package com.example.transactionsapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Info extends Fragment {

    Button signout, clearAll;

    FirebaseAuth mAuth;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().getRoot().child("users");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.info_fragment, container, false);
        getActivity().setTitle("Info");

        signout = rootView.findViewById(R.id.signout);
        clearAll = rootView.findViewById(R.id.clearAll);

        mAuth = FirebaseAuth.getInstance();
        final String user = mAuth.getUid().toString();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance().signOut(getContext());
            }
        });

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef.child(user).removeValue();
                Toast.makeText(getContext(), "Successfuly deleted all transactions!", Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }
}
