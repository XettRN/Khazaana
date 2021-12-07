package com.example.khazaana.main.ifa;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.khazaana.R;

public class IFASettings extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.ifa_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button previousPage = view.findViewById(R.id.back_ifa_settings);
        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = IFASettingsDirections.actionIFASettingsToIFAAccountSettings();
                Navigation.findNavController(view).navigate(action);
            }
        });

        Button addClient = view.findViewById(R.id.add_client);
        addClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = IFASettingsDirections.actionIFASettingsToIFAAccountSettings();
                Navigation.findNavController(view).navigate(action);
            }
        });

        Button removeClient = view.findViewById(R.id.remove_client);
        removeClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = IFASettingsDirections.actionIFASettingsToRemoveClient2();
                Navigation.findNavController(view).navigate(action);
            }
        });

        Button updatePwd = view.findViewById(R.id.ifa_update_password);
        updatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = IFASettingsDirections.actionIFASettingsToIFAUpdatePassword();
                Navigation.findNavController(view).navigate(action);
            }
        });
    }
}