package accurate.gaw.livestreamingapp.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import accurate.gaw.livestreamingapp.R;

public class FilterAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;


    public FilterAdapter(Activity context, String[] maintitle) {
       super(context, R.layout.filterlistlayout, maintitle);

        this.context=context;
        this.maintitle=maintitle;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        android.view.View rowView=inflater.inflate(R.layout.filterlistlayout, null,true);


        return rowView;

    };
}