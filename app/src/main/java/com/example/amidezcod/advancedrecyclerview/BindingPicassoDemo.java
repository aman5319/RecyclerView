package com.example.amidezcod.advancedrecyclerview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amidezcod.advancedrecyclerview.databinding.PicassoBindingDemoBinding;
import com.squareup.picasso.Picasso;

/**
 * Created by amidezcod on 30/6/17.
 */

public class BindingPicassoDemo extends Fragment {
    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    PicassoBindingDemoBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.picasso_binding_demo, container, false);
        mBinding.text1Binding.setText("Call of Duty Black ops3");
        mBinding.text1Binding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "BlackOps", Toast.LENGTH_SHORT).show();
            }
        });

        Picasso.with(getContext())
                .load("https://i.ytimg.com/vi/Tpm1yk3enzY/maxresdefault.jpg")
                .placeholder(R.drawable.call_of_duty_ghosts)
                .into(mBinding.imagePicasso);

        return mBinding.getRoot();
    }
}
