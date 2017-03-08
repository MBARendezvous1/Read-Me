package com.shwetak3e.readfile;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shwetak3e.readfile.Adapters.FilesAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SelectFileActivity extends AppCompatActivity {

    GridView fileGrid;
    TextView noFileText;

    List<String> fileNames = new ArrayList<String>();
    List<File> filesList = new ArrayList<File>();
    List<String> occurences=new ArrayList<String>();
    Map<String, List<String>> word_freq_map=new LinkedHashMap<String, List<String>>();
    Button next;

    FilesAdapter filesAdapter;
    static int selected_pos=0;


    public static String FINALWORDFREQLIST="final_word_freq_list";


    private static final String pathName = Environment.getExternalStorageDirectory()
            .getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        next=(Button)findViewById(R.id.next);
        noFileText=(TextView)findViewById(R.id.noFile);
        noFileText.setVisibility(View.INVISIBLE);


        fileGrid=(GridView)findViewById(R.id.fileGrid);
        if(!fileGrid.isSelected()){
            next.setClickable(false);
        }
        getFileNames(pathName);
        filesAdapter=new FilesAdapter(this,fileNames);
        fileGrid.setAdapter(filesAdapter);
        fileGrid.setItemChecked(selected_pos,true);
        fileGrid.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        fileGrid.setSelector(getResources().getDrawable(R.drawable.bg_each_file_holder));
        fileGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_pos=position;
                next.setActivated(true);
                next.setClickable(true);
            }
        });


    }

    public void getFileNames(String dir) {
        File directory = new File(dir);

        if (directory != null) {

            directory.mkdirs();
            File[] files = directory.listFiles();
            if (files != null) {
                if (files.length == 0)
                    return;
                else {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            getFileNames(file.getAbsolutePath());
                        } else if (file.getName().endsWith(".txt")
                                || file.getName().endsWith(".rtf")
                                || file.getName().endsWith(".log")
                                || file.getName().endsWith(".docx")) {
                            fileNames.add(file.getName());
                            filesList.add(file);
                        }
                    }
                }
            } else {
                noFileText.setVisibility(View.VISIBLE);
                fileGrid.setVisibility(View.GONE);
                Log.i("TAG", "No files");
            }
        }
    }


    public void startReading(View view){
        readFile(filesList.get(selected_pos));
        Intent intent =new Intent(this,OccurenceActivity.class);
        intent.putExtra(FINALWORDFREQLIST,new LinkedHashMap<String,List<String>>(word_freq_map));
        startActivity(intent);
    }


    void readFile(File file) {
        BufferedReader bufferedReader;
        String newLine;
        String wordsInLine[] = null;
        word_freq_map=new LinkedHashMap<String,List<String>>();
        Map<String, Integer> wordFrequency = new LinkedHashMap<String, Integer>();
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((newLine = bufferedReader.readLine()) != null) {
                newLine = newLine.toUpperCase();
                wordsInLine = newLine.split("\\s+"); //Splits the words based on the regular expression of space

                if (wordsInLine != null || newLine.length()==0) {
                    for (String word : wordsInLine) {
                        if (wordFrequency.containsKey(word)) {
                            wordFrequency.put(word, wordFrequency.get(word) + 1);
                        } else {
                            wordFrequency.put(word, 1);
                        }

                    }
                }else{
                   Log.i("TAG","No Words in New Line");
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        wordFrequency=sortwordsByValues(wordFrequency);
        int  group=-1;
        String freq_group="";
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {

            if(group==-1){
                group = entry.getValue() / 10;
                freq_group=group==0?"0-10":Integer.toString(group*10+1)+"-"+Integer.toString(group*10+10);
            }
            else if(entry.getValue()/10 != group ) {
                word_freq_map.put(freq_group,occurences);
                occurences=new ArrayList<>();
                group = entry.getValue() / 10;
                freq_group=group==0?"0-10":Integer.toString(group*10+1)+"-"+Integer.toString(group*10+10);
            }
            occurences.add(entry.getKey()+"   :   "+ entry.getValue());
        }
        if(freq_group!="")
        word_freq_map.put(freq_group,occurences);
    }


    private LinkedHashMap<String, Integer> sortwordsByValues(Map<String, Integer> map) {
        List<String> mapKeys = new ArrayList<>(map.keySet());
        List<Integer> mapValues = new ArrayList<>(map.values());
        Collections.sort(mapKeys);
        Collections.sort(mapValues);

        LinkedHashMap<String, Integer> sortedMap =
                new LinkedHashMap<>();

        Iterator<Integer> valueIt = mapValues.iterator();
        Integer val=0;
        while (valueIt.hasNext()) {
            val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer val1 = map.get(key);
                Integer val2 = val;

                if (val1.equals(val2)) {
                    keyIt.remove();
                    sortedMap.put(key,val);
                    break;
                }
            }
        }

        return sortedMap;
    }

}
