package ng.org.knowit.med_manager.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import ng.org.knowit.med_manager.R;



public class MedicineDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private String medicineName, medicineDescription, medicineFrequency,
            medicineDuration, medicineTimeStamp;

    private long id;

    private int imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);

        mToolbar = findViewById(R.id.toolbar_medicine_detail_activity);
        setSupportActionBar(mToolbar);
        //noinspection deprecation
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        Intent intent = getIntent();

        medicineName = intent.getStringExtra("medicineName");
        medicineDescription = intent.getStringExtra("medicineDescription");
        medicineDuration = intent.getStringExtra("medicineDuration");
        medicineFrequency = intent.getStringExtra("medicineFrequency");
        medicineTimeStamp = intent.getStringExtra("medicineTimeStamp");
        imageId = intent.getIntExtra("imageId", R.drawable.image_drug);
        id = intent.getLongExtra("id",1);

        ImageView medicineImage = (ImageView) findViewById(R.id.medicine_detail_image);
        Glide.with(this).load(imageId).into(medicineImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_medicine_detail, menu);
        return true;
    }
}
