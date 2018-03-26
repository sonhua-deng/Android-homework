package com.example.lenovo.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

/**
 * Created by Lenovo on 2017/10/20.
 */

public class detailListAdapter extends BaseAdapter {
    private List<detailItem> dItems;
    private Context context;
    public  detailListAdapter(List<detailItem> dItems,Context c) {
        this.dItems=dItems;
        context=c;
    }
    @Override
    public int getCount() {
        if(dItems==null) {
            return 0;
        }
        return dItems.size();
    }

    @Override
    public Object getItem(int i) {
        if(dItems==null) {
            return null;
        }
        return dItems.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.detailitems,null);
        TextView name=(TextView) view.findViewById(R.id.detailItemName);
        name.setText(dItems.get(i).getName());
        return view;
    }

}
