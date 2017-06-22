package com.example.amidezcod.advancedrecyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by amidezcod on 18/6/17.
 */

public class FragmentRecy_Card_vert extends Fragment {
    private RecyclerView mRecyclerView;
    private ArrayList<CallOfDutyPOJO> callOfDutyPOJOArrayList;
    private RecyclerViewAdapterForCardView recyclerViewAdapterForCardView;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_for_cardview_vertical, container, false);

        setupRecyclerView(rootView);
        setupDataForAdapter();
        setupAdapter();
        itemDecorate();
       fabHide(rootView);
        return rootView;
    }

    private void fabHide(View view) {
        fab= view.findViewById(R.id.fab2);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        fab.show();
                        break;
                    default:
                        fab.hide();
                        break;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    private void itemDecorate() {
        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; // for drag and drop
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END; // for swipe left and right
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            // method for drag and drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(callOfDutyPOJOArrayList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                recyclerViewAdapterForCardView.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            // method for swiping left and right
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                callOfDutyPOJOArrayList.remove(viewHolder.getAdapterPosition());
                recyclerViewAdapterForCardView.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void setupAdapter() {
        recyclerViewAdapterForCardView = new RecyclerViewAdapterForCardView(callOfDutyPOJOArrayList);
        mRecyclerView.setAdapter(recyclerViewAdapterForCardView);
    }

    private void setupDataForAdapter() {
        callOfDutyPOJOArrayList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            callOfDutyPOJOArrayList.add(new CallOfDutyPOJO(R.drawable.b, "Ghost " + i, "Activision", "Raven"));
        }

    }

    private void setupRecyclerView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }
    
    class RecyclerViewAdapterForCardView extends
            RecyclerView.Adapter<RecyclerViewAdapterForCardView.ViewHolder> {
        ArrayList<CallOfDutyPOJO> callOfDutyPOJOArrayList;

        private RecyclerViewAdapterForCardView(ArrayList<CallOfDutyPOJO> callOfDutyPOJOArrayList) {
            this.callOfDutyPOJOArrayList = callOfDutyPOJOArrayList;
        }

        @Override
        public RecyclerViewAdapterForCardView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_vertical_linear, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapterForCardView.ViewHolder holder, int position) {
            holder.gameName.setText(callOfDutyPOJOArrayList.get(position).gameName);
            holder.gameDeveloper1.setText(callOfDutyPOJOArrayList.get(position).DeveloperName1);
            holder.gameDeveloper2.setText(callOfDutyPOJOArrayList.get(position).DeveloperName2);
            holder.gamePhoto.setImageResource(callOfDutyPOJOArrayList.get(position).imageId);

        }

        @Override
        public int getItemCount() {
            return callOfDutyPOJOArrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            CardView cv;
            TextView gameName;
            TextView gameDeveloper1;
            TextView gameDeveloper2;
            ImageView gamePhoto;

            private ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                cv = itemView.findViewById(R.id.cardview_vertical);
                gameName = itemView.findViewById(R.id.cardview_game_name);
                gameDeveloper1 = itemView.findViewById(R.id.cardview_game_developer1);
                gameDeveloper2 = itemView.findViewById(R.id.cardview_game_developer2);
                gamePhoto = itemView.findViewById(R.id.cardview_game_image);

            }

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), gameName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
