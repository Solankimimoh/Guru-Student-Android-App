package com.example.mentor;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMG_CODE = 100;
    private ImageView profileAvatarImg;
    private EditText profileNameTv;
    private TextView profileEmailTv;
    private EditText profileMobileTv;
    private TextView profileIndustryTv;
    private EditText profileExperienceTv;
    private EditText profileEducationTv;
    private EditText profileMentorTypeTv;
    private EditText profileMentorSkillTv;
    private Button profileFollwingBtn;
    private Button profileFollwerBtn;
    private Button profileBtn;
    private LovelyProgressDialog waitingDialog;

    private String loginType;
    private Intent intent;
    //    Firebase Init
    private DatabaseReference DataRef;
    private FirebaseAuth auth;
    private String profileData;
    private boolean profilePic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        DataRef = FirebaseDatabase.getInstance().getReference();
        waitingDialog = new LovelyProgressDialog(ProfileActivity.this);


        initView();

        DataRef.child(AppConstant.FIREBASE_TABLE_FOLLOWERS).removeValue();
        DataRef.child(AppConstant.FIREBASE_TABLE_MENTOR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot list : dataSnapshot.getChildren()) {
                    if (list.child(AppConstant.FIREBASE_TABLE_EMAIL).getValue().equals(auth.getCurrentUser().getEmail())) {

                    } else {

                        FollowingModel followingModel = new FollowingModel(list.child(AppConstant.FIREBASE_TABLE_FULLNAME).getValue().toString(), list.child(AppConstant.FIREBASE_INDUSTRY).getValue().toString(), false, false);

                        DataRef.child(AppConstant.FIREBASE_TABLE_FOLLOWERS).child(DataRef.push().getKey()).setValue(followingModel).addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        intent = getIntent();

        if (intent.hasExtra("KEY_LOGIN_TYPE")) {
            loginType = intent.getStringExtra("KEY_LOGIN_TYPE");

            if (loginType.equals(AppConstant.FIREBASE_TABLE_STUDNET)) {

                profileExperienceTv.setVisibility(View.GONE);
                profileEducationTv.setVisibility(View.GONE);
                profileMentorTypeTv.setVisibility(View.GONE);
                profileMentorSkillTv.setVisibility(View.GONE);

                DataRef.child(loginType)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                final String userName = dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_FULLNAME).getValue().toString();
                                final String userEmail = dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_EMAIL).getValue().toString();
//                                final String department = dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_DEPARTMENT).getValue().toString();
                                profileNameTv.setText(dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_FULLNAME).getValue().toString());
                                profileEmailTv.setText(dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_EMAIL).getValue().toString());
                                profileMobileTv.setText(dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_MOBILE).getValue().toString());
                                profileIndustryTv.setText(dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_INDUSTRY).getValue().toString());
                                Bitmap src;
                                String imgBase64 = dataSnapshot.child(auth.getCurrentUser().getUid()).child("avatar").getValue().toString();
                                byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
                                src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                profileAvatarImg.setImageDrawable(ImageUtils.roundedImage(ProfileActivity.this, src));
                                profileData = imgBase64;

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.e("TAG", "Failed to read value.", error.toException());
                            }
                        });

            } else if (loginType.equals(AppConstant.FIREBASE_TABLE_MENTOR)) {

//                profileFollwingBtn.setVisibility(View.INVISIBLE);
                profileFollwerBtn.setVisibility(View.INVISIBLE);

                DataRef.child(loginType)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                final String userName = dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_FULLNAME).getValue().toString();
                                final String userEmail = dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_EMAIL).getValue().toString();
