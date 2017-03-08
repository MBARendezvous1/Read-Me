package com.shwetak3e.readfile.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwetak3e.readfile.R;

import java.util.List;

/**
 * Created by Pervacio on 3/7/2017.
 */

public class FilesAdapter extends BaseAdapter {


    private Context mContext;
    private List<String> fileNames;
    LayoutInflater inflater;


    public FilesAdapter(Context context, List<String> fileNames){
        this.mContext=context;
        this. fileNames=fileNames;
        this.inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return fileNames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        if (convertView == null) {
            convertView= inflater.inflate(R.layout.file_grid, null);
            holder.fileName = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder=(Holder) convertView.getTag();
        }
        holder.fileName.setText(fileNames.get(position));
        return convertView;
    }

    class Holder{
        TextView fileName;
    }
}
