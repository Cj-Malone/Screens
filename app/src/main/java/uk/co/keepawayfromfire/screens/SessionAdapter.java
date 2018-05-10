package uk.co.keepawayfromfire.screens;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cj on 21/01/18.
 */

public class SessionAdapter extends ArrayAdapter<Session> {

    private final PackageManager packageManager;
    private final ArrayList<Session> values;

    public SessionAdapter(Context context, ArrayList<Session> values) {
        super(context, -1, values);

        this.packageManager = context.getPackageManager();
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View sessionView = convertView;

        if (sessionView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            sessionView = inflater.inflate(R.layout.session_view, parent, false);

            SessionAdapter.ViewHolder viewHolder = new SessionAdapter.ViewHolder();

            viewHolder.icon1ImageView = sessionView.findViewById(R.id.icon1ImageView);
            viewHolder.icon2ImageView = sessionView.findViewById(R.id.icon2ImageView);
            viewHolder.nameTextView = sessionView.findViewById(R.id.nameTextView);

            sessionView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) sessionView.getTag();
        if (values.get(position).package1 != null)
            viewHolder.icon1ImageView.setImageDrawable(values.get(position).package1.loadIcon(packageManager));
        if (values.get(position).package2 != null)
            viewHolder.icon2ImageView.setImageDrawable(values.get(position).package2.loadIcon(packageManager));
        viewHolder.icon2ImageView.setImageDrawable(getContext().getDrawable(R.drawable.logo));
        viewHolder.icon1ImageView.setImageDrawable(getContext().getDrawable(R.drawable.logo));
        viewHolder.nameTextView.setText(values.get(position).name);

        return sessionView;
    }

    static class ViewHolder {
        public ImageView icon1ImageView;
        public ImageView icon2ImageView;
        public TextView nameTextView;
    }
}
