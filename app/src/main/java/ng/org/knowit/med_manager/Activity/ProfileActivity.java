package ng.org.knowit.med_manager.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ng.org.knowit.med_manager.R;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private EditText profileName, profileEmail, profileQuotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeViews();


        mToolbar = findViewById(R.id.toolbar_profile_activity);
        setSupportActionBar(mToolbar);
        //noinspection deprecation
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);
    }
    public void initializeViews(){
        profileEmail = findViewById(R.id.input_profile_email);
        profileName = findViewById(R.id.input_profile_name);
        profileQuotes = findViewById(R.id.input_profile_quotes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_profile_activity:
                editProfile();

        }
        return super.onOptionsItemSelected(item);
    }
    private void editProfile() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_profile_dialog, null);
        dialogBuilder.setView(dialogView);


        final EditText ProfileName = dialogView.findViewById(R.id.input_profile_name);
        profileName = ProfileName;

        final EditText ProfileEmail = dialogView.findViewById(R.id.input_profile_email);
        profileEmail = ProfileEmail;

        final EditText ProfileQuotes = dialogView.findViewById(R.id.input_profile_quotes);
        profileQuotes = ProfileQuotes;


        dialogBuilder.setTitle("Edit Profile");
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.show();
    }
}
