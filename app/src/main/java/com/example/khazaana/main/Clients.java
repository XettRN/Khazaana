package com.example.khazaana.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.khazaana.AddData;
import com.example.khazaana.Portfolio;
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
                    NavDirections action = ClientsDirections.actionClientsFragToAddStockFragment();
                    Navigation.findNavController(view).navigate(action);
                    return true;
                }
                return Clients.super.onOptionsItemSelected(item);
            }
        });

        LinearLayout layout = view.findViewById(R.id.client_list);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        String[] clients = getResources().getStringArray(R.array.clients);

        for (String client: clients) {
            TextView textView = new TextView(getContext());
            textView.setText(client);
            textView.setLayoutParams(params);
            textView.setOnClickListener(this::goClientPortfolio);
            layout.addView(textView);
        }
    }

    public void goAddData(MenuItem item) {
        Intent intent = new Intent(getContext(), AddData.class);
        startActivity(intent);
    }

    public void goClientPortfolio(View view) {
        Intent intent = new Intent(getContext(), Portfolio.class);
        startActivity(intent);
    }
}