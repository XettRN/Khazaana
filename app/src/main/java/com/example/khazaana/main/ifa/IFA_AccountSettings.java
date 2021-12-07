package com.example.khazaana.main.ifa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.khazaana.R;

public class IFA_AccountSettings extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.ifa_account_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button profile = view.findViewById(R.id.ifa_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = IFA_AccountSettingsDirections.actionIFAAccountSettingsToProfileFrag();
                Navigation.findNavController(view).navigate(action);
            }
        });

        Button settings = view.findViewById(R.id.ifa_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = IFA_AccountSettingsDirections.actionIFAAccountSettingsToIFASettings();
                Navigation.findNavController(view).navigate(action);
            }
        });
    }
}