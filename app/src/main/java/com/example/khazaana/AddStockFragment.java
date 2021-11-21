package com.example.khazaana;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class AddStockFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_stock, container, false);

        String[] stocks = getResources().getStringArray(R.array.stocks);
        AutoCompleteTextView auto = view.findViewById(R.id.autoCompleteTextView);
        auto.setAdapter(new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, stocks));

        return view;
    }
}