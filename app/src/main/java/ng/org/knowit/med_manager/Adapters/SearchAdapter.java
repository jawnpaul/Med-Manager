package ng.org.knowit.med_manager.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ng.org.knowit.med_manager.Data.MedicineContract;
import ng.org.knowit.med_manager.R;

/**
 * Created by john on 4/10/18.
 */

public class SearchAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mStringList = new ArrayList<>();
    private Context mContext;
    private Cursor mCursor;

    public SearchAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
        this.mContext = context;
        this.mCursor = cursor;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //this.mStringList = customizedListView;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder holder = new ViewHolder();

        holder.Word = (TextView) view.findViewById(R.id.list_item_search);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.Word.setText(cursor.getString(cursor.getColumnIndex(MedicineContract.MedicineEntry.COLUMN_MEDICINE_NAME)));
    }

    static class ViewHolder{
        TextView Word;
    }

}


