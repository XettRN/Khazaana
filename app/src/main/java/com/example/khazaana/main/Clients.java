package com.example.khazaana.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.khazaana.AddData;
import com.example.khazaana.R;

public class Clients extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar clientBar = view.findViewById(R.id.clientBar);
        clientBar.inflateMenu(R.menu.client_toolbar);
        clientBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.toolbar_add) {
                    goAddData(item);
                    return true;
                }
                return Clients.super.onOptionsItemSelected(item);
            }
        });
    }

    public void goAddData(MenuItem item) {
        Intent intent = new Intent(getContext(), AddData.class);
        startActivity(intent);
    }
}