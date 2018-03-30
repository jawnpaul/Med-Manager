package ng.org.knowit.med_manager.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

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

    private MedicineDatabaseAdapter mMedicineDatabaseAdapter;
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



        mMedicineDatabaseAdapter = new MedicineDatabaseAdapter(this, cursor);

        mRecyclerView.setAdapter(mMedicineDatabaseAdapter);


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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

}
