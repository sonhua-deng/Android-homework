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

public class ItemsListAdapter extends BaseAdapter {
    private List<Items> lItems;
    private Context context;
    public ItemsListAdapter(List<Items> lItems,Context c) {
        this.lItems=lItems;
        context=c;
    }
    @Override
    public int getCount() {
        if(lItems==null) {
            return 0;
        }
        return lItems.size();
    }

    @Override
    public Object getItem(int i) {
        if(lItems==null) {
            return null;
        }
        return lItems.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.itemslist,null);
        Items item=lItems.get(i);
        TextView name=(TextView) view.findViewById(R.id.nameList);
        TextView price=(TextView)view.findViewById(R.id.priceList);
        TextView letter=(TextView)view.findViewById(R.id.itemList);
        name.setText(item.getName());
        price.setText(item.getPrice());
        String let=""+ item.getName().charAt(0);
        letter.setText(let);
        return view;
    }

}
