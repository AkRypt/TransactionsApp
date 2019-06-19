package com.example.transactionsapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TransactionsAdapter extends ArrayAdapter<SingleTransaction> {

    TextView mode, personName, amount, date, time, desc;

    public TransactionsAdapter(@NonNull Context context, int resource, @NonNull List<SingleTransaction> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        SingleTransaction currentTrans = getItem(position);

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.transactions_list, parent, false);
        }

        mode =listItemView.findViewById(R.id.mode);
        mode.setText(currentTrans.getMode());

        personName = listItemView.findViewById(R.id.personName);
        personName.setText(currentTrans.getPersonName());

        amount = listItemView.findViewById(R.id.transactionAmount);
        String amountCo = currentTrans.getAmount();
        if (Integer.parseInt(amountCo) > 0) {
            amount.setTextColor(Color.parseColor("#0097e2"));
        } else {
            amount.setTextColor(Color.parseColor("#FF4444"));
        }
        amount.setText(amountCo);

        desc = listItemView.findViewById(R.id.transactionDesc);
        desc.setText(currentTrans.getDesc());

        date = listItemView.findViewById(R.id.date);
        date.setText(currentTrans.getDate());

        time = listItemView.findViewById(R.id.time);
        time.setText(currentTrans.getTime());

        return listItemView;
    }
}
