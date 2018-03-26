package com.example.lab11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lab11.R;
import com.example.lab11.Repos;

import java.util.List;

/**
 * Created by Lenovo on 2017/10/20.
 */

public class ItemsListAdapter extends BaseAdapter {
    private List<Repos> repos;
    private Context context;
    public ItemsListAdapter(List<Repos> lItems, Context c) {
        this.repos=lItems;
        context=c;
    }
    @Override
    public int getCount() {
        if(repos==null) {
            return 0;
        }
        return repos.size();
    }

    @Override
    public Object getItem(int i) {
        if(repos==null) {
            return null;
        }
        return repos.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.listitem,null);
        Repos item=repos.get(i);
        TextView name=(TextView) view.findViewById(R.id.text1_l);
        TextView language=(TextView)view.findViewById(R.id.text2_l);
        TextView des=(TextView)view.findViewById(R.id.text3_l);
        name.setText(item.getName());
        language.setText(item.getLanguage());
        String flag= item.getDescription();
        if(flag!=null) {
            if(flag.length()>46) {
                flag="...";
            }
        }

        des.setText(flag);
        return view;
    }

}
