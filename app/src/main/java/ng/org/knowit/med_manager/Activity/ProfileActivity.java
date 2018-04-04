package ng.org.knowit.med_manager.Activity;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import ng.org.knowit.med_manager.R;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private EditText profileNameEditText, profileEmailEditText, profileQuotesEditText, profilePhoneEditText;

    private TextView profileNameTextView, profileEmailTextView, profileQuoteTextView, profilePhoneTextView;

    private String profileName, profileEmail, profilePhone, profileQuotes;

    private static final String TAG = "Profile Activity";

    private static final int RC_PHOTO_PICKER = 2;

    private ImageView profileImageView;

    private Uri photoUrl, mPhotoUrl;

    private FirebaseUser mFirebaseUser;

    private UserProfileChangeRequest profileNameUpdate, profileImageUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
    }
    public void initializeViews(){
        profileEmailEditText = findViewById(R.id.input_profile_email);
        profileNameEditText = findViewById(R.id.input_profile_name);
        profileQuotesEditText = findViewById(R.id.input_profile_quotes);
        profilePhoneEditText = findViewById(R.id.input_profile_phone);
        profileNameTextView = findViewById(R.id.profile_name_text_view);
        profileEmailTextView = findViewById(R.id.profile_email_text_view);
        profilePhoneTextView = findViewById(R.id.profile_phone_text_view);
        profileQuoteTextView = findViewById(R.id.profile_quotes_text_view);
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

        final EditText ProfileEmail = dialogView.findViewById(R.id.input_profile_email);
        profileEmailEditText = ProfileEmail;

        final EditText ProfilePhone  = dialogView.findViewById(R.id.input_profile_phone);
        profilePhoneEditText = ProfilePhone;

        final EditText ProfileQuotes = dialogView.findViewById(R.id.input_profile_quotes);
        profileQuotesEditText = ProfileQuotes;


        dialogBuilder.setTitle("Edit Profile");
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
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
                                    Log.d(TAG, "User Name  updated. ");
                                }
                            }
                        });
                profileNameTextView.setText(profileName);
            }

          String  ProfileEmail = profileEmailEditText.getText().toString();
        if (profileEmail!=null && !ProfileEmail.trim().isEmpty()){
            profileEmail = ProfileEmail;

            /*mFirebaseUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "Email sent. ");
                        }
                    });*/


            mFirebaseUser.updateEmail(profileEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "User Email address updated");
                            }
                        }
                    });

            profileEmailTextView.setText(profileEmail);
        }

        String  ProfilePhone = profilePhoneEditText.getText().toString();
        if (profilePhone==null && !ProfilePhone.trim().isEmpty()){
            profilePhone = ProfilePhone;
            profilePhoneTextView.setText(profilePhone);
        }

        String ProfileQuotes = profileQuotesEditText.getText().toString();
        if (profileQuotes==null && !ProfilePhone.trim().isEmpty()){
            profileQuotes = ProfileQuotes;
            profileQuoteTextView.setText(profileQuotes);

        }

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
                                Log.d(TAG, "User Image  updated. ");
                            }
                        }
                    });

            Glide.with(this).load(mPhotoUrl).into(profileImageView);
        }

    }
}
