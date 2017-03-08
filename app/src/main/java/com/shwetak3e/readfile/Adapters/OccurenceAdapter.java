package com.shwetak3e.readfile.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.shwetak3e.readfile.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pervacio on 3/7/2017.
 */

public class OccurenceAdapter extends BaseExpandableListAdapter {

    private Context context;
    HashMap<String,List<String>> word_freq_map=new HashMap<>();
    ArrayList<String> freq_group_list=new ArrayList<>();
    LayoutInflater layoutInflater;

    public OccurenceAdapter(Context context, HashMap<String, List<String>> word_freq_map) {
        this.context = context;
        this.word_freq_map = word_freq_map;
        for (Map.Entry<String, List<String>> entry : word_freq_map.entrySet()) {
            Log.i("TAG1", entry.getKey() + " :\n" + entry.getValue().size());
        }
        this.freq_group_list = new ArrayList<>(word_freq_map.keySet());
        this.layoutInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getGroupCount() {
        if(freq_group_list!=null){
            return freq_group_list.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(word_freq_map!=null){
            return word_freq_map.get(freq_group_list.get(groupPosition)).size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if(freq_group_list!=null){
            return freq_group_list.get(groupPosition);
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(word_freq_map!=null && freq_group_list!=null){
            return word_freq_map.get(freq_group_list.get(groupPosition)).get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.frequency_group,null);
        }
        String group="";
        TextView freqGroup=(TextView)convertView.findViewById(R.id.freq_group);
        if(freq_group_list!=null) {
            group = freq_group_list.get(groupPosition);
        }
        freqGroup.setText(group);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.word_frequency_row,null);
        }
        String word="";
        TextView wordFrequency=(TextView)convertView.findViewById(R.id.word_freq);
        if(freq_group_list!=null && word_freq_map!=null){
            word=word_freq_map.get(freq_group_list.get(groupPosition)).get(childPosition);
        }
        wordFrequency.setText(word);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
