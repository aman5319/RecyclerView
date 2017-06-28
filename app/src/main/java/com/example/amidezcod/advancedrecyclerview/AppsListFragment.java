package com.example.amidezcod.advancedrecyclerview;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amidezcod on 24/6/17.
 */

public class AppsListFragment extends Fragment {
    private List<ApplicationInfo> packages;
    private PackageManager packageManager;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        packageManager = context.getPackageManager();
        packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
    }

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
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_apps_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.list_item_app);

        // Setup D&D feature and RecyclerView
        RecyclerViewDragDropManager dragMgr = new RecyclerViewDragDropManager();

        dragMgr.setInitiateOnMove(false);
        dragMgr.setInitiateOnLongPress(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(dragMgr.createWrappedAdapter(new MyAdapter()));

        dragMgr.attachRecyclerView(recyclerView);

        return view;
    }

    private static class MyItem {
        public final long id;
        public final String text;
        private Drawable imageDrawable;

        private MyItem(long id, String text, Drawable imageId) {
            this.id = id;
            this.text = text;
            this.imageDrawable = imageId;
        }
    }

    static class MyViewHolder extends AbstractDraggableItemViewHolder {
        TextView textView;
        ImageView imageView;

        private MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_app_list);
            imageView = itemView.findViewById(R.id.image_app_list);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements DraggableItemAdapter<MyViewHolder> {
        List<MyItem> mItems;


        private MyAdapter() {
            setHasStableIds(true); // this is required for D&D feature.
            mItems = new ArrayList<>();
            for (int i = 0; i < packages.size(); i++) {
                String[] strings = packages.get(i).packageName.split("\\.");
                String s = capitalizeFirstLetter(strings[strings.length - 1]);
                mItems.add(new MyItem(i, s, packages.get(i).loadIcon(packageManager)));
            }

        }

        @Override
        public long getItemId(int position) {
            return mItems.get(position).id; // need to return stable (= not change even after reordered) value
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_for_app_list, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final MyItem item = mItems.get(position);
            holder.textView.setText(item.text);
            holder.imageView.setImageDrawable(item.imageDrawable);
            //click listner won't work properly with drag and drop
//            holder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int adapterPos = holder.getAdapterPosition();
//                    if (adapterPos != RecyclerView.NO_POSITION) {
//                        String packageName = packages.get(adapterPos).packageName;
//                        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
//                        if (intent == null) {
//                            // Bring user to the market or let them choose an app?
//                            intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setData(Uri.parse("market://details?id=" + packageName));
//                        }
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                }
//            });
        }


        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @Override
        public void onMoveItem(int fromPosition, int toPosition) {
            MyItem movedItem = mItems.remove(fromPosition);
            mItems.add(toPosition, movedItem);
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public boolean onCheckCanStartDrag(MyViewHolder holder, int position, int x, int y) {
            return true;
        }

        @Override
        public ItemDraggableRange onGetItemDraggableRange(MyViewHolder holder, int position) {
            return null;
        }

        @Override
        public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
            return true;
        }

        private String capitalizeFirstLetter(String original) {
            if (original == null || original.length() == 0) {
                return original;
            }
            return original.substring(0, 1).toUpperCase() + original.substring(1);


        }
    }
}



