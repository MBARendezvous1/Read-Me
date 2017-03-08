package com.shwetak3e.readfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.shwetak3e.readfile.Adapters.OccurenceAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OccurenceActivity extends AppCompatActivity {

    ExpandableListView wordList;
    TextView emptyFile;
    OccurenceAdapter occurenceAdapter;
    HashMap<String,List<String>> word_freq_map=new HashMap<>();

    static int expandpos;
    static int prevPosition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occurence);
        emptyFile=(TextView)findViewById(R.id.emptyFile);
        emptyFile.setVisibility(View.GONE);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {

            word_freq_map = (HashMap<String, List<String>>) bundle.getSerializable(SelectFileActivity.FINALWORDFREQLIST);
        }

        if(word_freq_map.size()==0){
            emptyFile.setVisibility(View.VISIBLE);
        }
        word_freq_map=sortGroupsbyKeys(word_freq_map);

        occurenceAdapter=new OccurenceAdapter(this,word_freq_map);
        wordList=(ExpandableListView)findViewById(R.id.wordList);
        wordList.setAdapter(occurenceAdapter);
        wordList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                    expandpos=groupPosition;
                    OccurenceAdapter customExpandAdapter = (OccurenceAdapter) wordList.getExpandableListAdapter();
                    if (customExpandAdapter == null) {
                        return;
                    }

                    if(prevPosition!=-1 && prevPosition!=groupPosition){
                        wordList.collapseGroup(prevPosition);
                    }

                    prevPosition=groupPosition;

            }
        });

    }

    private LinkedHashMap<String, List<String>> sortGroupsbyKeys(Map<String, List<String>> map) {
        List<String> mapKeys = new ArrayList<>(map.keySet());
        Collections.sort(mapKeys);
        LinkedHashMap<String, List<String>> sortedMap =
                new LinkedHashMap<>();

        Iterator<String> keyIt=mapKeys.iterator();
        List<String> val=new ArrayList<String>();
        while(keyIt.hasNext()){
            String key=keyIt.next();
            val=map.get(key);
            keyIt.remove();
            sortedMap.put(key,val);
        }

        return  sortedMap;

    }

}
