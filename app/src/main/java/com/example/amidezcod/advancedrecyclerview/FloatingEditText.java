package com.example.amidezcod.advancedrecyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by amidezcod on 17/6/17.
 */

public class FloatingEditText extends Fragment {
    public FloatingEditText() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.floating_edittext , container ,false);
        return rootView;
    }

}
