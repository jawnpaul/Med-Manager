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

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ng.org.knowit.med_manager.Data.MedicineContract;
import ng.org.knowit.med_manager.Data.MedicineDbHelper;
import ng.org.knowit.med_manager.R;



public class MedicineDetailActivity extends AppCompatActivity {

    private TextView medicineNameTextView, medicineDescriptionTextView,
            medicineFrequencyTextView, medicineDurationTextView, medicineDateCreatedTextView;

    private long id;

    private SQLiteDatabase mSQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);

        initializeViews();

        MedicineDbHelper dbHelper = new MedicineDbHelper(this);
        mSQLiteDatabase = dbHelper.getWritableDatabase();

        Toolbar toolbar = findViewById(R.id.toolbar_medicine_detail_activity);
        setSupportActionBar(toolbar);
        //noinspection deprecation
        toolbar.setTitle("Medicine");
        //noinspection deprecation
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //noinspection deprecation
        toolbar.setBackgroundColor(getResources().getColor(R.color.blue));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        Intent intent = getIntent();

        String medicineName = intent.getStringExtra("medicineName");
        medicineNameTextView.setText(medicineName);

        String medicineDescription = intent.getStringExtra("medicineDescription");
        medicineDescriptionTextView.setText(medicineDescription);

        String medicineDuration = intent.getStringExtra("medicineDuration");
        medicineDurationTextView.setText(medicineDuration);

        String medicineFrequency = intent.getStringExtra("medicineFrequency");
        medicineFrequencyTextView.setText(medicineFrequency);

        String medicineTimeStamp = intent.getStringExtra("medicineTimeStamp");
        medicineDateCreatedTextView.setText(medicineTimeStamp);

        int imageId = intent.getIntExtra("imageId", R.drawable.image_drug);

        id = intent.getLongExtra("id",1);

        ImageView medicineImage = findViewById(R.id.medicine_detail_image);
        Glide.with(this).load(imageId).into(medicineImage);
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean removeMedicine (long id){
        return mSQLiteDatabase.delete(
                MedicineContract.MedicineEntry.TABLE_NAME, MedicineContract.MedicineEntry._ID + "=" + id, null) >0;
    }

    private void initializeViews(){
        medicineNameTextView = findViewById(R.id.medicine_detail_name);
        medicineDescriptionTextView = findViewById(R.id.medicine_detail_description);
        medicineFrequencyTextView = findViewById(R.id.medicine_detail_frequency);
        medicineDurationTextView = findViewById(R.id.medicine_detail_interval);
        medicineDateCreatedTextView = findViewById(R.id.medicine_detail_date_created);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_medicine_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.medicine_detail_delete:
                removeMedicine(id);
                return true;
        }
            finish();
        return super.onOptionsItemSelected(item);
    }
}
