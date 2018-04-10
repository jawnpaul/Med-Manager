/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package ng.org.knowit.med_manager.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ng.org.knowit.med_manager.Data.MedicineContract;
import ng.org.knowit.med_manager.R;

@SuppressWarnings({"UnnecessaryLocalVariable", "SameParameterValue", "unused", "UnusedAssignment"})
public class SearchAdapter extends CursorAdapter {
    private final LayoutInflater mLayoutInflater;
    private ArrayList<String> mStringList = new ArrayList<>();

    public SearchAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
        Context context1 = context;
        Cursor cursor1 = cursor;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //this.mStringList = customizedListView;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder holder = new ViewHolder();

        holder.Word = view.findViewById(R.id.list_item_search);

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


