package com.example.listapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    //The list of messages to be displayed
    private List<Message> itemList;
    //An object that can create/inflate views using the context/activity provided
    private LayoutInflater inflater;
    public ListViewAdapter(Activity context, List<Message> itemList) {
        super();
        this.itemList = itemList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Abstract methods used by parent
    public int getCount(){ return itemList.size();}
    public Object getItem(int position) {return itemList.get (position);}
    public long getItemId(int position) {return position; }

    // Convenient store to be used with tagging
    public static class ViewHolder {
        TextView txtViewTitle;
        TextView txtViewSubtitle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView==null) {
                //No previous view, instantiate holder, inflate view and setup views of holder
                holder = new ViewHolder();
                convertView = inflater. inflate(R.layout.listitem_row, null);

                holder.txtViewTitle = (TextView) convertView. findViewById(R.id.txtViewTitle);
                holder.txtViewSubtitle = (TextView) convertView.findViewById(R.id.txtViewSubtitle);

                //Remember the holder inside the view
                convertView.setTag(holder);
            }
            else
                //Get holder to prepare for update with new data
                holder=(ViewHolder)convertView.getTag();

            //Update views
            Message message = (Message) itemList.get(position);
            holder.txtViewTitle.setText(message.getTitle());
            holder.txtViewSubtitle.setText(message.getSubtitle());

            return convertView;
     }

}