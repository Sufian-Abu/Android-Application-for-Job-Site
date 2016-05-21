package com.example.abusufian.jobcueandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by abusufian on 5/20/2016.
 */
public class ListViewAdapterForFoundJobs extends ArrayAdapter<String> {

    int groupid;
    String[] item_list;
    Context context;
    String []description;
    String []id_job;

    public ListViewAdapterForFoundJobs(Context context, int vg, int id, String[] item_list)
    {
        super(context,vg, id, item_list);
        this.context=context;
        groupid=vg;
        this.item_list=item_list;
    }

    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public TextView textview;
        public Button button;
        public Button button2;

    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        // Inflate the list_item.xml file if convertView is null
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textview= (TextView) rowView.findViewById(R.id.usertile);
            viewHolder.button= (Button) rowView.findViewById(R.id.jobdescription);
            viewHolder.button2=(Button)rowView.findViewById(R.id.employer);
            rowView.setTag(viewHolder);



        }
        // Set text to each TextView of ListView item
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.textview.setText(item_list[position]);
        holder.button.setText("Description");
        holder.button2.setText("Employer");





        return rowView;
    }


}
