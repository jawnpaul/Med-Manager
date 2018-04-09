package ng.org.knowit.med_manager.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ng.org.knowit.med_manager.Adapters.MedicineDatabaseAdapter;
import ng.org.knowit.med_manager.Alarm.AlarmReceiver;
import ng.org.knowit.med_manager.Data.MedicineContract;
import ng.org.knowit.med_manager.Data.MedicineDbHelper;
import ng.org.knowit.med_manager.R;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;
    private Spinner frequencySpinner;

    private MedicineDatabaseAdapter medicineDatabaseAdapter;

    private static final int RC_SIGN_IN = 123;

    private int spinnerPosition;


    private EditText startDateEditText, endDateEditText, medicineNameEditText, medicineDescriptionEditText;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener startDateDialog;
    private DatePickerDialog.OnDateSetListener endDateDialog;
    private Date startDate, endDate, currentDate;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private boolean isConnected;


    private static final int ONE_HOUR_IN_MILLI_SECONDS =3600000;
    private static final int TWO_HOURS_IN_MILLI_SECONDS = 7200000;
    private static final int THREE_HOURS_IN_MILLI_SECONDS = 10800000;
    private static final int SIX_HOURS_IN_MILLI_SECONDS = 21600000;
    private static final int EIGHT_HOURS_IN_MILLI_SECONDS = 28800000;
    private static final int TWELVE_HOURS_IN_MILLI_SECONDS = 43200000;
    private static final int TWENTY_FOUR_HOURS_IN_MILLI_SECONDS = 86400000;

    private SQLiteDatabase mSQLiteDatabase;
    private String medicineFrequency, medicineName, medicineDescription,
            medicineDuration, medicineStartDate,
            medicineEndDate, profleName, profileEmail, profilePhoneNumber;
    private Uri profilePhotoUrl;

    private RecyclerView mRecyclerView;

    private View emptyStateView;

    private TextView profileNameTextView, emptyViewText;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initialize all the views
        initializeViews();

        emptyStateView.setVisibility(View.GONE);

        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork!=null &&
                activeNetwork.isConnectedOrConnecting();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(isConnected && user!= null) {
            emptyStateView.setVisibility(View.GONE);

            mFloatingActionButton.setVisibility(View.VISIBLE);
        }

        else if(!isConnected && user==null) {
            emptyStateView.setVisibility(View.VISIBLE);

            mFloatingActionButton.setVisibility(View.GONE);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();


        mRecyclerView = this.findViewById(R.id.recycler_view_medicine);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        MedicineDbHelper dbHelper = new MedicineDbHelper(this);
        mSQLiteDatabase = dbHelper.getWritableDatabase();


        Cursor cursor = getAllMedicine();


        medicineDatabaseAdapter = new MedicineDatabaseAdapter(this, cursor);

        mRecyclerView.setAdapter(medicineDatabaseAdapter);


        setSupportActionBar(mToolbar);
        //noinspection deprecation
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu);


        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        }


        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewMedicineDialog();
            }
        });



        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.profile:
                                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(intent);
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

       myCalendar  = Calendar.getInstance();

        currentDate = myCalendar.getTime();

        startDateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDate = myCalendar.getTime();
                updateLabelForStartDate();
            }
        };

        endDateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDate = myCalendar.getTime();
                updateLabelForEndDate();
            }

        };

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    //User is signed in
                    profleName = user.getDisplayName();
                    profileEmail = user.getEmail();
                    profilePhoneNumber = user.getPhoneNumber();
                    profilePhotoUrl = user.getPhotoUrl();
                    updateProfile(user.getDisplayName(), user.getPhotoUrl());
                }else {
                    //User is signed out
                    // Create and launch sign-in intent

                    // Choose authentication providers
                    @SuppressWarnings("deprecation") List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(true)
                                    .setAvailableProviders(providers)
                                    .setLogo(R.mipmap.ic_launcher)
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void addToMedicineList(){
        //Setting the values gotten from the dialog to corresponding global variables
        medicineFrequency = String.valueOf(frequencySpinner.getSelectedItem());
        medicineName = medicineNameEditText.getText().toString();
        medicineDescription = medicineDescriptionEditText.getText().toString();
        medicineStartDate = startDateEditText.getText().toString();
        medicineEndDate = endDateEditText.getText().toString();
        spinnerPosition = frequencySpinner.getSelectedItemPosition();

        //Checking to make sure the values are not empty
        if (medicineName.length()==0 || medicineDescription.length()==0 ||
                medicineFrequency.length()==0 || medicineStartDate.length()==0 || medicineEndDate.length()==0){
            Toast.makeText(MainActivity.this, "Input Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checking to make sure Start date is before end date
        if(startDate.after(endDate)){
            Toast.makeText(this, "Start date cannot be after end date", Toast.LENGTH_SHORT).show();
            return;
        }
        //Concatenating the two date Strings to form a single String
        medicineDuration = medicineStartDate + " - " + medicineEndDate;
        //Log.d("Lol", medicineName+medicineDescription+ medicineFrequency +medicineStartDate + medicineEndDate + " " + medicineDuration);
        addNewMedicine(medicineName, medicineDescription, medicineFrequency, medicineDuration);
        Log.d("Lol" ,"Successfully created");

        medicineDatabaseAdapter.swapCursor(getAllMedicine());

        mRecyclerView.scrollToPosition(medicineDatabaseAdapter.getItemCount() - 1);

    }

    private Cursor getAllMedicine() {
        return mSQLiteDatabase.query(MedicineContract.MedicineEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MedicineContract.MedicineEntry.COLUMN_TIMESTAMP);

    }

    private long addNewMedicine(String medicineName, String medicineDescription, String medicineFrequency, String medicineDuration){
        ContentValues cv = new ContentValues();
        cv.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_NAME, medicineName);
        cv.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_DESCRIPTION, medicineDescription);
        cv.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_FREQUENCY, medicineFrequency);
        cv.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_DURATION, medicineDuration);
        return mSQLiteDatabase.insert(MedicineContract.MedicineEntry.TABLE_NAME,null,cv);
    }

    public void pickStartDate(View view){
        new DatePickerDialog(MainActivity.this, startDateDialog, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void pickEndDateDate(View view){
        new DatePickerDialog(MainActivity.this, endDateDialog, myCalendar
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

    private void initializeViews(){
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mFloatingActionButton = findViewById(R.id.fab);
        startDateEditText = findViewById(R.id.edit_text_start_date);
        endDateEditText = findViewById(R.id.edit_text_end_date);
        medicineDescriptionEditText = findViewById(R.id.input_medicine_description);
        medicineNameEditText = findViewById(R.id.input_medicine_name);
        profileImage = mNavigationView.getHeaderView(0).findViewById(R.id.image_profile);
        profileNameTextView = mNavigationView.getHeaderView(0).findViewById(R.id.text_profile_name);
        emptyStateView = findViewById(R.id.empty_view);
        emptyViewText = findViewById(R.id.empty_view_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
       searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_sign_out :
                AuthUI.getInstance()
                        .signOut(this);
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

                if(!isConnected){
                    emptyStateView.setVisibility(View.VISIBLE);
                    mFloatingActionButton.setVisibility(View.GONE);
                    finish();

                }

                else if(isConnected){
                    Toast.makeText(this, "Sign in cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }
    }

    private void createNewMedicineDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_medicine_dialog, null);
        dialogBuilder.setView(dialogView);

        frequencySpinner = dialogView.findViewById(R.id.spinner_frequency);


        final EditText MedicineNameEditText = dialogView.findViewById(R.id.input_medicine_name);
        medicineNameEditText = MedicineNameEditText;


        final EditText MedicineDescriptionEditText = dialogView.findViewById(R.id.input_medicine_description);
        medicineDescriptionEditText = MedicineDescriptionEditText;


        final EditText StartDateEditText = dialogView.findViewById(R.id.edit_text_start_date);
        startDateEditText = StartDateEditText;

        final EditText EndDateEditText = dialogView.findViewById(R.id.edit_text_end_date);
        endDateEditText = EndDateEditText;

        dialogBuilder.setTitle("Add new medicine");
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                addToMedicineList();
                createAlarm();

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void updateProfile(String profileName, Uri photoUrl){
        profileNameTextView.setText(profileName);
        if(photoUrl==null){
            Glide.with(getApplicationContext()).load(R.drawable.ic_action_account).into(profileImage);
        } else
            Glide.with(getApplicationContext()).load(photoUrl).into(profileImage);
    }

    private boolean removeMedicine (long id){
        return mSQLiteDatabase.delete(
                MedicineContract.MedicineEntry.TABLE_NAME, MedicineContract.MedicineEntry._ID + "=" + id, null) >0;
    }

    public void triggerAlarmManager(long alarmInterval, int REQUEST_CODE) {

        //This gives the start date in milliseconds
        long date =  startDate.getTime();

        //This is to make sure that it doesn't send notification immediately after creation when the start date is the current time
        if(date == startDate.getTime()){
            date = date + alarmInterval;
        }

        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
      PendingIntent  pendingIntent = PendingIntent.getBroadcast(MainActivity.this, REQUEST_CODE, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, date, alarmInterval, pendingIntent);//set alarm manager with entered timer by converting into milliseconds

        //Toast.makeText(this, "Alarm Set for " + date + " seconds.", Toast.LENGTH_SHORT).show();
    }

    private void createAlarm(){

        if (currentDate.before(endDate) || currentDate.equals(endDate)) {
            switch (spinnerPosition) {
                case 0:
                    triggerAlarmManager(ONE_HOUR_IN_MILLI_SECONDS, 100);
                    break;
                case 1:
                    triggerAlarmManager(TWO_HOURS_IN_MILLI_SECONDS, 200);
                    break;
                case 2:
                    triggerAlarmManager(THREE_HOURS_IN_MILLI_SECONDS, 300);
                    break;
                case 3:
                    triggerAlarmManager(SIX_HOURS_IN_MILLI_SECONDS, 400);
                    break;
                case 4:
                    triggerAlarmManager(EIGHT_HOURS_IN_MILLI_SECONDS, 500);
                    break;
                case 5:
                    triggerAlarmManager(TWELVE_HOURS_IN_MILLI_SECONDS, 600);
                    break;
                case 6:
                    triggerAlarmManager(TWENTY_FOUR_HOURS_IN_MILLI_SECONDS, 700);
                    break;
                default:
                    break;
            }
        } else if (currentDate.after(endDate)){
            return;
        }
    }


}