//                                final String department = dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_DEPARTMENT).getValue().toString();
                                profileNameTv.setText(dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_FULLNAME).getValue().toString());
                                profileEmailTv.setText(dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_EMAIL).getValue().toString());
                                profileMobileTv.setText(dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_MOBILE).getValue().toString());
                                profileIndustryTv.setText(dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_INDUSTRY).getValue().toString());
                                Bitmap src;
                                String imgBase64 = dataSnapshot.child(auth.getCurrentUser().getUid()).child("avatar").getValue().toString();
                                byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
                                src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                profileAvatarImg.setImageDrawable(ImageUtils.roundedImage(ProfileActivity.this, src));
                                profileData = imgBase64;
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.e("TAG", "Failed to read value.", error.toException());
                            }
                        });

                DataRef.child(AppConstant.FIREBASE_TABLE_MENTOR_DETAILS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        profileMentorSkillTv.setText(dataSnapshot.child(auth.getCurrentUser().getUid()).child("skill").getValue().toString());
                        profileExperienceTv.setText(dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_EXPERIENCE).getValue().toString());
                        profileEducationTv.setText(dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_EDUCATION).getValue().toString());
                        profileMentorTypeTv.setText(dataSnapshot.child(auth.getCurrentUser().getUid()).child(AppConstant.FIREBASE_TABLE_MENTOR_TYPE).getValue().toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }

    }

    private void initView() {

        profileAvatarImg = findViewById(R.id.activity_profile_user_avatar_img);
        profileNameTv = findViewById(R.id.activity_profile_name);
        profileEmailTv = findViewById(R.id.activity_profile_email);
        profileMobileTv = findViewById(R.id.activity_profile_mobile);
        profileIndustryTv = findViewById(R.id.activity_profile_industry);
        profileExperienceTv = findViewById(R.id.activity_profile_experience);
        profileEducationTv = findViewById(R.id.activity_profile_education);
        profileMentorTypeTv = findViewById(R.id.activity_profile_mentor_type);
        profileMentorSkillTv = findViewById(R.id.activity_profile_mentor_skill_tv);
        profileBtn = findViewById(R.id.activity_profile_updatebtn);

        profileAvatarImg.setOnClickListener(this);

//        profileFollwingBtn = findViewById(R.id.activity_profile_following_btn);
        profileFollwerBtn = findViewById(R.id.activity_profile_follower_btn);

        profileBtn.setOnClickListener(this);
//        profileFollwingBtn.setOnClickListener(this);
        profileFollwerBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.activity_profile_following_btn:
//                final Intent gotoFOllowIntent = new Intent(ProfileActivity.this, FollowingFollowerActivity.class);
//                startActivity(gotoFOllowIntent);
//                break;
            case R.id.activity_profile_updatebtn:
                updateProfile();
                break;
            case R.id.activity_profile_user_avatar_img:


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return;
                } //creating an intent for file chooser
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMG_CODE);

                break;
            case R.id.activity_profile_follower_btn:
                final Intent gotoFollowerIntent = new Intent(ProfileActivity.this, FollowingFollowerActivity.class);
                startActivity(gotoFollowerIntent);
                break;
        }
    }

    private void updateProfile() {

        if (loginType.equals(AppConstant.FIREBASE_TABLE_STUDNET)) {

            String name = profileNameTv.getText().toString().trim();
            String email = profileEmailTv.getText().toString();
            String mobile = profileMobileTv.getText().toString().trim();
            String industry = profileIndustryTv.getText().toString();
            DataRef.child(AppConstant.FIREBASE_TABLE_STUDNET)
                    .child(auth.getCurrentUser().getUid())
                    .child("fullname")
                    .setValue(name);
            DataRef.child(AppConstant.FIREBASE_TABLE_STUDNET)
                    .child(auth.getCurrentUser().getUid())
                    .child("mobile")
                    .setValue(mobile);
            DataRef.child(AppConstant.FIREBASE_TABLE_STUDNET)
                    .child(auth.getCurrentUser().getUid())
                    .child("avatar")
                    .setValue(profileData);

        } else if (loginType.equals(AppConstant.FIREBASE_TABLE_MENTOR)) {

            String name = profileNameTv.getText().toString().trim();
            String mobile = profileMobileTv.getText().toString().trim();
            String exep = profileExperienceTv.getText().toString().trim();
            String educatuion = profileEducationTv.getText().toString().trim();
            String mentorskill = profileMentorSkillTv.getText().toString().trim();
            String mentortype = profileMentorTypeTv.getText().toString().trim();

            DataRef.child(AppConstant.FIREBASE_TABLE_MENTOR)
                    .child(auth.getCurrentUser().getUid())
                    .child("fullname")
                    .setValue(name);
            DataRef.child(AppConstant.FIREBASE_TABLE_MENTOR)
                    .child(auth.getCurrentUser().getUid())
                    .child("mobile")
                    .setValue(mobile);
            DataRef.child(AppConstant.FIREBASE_TABLE_MENTOR)
                    .child(auth.getCurrentUser().getUid())
                    .child("avatar")
                    .setValue(profileData);
            DataRef.child(AppConstant.FIREBASE_TABLE_MENTOR_DETAILS)
                    .child(auth.getCurrentUser().getUid())
                    .child("experience")
                    .setValue(exep);
            DataRef.child(AppConstant.FIREBASE_TABLE_MENTOR_DETAILS)
                    .child(auth.getCurrentUser().getUid())
                    .child("mentorType")
                    .setValue(mentortype);
            DataRef.child(AppConstant.FIREBASE_TABLE_MENTOR_DETAILS)
                    .child(auth.getCurrentUser().getUid())
                    .child("qualification")
                    .setValue(educatuion);
            DataRef.child(AppConstant.FIREBASE_TABLE_MENTOR_DETAILS)
                    .child(auth.getCurrentUser().getUid())
                    .child("skill")
                    .setValue(mentorskill);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(ProfileActivity.this, "Not Choosed ", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());

                Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                imgBitmap = ImageUtils.cropToSquare(imgBitmap);
                InputStream is = ImageUtils.convertBitmapToInputStream(imgBitmap);
                final Bitmap liteImage = ImageUtils.makeImageLite(is,
                        imgBitmap.getWidth(), imgBitmap.getHeight(),
                        ImageUtils.AVATAR_WIDTH, ImageUtils.AVATAR_HEIGHT);

                String imageBase64 = ImageUtils.encodeBase64(liteImage);
                profileData = imageBase64;

                waitingDialog.setCancelable(false)
                        .setTitle("Avatar updating....")
                        .setTopColorRes(R.color.colorPrimary)
                        .show();

//                SharedPreferenceHelper preferenceHelper = SharedPreferenceHelper.getInstance(context);
//                preferenceHelper.saveUserInfo(myAccount);
                profileAvatarImg.setImageDrawable(ImageUtils.roundedImage(ProfileActivity.this, liteImage));
                waitingDialog.dismiss();
                new LovelyInfoDialog(ProfileActivity.this)
                        .setTopColorRes(R.color.colorPrimary)
                        .setTitle("Success")
                        .setMessage("Update avatar successfully!")
                        .show();
                profilePic = true;


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}
