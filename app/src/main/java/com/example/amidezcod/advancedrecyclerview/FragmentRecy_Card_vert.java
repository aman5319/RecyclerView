package com.example.amidezcod.advancedrecyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_for_cardview_vertical, container, false);
        setupRecyclerView(rootView);
        setupDataForAdapter();
        setupAdapter();
        itemDecorate();
        fabHide(rootView);
        itemSwipeAnimation();
        return rootView;
    }

    private void itemSwipeAnimation() {
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(50);
        itemAnimator.setRemoveDuration(500);
        mRecyclerView.setItemAnimator(itemAnimator);
    }

    private void fabHide(View view) {
        fab = view.findViewById(R.id.fab2);
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
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int ppos = viewHolder.getAdapterPosition();

                final CallOfDutyPOJO callOfDutyPOJO = callOfDutyPOJOArrayList.get(ppos);
                callOfDutyPOJOArrayList.remove(ppos);
                recyclerViewAdapterForCardView.notifyItemRemoved(viewHolder.getAdapterPosition());
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Card Delete", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                callOfDutyPOJOArrayList.add(ppos, callOfDutyPOJO);
                                recyclerViewAdapterForCardView.notifyItemInserted(ppos);
                            }
                        }).show();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    Paint p = new Paint();
                    p.setColor(Color.parseColor("#388E3C"));
                    if (dX > 0) {
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = getBitmapFromVectorDrawable(getContext(), R.drawable.ic_delete_sweep_black_24dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = getBitmapFromVectorDrawable(getContext(), R.drawable.ic_delete_sweep_black_24dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
                Drawable drawable = ContextCompat.getDrawable(context, drawableId);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    drawable = (DrawableCompat.wrap(drawable)).mutate();
                }

                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);

                return bitmap;
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                getDefaultUIUtil().clearView(viewHolder.itemView);
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
            callOfDutyPOJOArrayList.add(new CallOfDutyPOJO(R.drawable.programming, "Ghost " + i, "Activision", "Raven"));
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
            setAnimation(holder.itemView, position);

        }

        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

        /**
         * Called when a view created by this adapter has been detached from its window.
         * <p>
         * <p>Becoming detached from the window is not necessarily a permanent condition;
         * the consumer of an Adapter's views may choose to cache views offscreen while they
         * are not visible, attaching and detaching them as appropriate.</p>
         *
         * @param holder Holder of the view being detached
         */
        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            holder.itemView.clearAnimation();
        }

        @Override
        public int getItemCount() {
            return callOfDutyPOJOArrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
