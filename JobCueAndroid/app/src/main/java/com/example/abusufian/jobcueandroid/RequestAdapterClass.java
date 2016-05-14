package com.example.abusufian.jobcueandroid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by abusufian on 5/12/2016.
 */
public class RequestAdapterClass extends ArrayAdapter<String> {

    int groupid;
    String[] firstName;
    Context context;

    public RequestAdapterClass (Context context, int vg, int id, String[] fName)
    {
        super(context, vg, id, fName);
        this.context=context;
        groupid=vg;
        this.firstName=fName;
    }



    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public TextView textview;
        public Button button;

    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        // Inflate the list_item.xml file if convertView is null
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textview= (TextView) rowView.findViewById(R.id.usertext);
            viewHolder.button= (Button) rowView.findViewById(R.id.description);
            rowView.setTag(viewHolder);



        }
        // Set text to each TextView of ListView item
        ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.textview.setText(firstName[position]);
        holder.button.setText("Description");
        return rowView;
    }


}
