package ng.org.knowit.med_manager.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ng.org.knowit.med_manager.Adapters.MedicineDatabaseAdapter;
import ng.org.knowit.med_manager.Data.MedicineContract;
import ng.org.knowit.med_manager.Data.MedicineDbHelper;
import ng.org.knowit.med_manager.Data.TestUtil;
import ng.org.knowit.med_manager.R;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;

    private static final int RC_SIGN_IN = 123;

    private EditText startDateEditText, endDateEditText;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener startDate;
    private DatePickerDialog.OnDateSetListener endDate;


    private String[] frequency;

    private SQLiteDatabase mSQLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView;
        mRecyclerView = this.findViewById(R.id.recycler_view_medicine);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        MedicineDbHelper dbHelper = new MedicineDbHelper(this);
        mSQLiteDatabase = dbHelper.getWritableDatabase();

        TestUtil.insertFakeData(mSQLiteDatabase);
        Cursor cursor = getAllData();


        MedicineDatabaseAdapter medicineDatabaseAdapter = new MedicineDatabaseAdapter(this, cursor);

        mRecyclerView.setAdapter(medicineDatabaseAdapter);


        //Initialize all the views
        initializeViews();


        setSupportActionBar(mToolbar);
        //noinspection deprecation
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu);


        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLangDialog();
            }
        });

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        // Choose authentication providers
        @SuppressWarnings("deprecation") List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);


       myCalendar  = Calendar.getInstance();
        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelForStartDate();
            }
        };

        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelForEndDate();
            }

        };


    }
    public void pickStartDate(View view){
        new DatePickerDialog(MainActivity.this, startDate, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void pickEndDateDate(View view){
        new DatePickerDialog(MainActivity.this, endDate, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabelForStartDate() {
        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDateEditText.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelForEndDate() {
        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDateEditText.setText(sdf.format(myCalendar.getTime()));
    }


    private Cursor getAllData() {
        return mSQLiteDatabase.query(MedicineContract.MedicineEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MedicineContract.MedicineEntry.COLUMN_TIMESTAMP);

    }

    private void initializeViews(){
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mFloatingActionButton = findViewById(R.id.fab);
        startDateEditText = findViewById(R.id.edit_text_start_date);
        endDateEditText = findViewById(R.id.edit_text_end_date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            @SuppressWarnings("UnusedAssignment") IdpResponse response =
                    IdpResponse.fromResultIntent(data);

            //noinspection StatementWithEmptyBody
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                @SuppressWarnings("UnusedAssignment") FirebaseUser user =
                        FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }

    private void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_medicine_dialog, null);
        dialogBuilder.setView(dialogView);


        final EditText StartDateEditText = dialogView.findViewById(R.id.edit_text_start_date);
        startDateEditText = StartDateEditText;

        final EditText EndDateEditText = dialogView.findViewById(R.id.edit_text_end_date);
        endDateEditText = EndDateEditText;

        dialogBuilder.setTitle("Add new medicine");
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               // edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
