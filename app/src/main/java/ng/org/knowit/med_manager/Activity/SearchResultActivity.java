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

package ng.org.knowit.med_manager.Activity;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import ng.org.knowit.med_manager.Adapters.SearchAdapter;
import ng.org.knowit.med_manager.Data.MedicineContract;
import ng.org.knowit.med_manager.Data.MedicineDbHelper;
import ng.org.knowit.med_manager.R;

public class SearchResultActivity extends AppCompatActivity {

    private String mQuery;

    private MedicineDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        handleIntent(getIntent());

         dbHelper = new MedicineDbHelper(this);

        Cursor cursor = getMedicineListByKeyWord(mQuery);

        SearchAdapter searchAdapter = new SearchAdapter(this, cursor, 0);
        if (cursor==null){
            Toast.makeText(SearchResultActivity.this,"No records found!",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(SearchResultActivity.this, cursor.getCount() + " records found!",Toast.LENGTH_LONG).show();
        }

        ListView listView = findViewById(R.id.search_result_list_view);
        listView.setAdapter(searchAdapter);

        searchAdapter.swapCursor(cursor);

        Toolbar toolbar = findViewById(R.id.toolbar_search_result_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Search Result");
        //noinspection deprecation
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //noinspection deprecation
        toolbar.setBackgroundColor(getResources().getColor(R.color.blue));


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            @SuppressWarnings("UnnecessaryLocalVariable") String query = intent.getStringExtra(SearchManager.QUERY);
            mQuery = query;
        }
    }

    private Cursor getMedicineListByKeyWord(String search){
        SQLiteDatabase SQLiteDatabase = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  rowid as " +
                MedicineContract.MedicineEntry._ID + "," +
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_NAME + "," +
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_DESCRIPTION + "," +
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_FREQUENCY + "," +
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_DURATION + "," +
                MedicineContract.MedicineEntry.COLUMN_TIMESTAMP +
                " FROM " + MedicineContract.MedicineEntry.TABLE_NAME +
                " WHERE " +  MedicineContract.MedicineEntry.COLUMN_MEDICINE_NAME + "  LIKE  '%" +search + "%' "
                ;

        Cursor cursor = SQLiteDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

}
