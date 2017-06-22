package com.example.amidezcod.advancedrecyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amidezcod on 19/6/17.
 */

public class FragmentExpandableListView extends Fragment {
    private ExpandableListView mExpandableListView;
    private ArrayList<String> mChildArrayList;
    private Map<String, ArrayList<String>> mArrayListMap;
    private ExpandableListAdapter expandableListAdapter;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.expandable_list_view, container, false);

        mExpandableListView = rootview.findViewById(R.id.expandable_view);
        dummyData();
        expandableListAdapter = new MyExlistAdapter();
        mExpandableListView.setAdapter(expandableListAdapter);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getActivity(), mArrayListMap.get(mChildArrayList.get(groupPosition)).get(childPosition) ,Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return rootview;
    }

    private void dummyData() {
        mChildArrayList = new ArrayList<>();
        mArrayListMap = new HashMap<>();

        mChildArrayList.add("Kotlin");
        mChildArrayList.add("Java");
        mChildArrayList.add("C++");

        ArrayList<String> kotlin = new ArrayList<>();
        ArrayList<String> java = new ArrayList<>();
        kotlin.add("Null Exception removed");
        kotlin.add("Boiler Plate Code Removed");
        kotlin.add("Works on top of JAVA");
        kotlin.add("InterConvertible with JAVA");
        kotlin.add("Lambda & Inline Functions");

        java.add("Null Pointer Exception ");
        java.add("Lots of Boiler Plate Code ");
        java.add("Checked Exception");
        java.add("Static Members");
        mArrayListMap.put(mChildArrayList.get(0), kotlin);
        mArrayListMap.put(mChildArrayList.get(1), java);
        mArrayListMap.put(mChildArrayList.get(2), java);

    }

   private class MyExlistAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return mChildArrayList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return mArrayListMap.get(mChildArrayList.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return mChildArrayList.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return mArrayListMap.get(mChildArrayList.get(i)).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpandable, View view, ViewGroup viewGroup) {
            String lang = (String) getGroup(groupPosition);
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_parent, viewGroup, false);
            }
            TextView textView = view.findViewById(R.id.list_paren_text);
            ImageView imageView = view.findViewById(R.id.image_arrow);
            textView.setText(lang);
            if(isExpandable){
                imageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            }else{
                imageView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

            }
            return view;

        }

        @Override
        public View getChildView(int groupPosition, int childpoition, boolean b, View view, ViewGroup viewGroup) {
            String topics = (String) getChild(groupPosition, childpoition);
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_child, viewGroup, false);
            }
            TextView textView = view.findViewById(R.id.list_child_text);
            textView.setText(topics);
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
