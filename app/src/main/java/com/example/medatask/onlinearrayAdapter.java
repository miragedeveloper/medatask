package com.example.medatask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class onlinearrayAdapter extends ArrayAdapter {
    private Context context;
    private  int resource;
    private List<user> mlist;
    public  onlinearrayAdapter(Context context,int resource,List<user> mlist){
        super(context,resource,mlist);
        this.context=context;
        this.resource=resource;
        this.mlist=mlist;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        user curr_user=mlist.get(position);
        View view= LayoutInflater.from(context).inflate(resource,parent,false);
        TextView name=view.findViewById(R.id.name);
        name.setText(curr_user.getName());
        return view;
    }
    @Override
    public Object getItem(int position){
        return mlist.get(position);
    }

}
