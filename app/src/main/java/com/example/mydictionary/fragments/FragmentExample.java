package com.example.mydictionary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mydictionary.R;

public class FragmentExample extends Fragment {
    public FragmentExample(){

    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
       // View view = inflater.inflate(R.layout.fragment_definition,container, false);
       View view = inflater.inflate(R.layout.fragment_definition,container,false);
       return view;
   }
}

