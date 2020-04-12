package com.example.mydictionary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mydictionary.R;

public class FragmentDefinition extends Fragment {
    public FragmentDefinition(){

    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
       // View view = inflater.inflate(R.layout.fragment_definition,container, false);
       View view = inflater.inflate(R.layout.fragment_definition,container,false);
       return view;
   }
}

