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

    // Recycler view
    private RecyclerView mRecyclerView;
    //arrayList for Storing information that to be populated via Recycler view
    private ArrayList<CallOfDutyPOJO> callOfDutyPOJOArrayList;
    //Recycler view Adapter
    private RecyclerViewAdapterForCardView recyclerViewAdapterForCardView;
    //fab
    private FloatingActionButton fab;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_for_cardview_vertical, container, false);

        //setups up the recycler view with LinearLayoutManager
        setupRecyclerView(rootView);
        //add data to arrayList
        setupDataForAdapter();

        //adds the arrayList data to adapter
        setupAdapter();

        //handles the item Swipe Drag Drop event
        itemDecorate();

        //hide the fab on scroll of List
        fabHide(rootView);

        //setups the animation on item swipe
        itemSwipeAnimation();
        //return the view
        return rootView;
    }

    private void itemSwipeAnimation() {
        //take the default itemAnimator and adds animation on removal or addition of views

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        //add duration for add element
        itemAnimator.setAddDuration(50);
        //remove duration when item remove
        itemAnimator.setRemoveDuration(500);
        //attach the item animator with recyclerview
        mRecyclerView.setItemAnimator(itemAnimator);
    }

    private void fabHide(View view) {
        fab = view.findViewById(R.id.fab2);
        //add the scrollListener
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    //in idle state i.e when it is not scrolling then show fab else hide
                    case RecyclerView.SCROLL_STATE_IDLE:
                        fab.show();
                        break;
                    default:
                        //hide when it is scrolling
                        fab.hide();
                        break;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    private void itemDecorate() {
        //using ItemTouchHelper class we can manipulate how the view behaves on swipe Drag Drop Gesture
        //how the child should be drawn beneath the view
        new ItemTouchHelper(new ItemTouchHelper.Callback() {

            //give the movement flags i.e direction to handle for item movement
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; // for drag and drop
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END; // for swipe left and right
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            // method for drag and drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //swap the viewholder with target
                Collections.swap(callOfDutyPOJOArrayList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
               /*
               we have to notify the Recycler view that data has changed
                */
                recyclerViewAdapterForCardView.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            // method for swiping left and right
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                //get the adapter Position
                final int ppos = viewHolder.getAdapterPosition();
                //get the data element which has to be deleted for undo purpose
                final CallOfDutyPOJO callOfDutyPOJO = callOfDutyPOJOArrayList.get(ppos);
                //remove the data from arraylist
                callOfDutyPOJOArrayList.remove(ppos);
                //notify adapter about deletion
                recyclerViewAdapterForCardView.notifyItemRemoved(viewHolder.getAdapterPosition());
                //show a snackbarwith undo action to undo the deletion
                //while using snackBar inside a fragment we have to use getActivity().findViewById(android.R.id.content)
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Card Delete", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //add the callOfDutyPOJO on ppos position
                                callOfDutyPOJOArrayList.add(ppos, callOfDutyPOJO);
                                //notify recycler view
                                recyclerViewAdapterForCardView.notifyItemInserted(ppos);
                            }
                        }).show();
            }
            //for drawing background thing
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                //draw on action statw swipe
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    //get the view
                    View itemView = viewHolder.itemView;
                    //calculate height and width of view
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                   //USE paint class for color
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

            // to convert Vector Drawables into bitmap
            //this method is required because vector drawables are not normal drawables
            private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
                //get the drawable
                Drawable drawable = ContextCompat.getDrawable(context, drawableId);
                // this conversion can only be done if SDK is above Lollipop
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    /*
                     * Potentially wrap {@code drawable} so that it may be used for tinting across the
                     * different API levels, via the tinting methods in this class.
                     *
                     * <p>If the given drawable is wrapped, we will copy over certain state over to the wrapped
                     * drawable, such as its bounds, level, visibility and state.</p>
                     *
                     * <p>You must use the result of this call. If the given drawable is being used by a view
                     * (as its background for instance), you must replace the original drawable with
                     * the result of this call:</p>
                     */

                    // A mutable drawable is guaranteed to not share its state with any other drawable
                    // This is especially useful when you need to modify properties of drawables loaded from resources
                    drawable = (DrawableCompat.wrap(drawable)).mutate();
                }
                //create a bitmap with drawable
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

                //When you're writing an application in which you would like to perform specialized drawing and/or control the animation of graphics,
                // you should do so by drawing through a Canvas. A Canvas works for you as a pretense, or interface,
                // to the actual surface upon which your graphics will be drawn — it holds all of your "draw" calls.
                // Via the Canvas, your drawing is actually performed upon an underlying Bitmap, which is placed into the window.

                Canvas canvas = new Canvas(bitmap);
                //Specify a bounding rectangle for the Drawable.

                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                //draw the content for drawable
                drawable.draw(canvas);

                return bitmap;
            }

            //clear the background child used when we use child draw method
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //default callBack for ItemTouchItemUtil to clear the view of background
                getDefaultUIUtil().clearView(viewHolder.itemView);
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void setupAdapter() {
        recyclerViewAdapterForCardView = new RecyclerViewAdapterForCardView(callOfDutyPOJOArrayList);
        //attach adapter with Recycler view
        mRecyclerView.setAdapter(recyclerViewAdapterForCardView);
    }

    private void setupDataForAdapter() {
        callOfDutyPOJOArrayList = new ArrayList<>();
        // add Data
        for (int i = 0; i < 20; i++) {
            callOfDutyPOJOArrayList.add(new CallOfDutyPOJO(R.drawable.programming, "Ghost " + i, "Activision", "Raven"));
        }

    }

    private void setupRecyclerView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        //if Recycler view Populates the item all of same size then use below method for fast loading
        mRecyclerView.setHasFixedSize(true);
        //Linearlayout manager for vertical scrolling as defalut
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //set layout Manager
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    //adapter class
    class RecyclerViewAdapterForCardView extends
            RecyclerView.Adapter<RecyclerViewAdapterForCardView.ViewHolder> {
        ArrayList<CallOfDutyPOJO> callOfDutyPOJOArrayList;

        //constructor for Recyclerview taking arrayList
        private RecyclerViewAdapterForCardView(ArrayList<CallOfDutyPOJO> callOfDutyPOJOArrayList) {
            this.callOfDutyPOJOArrayList = callOfDutyPOJOArrayList;
        }

        //create view
        @Override
        public RecyclerViewAdapterForCardView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_vertical_linear, parent, false);
            return new ViewHolder(view);
        }

        // binds the view holder with recyclerview
        @Override
        public void onBindViewHolder(RecyclerViewAdapterForCardView.ViewHolder holder, int position) {
            holder.gameName.setText(callOfDutyPOJOArrayList.get(position).gameName);
            holder.gameDeveloper1.setText(callOfDutyPOJOArrayList.get(position).DeveloperName1);
            holder.gameDeveloper2.setText(callOfDutyPOJOArrayList.get(position).DeveloperName2);
            holder.gamePhoto.setImageResource(callOfDutyPOJOArrayList.get(position).imageId);
            /*
             * <p>called to animated the view with some kind of animation on load
             * of view</p>
             * @param ViewHolder viewholder which has to be animated
             * @param position position of the view
             */
            setAnimation(holder.itemView, position);

        }

        //for animating when view Loads
        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                //use animation class to load default animations
                Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
                //start the animation
                viewToAnimate.startAnimation(animation);
                //update the lastPosition
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
        //for animation purpose on fast scrolling the animation should be cleared because
        //while recycler view is animating and the user scrolls then it will not give a smooth scroll
        //and overlaping of card might happen if not given
        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            //clear animation on view
            holder.itemView.clearAnimation();
        }

        @Override
        public int getItemCount() {
            return callOfDutyPOJOArrayList.size();
        }


        //viewholder class for RecyclerView
        //used when want to handle click events on complete view
        //every view is attached to View.OnClickListener
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            CardView cv;
            TextView gameName;
            TextView gameDeveloper1;
            TextView gameDeveloper2;
            ImageView gamePhoto;

            private ViewHolder(View itemView) {
                super(itemView);
                //attach the clickListener
                itemView.setOnClickListener(this);
                cv = itemView.findViewById(R.id.cardview_vertical);
                gameName = itemView.findViewById(R.id.cardview_game_name);
                gameDeveloper1 = itemView.findViewById(R.id.cardview_game_developer1);
                gameDeveloper2 = itemView.findViewById(R.id.cardview_game_developer2);
                gamePhoto = itemView.findViewById(R.id.cardview_game_image);

            }

            //click events for view
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), gameName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
