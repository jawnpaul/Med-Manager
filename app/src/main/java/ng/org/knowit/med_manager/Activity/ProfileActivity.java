package ng.org.knowit.med_manager.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import ng.org.knowit.med_manager.R;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private EditText profileNameEditText, profileQuotesEditText,
            profilePhoneEditText, profileSecondaryEmailEditText;

    private TextView profileNameTextView, profileEmailTextView,
            profileQuoteTextView, profilePhoneTextView, profileSecondaryEmailTextView;

    private String profileName, profileEmail, profilePhone, profileQuotes, profileSecondaryEmail;

    private static final String TAG = "Profile Activity";

    private static final int RC_PHOTO_PICKER = 2;

    private static final String MY_PREFERENCES = "my_preferences";
    private static final String EMAIL = "Email_key";
    private static final String PHONE = "Phone_key";
    private static final String QUOTES = "Quotes_key";

    private ImageView profileImageView;

    private Uri photoUrl, mPhotoUrl;

    private FirebaseUser mFirebaseUser;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private UserProfileChangeRequest profileNameUpdate, profileImageUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);


        profilePhone = "";
        profileQuotes = "";
        profileSecondaryEmail = "";

        initializeViews();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        profileName = mFirebaseUser.getDisplayName();
        profileNameTextView.setText(profileName);

        profileEmail = mFirebaseUser.getEmail();
        profileEmailTextView.setText(profileEmail);

        photoUrl = mFirebaseUser.getPhotoUrl();
        Glide.with(this).load(photoUrl).into(profileImageView);

        mToolbar = findViewById(R.id.toolbar_profile_activity);
        setSupportActionBar(mToolbar);
        //noinspection deprecation
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        mSharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        if(mSharedPreferences.contains(EMAIL)){
            profileSecondaryEmailTextView.setText(mSharedPreferences.getString(EMAIL, ""));
        }

        if(mSharedPreferences.contains(PHONE)){
            profilePhoneTextView.setText(mSharedPreferences.getString(PHONE, ""));
        }

        if(mSharedPreferences.contains(QUOTES)){
            profileQuoteTextView.setText(mSharedPreferences.getString(QUOTES, ""));
        }

    }


    public void initializeViews(){
        profileNameEditText = findViewById(R.id.input_profile_name);
        profileQuotesEditText = findViewById(R.id.input_profile_quotes);
        profilePhoneEditText = findViewById(R.id.input_profile_phone);
        profileNameTextView = findViewById(R.id.profile_name_text_view);
        profileEmailTextView = findViewById(R.id.profile_email_text_view);
        profilePhoneTextView = findViewById(R.id.profile_phone_text_view);
        profileQuoteTextView = findViewById(R.id.profile_quotes_text_view);
        profileSecondaryEmailEditText = findViewById(R.id.input_profile_secondary_email);
        profileSecondaryEmailTextView = findViewById(R.id.profile_secondary_email_text_view);
        profileImageView = findViewById(R.id.profile_image);
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
                break;
            case R.id.menu_change_profile_picture:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "complete action using"), RC_PHOTO_PICKER);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void editProfile() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_profile_dialog, null);
        dialogBuilder.setView(dialogView);


        final EditText ProfileName = dialogView.findViewById(R.id.input_profile_name);
        profileNameEditText = ProfileName;


        final EditText ProfilePhone  = dialogView.findViewById(R.id.input_profile_phone);
        profilePhoneEditText = ProfilePhone;

        final EditText ProfileQuotes = dialogView.findViewById(R.id.input_profile_quotes);
        profileQuotesEditText = ProfileQuotes;

        final EditText ProfileSecondaryEmail = dialogView.findViewById(R.id.input_profile_secondary_email);
        profileSecondaryEmailEditText = ProfileSecondaryEmail;


        dialogBuilder.setTitle("Edit Profile");
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateProfile();
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

    private void updateProfile(){


            String ProfileName = profileNameEditText.getText().toString();
            if(profileName!=null && !ProfileName.trim().isEmpty()){
                profileName = ProfileName;
                profileNameUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(profileName)
                        .build();
                mFirebaseUser.updateProfile(profileNameUpdate).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    profileNameTextView.setText(profileName);
                                    Log.d(TAG, "User Name  updated. ");
                                } else if(!task.isSuccessful()){
                                    Toast.makeText(ProfileActivity.this, "Profile name not updated because of no internet connection",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }

        String  ProfilePhone = profilePhoneEditText.getText().toString();
        if (profilePhone!=null && !ProfilePhone.trim().isEmpty()){
            profilePhone = ProfilePhone;
            mEditor = mSharedPreferences.edit();
            mEditor.putString(PHONE, profilePhone);
            mEditor.apply();
            profilePhoneTextView.setText(profilePhone);

        }

        String ProfileQuotes = profileQuotesEditText.getText().toString();
        if (profileQuotes!=null && !ProfilePhone.trim().isEmpty()){
            profileQuotes = ProfileQuotes;
            mEditor = mSharedPreferences.edit();
            mEditor.putString(QUOTES, profileQuotes);
            mEditor.apply();
            profileQuoteTextView.setText(profileQuotes);

        }

        String ProfileSecondaryEmail = profileSecondaryEmailEditText.getText().toString();
        if (profileSecondaryEmail!=null && !ProfileSecondaryEmail.trim().isEmpty()){
            profileSecondaryEmail = ProfileSecondaryEmail;
            mEditor = mSharedPreferences.edit();
            mEditor.putString(EMAIL, profileSecondaryEmail);
            mEditor.apply();
            profileSecondaryEmailTextView.setText(profileSecondaryEmail);
        }

        Log.d(TAG, "Successfully updated");





    }



    public void chooseImage(View view){
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "complete action using"), RC_PHOTO_PICKER);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            mPhotoUrl = data.getData();

            profileImageUpdate = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(mPhotoUrl)
                    .build();
            mFirebaseUser.updateProfile(profileImageUpdate).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Glide.with(ProfileActivity.this).load(mPhotoUrl).into(profileImageView);
                                Log.d(TAG, "User Image  updated. ");
                            } else if(!task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this, "No internet connection",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }
}
