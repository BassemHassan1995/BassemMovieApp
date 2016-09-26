package com.example.basse.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by basse on 18-Sep-16.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listReviewHeader; // list_group authors
    private HashMap<String, String> mapReviewChild; //list_group review
    private HashMap<String, String> linkedMapReviewChild; //list_group review
    private List<Map.Entry<String, String>> indexedList ;

    public ExpandableListAdapter(Context context, List<String> listReviewHeader,
                                 HashMap<String, String> mapReviewChild) {
        this.context = context;
        this.listReviewHeader = listReviewHeader;
        this.mapReviewChild = mapReviewChild;
    }

    public ExpandableListAdapter(Context context,
                                 HashMap<String, String> linkedMapReviewChild) {
        this.context = context;
        this.linkedMapReviewChild = linkedMapReviewChild;
        indexedList = new ArrayList<Map.Entry<String, String>>(linkedMapReviewChild.entrySet());
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return indexedList.get(groupPosition).getValue();
        //return this.mapReviewChild.get(listReviewHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_content, null);
        }

        final String review_content = getChild(groupPosition, childPosition);
        TextView review_content_textView = (TextView) convertView.findViewById(R.id.review_content_textView);
        review_content_textView.setText(review_content);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public String getGroup(int groupPosition) {
        return indexedList.get(groupPosition).getKey();
//        return listReviewHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return linkedMapReviewChild.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String review_author = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_author, null);
        }

        TextView review_author_textView = (TextView) convertView
                .findViewById(R.id.review_author_textView);
        review_author_textView.setText(review_author);

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
