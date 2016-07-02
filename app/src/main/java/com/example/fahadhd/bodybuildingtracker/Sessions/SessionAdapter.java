package com.example.fahadhd.bodybuildingtracker.Sessions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


public class SessionAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Session> sessions;
    public SessionAdapter(Context mContext, ArrayList<Session> sessionIDs) {
        this.mContext = mContext;
        this.sessions = sessionIDs;
    }


    @Override
    public int getCount() {
        return sessions.size();
    }

    @Override
    public Session getItem(int position) {
        return sessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;

    }

}
