package com.example.fahadhd.bodybuildingtracker.Sessions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.R;

import java.util.ArrayList;


public class SessionAdapter extends BaseAdapter {
    Context context;
    ArrayList<Session> sessions;
    public SessionAdapter(Context mContext, ArrayList<Session> sessions) {
        this.context = mContext;
        this.sessions = sessions;
    }


    @Override
    public int getCount() {
        return sessions.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        TextView date;
        TextView user_weight;
        TextView sessionID;
        public ViewHolder(View view){
            this.date = (TextView)view.findViewById(R.id.date);
            this.user_weight = (TextView)view.findViewById(R.id.user_weight);
            this.sessionID = (TextView)view.findViewById(R.id.sessionID);
        }

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;

        //Initializing new row
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.sessions_list_item, null);
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        }
        //Recycling views
        else {

            viewHolder = (ViewHolder) row.getTag();
        }

        Session session = sessions.get(position);
        viewHolder.date.setText(session.getDate());
        viewHolder.user_weight.setText(session.getWeight().toString());
        viewHolder.sessionID.setText(session.getSessionId().toString());
        return row;
    }


}
