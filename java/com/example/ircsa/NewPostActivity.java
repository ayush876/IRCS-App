package com.example.ircsa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class NewPostActivity extends AppCompatActivity {


    private Button setupbtn;
    private EditText setupname;
    private ImageView setupImage;

    private Uri postImageURI = null;

    private ProgressBar newPostProgress;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String user_id;
    private Bitmap compressedImageFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        setupImage = findViewById(R.id.new_post_image);
        setupname = findViewById(R.id.new_post_text);
        setupbtn = findViewById(R.id.post_btn);
        newPostProgress = findViewById(R.id.new_post_progress);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        user_id = firebaseAuth.getCurrentUser().getUid();

        setupImage.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(NewPostActivity.this);
            }


        });

//        setupbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final String desc = setupname.getText().toString();
//
//                if(!TextUtils.isEmpty(desc) && postImageURI != null){
//                    newPostProgress.setVisibility(View.VISIBLE);
//
//                    final String randomName = FieldValue.serverTimestamp().toString();
//
//                    StorageReference filepath = storageReference.child("post_images").child(randomName + ".jpg");
//                    filepath.putFile(postImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                            if(task.isSuccessful()){
//
//                                String downloadUri = task.getResult().getStorage().getDownloadUrl().toString();
//
//                                Map<String,String> postMap = new HashMap<>();
//                                postMap.put("image_url",downloadUri);
//                                postMap.put("desc", desc);
//                                postMap.put("timestamp", randomName);
//
//                                firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentReference> task) {
//
//                                        if(task.isSuccessful()){
//
//                                            Toast.makeText(NewPostActivity.this,"The post was Uploaded", Toast.LENGTH_LONG).show();
//                                            Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
//                                            startActivity(mainIntent);
//                                            finish();
//
//                                        } else{
//
//                                        }
//
//                                        newPostProgress.setVisibility(View.INVISIBLE);
//
//                                    }
//                                });
//
//                            } else{
//
//                                newPostProgress.setVisibility(View.INVISIBLE);
//
//                            }
//
//                        }
//                    });
//                }
//
//            }
//        });

        setupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desc = setupname.getText().toString();

                if(!TextUtils.isEmpty(desc) && postImageURI != null){

                    newPostProgress.setVisibility(View.VISIBLE);

                    final String randomName = UUID.randomUUID().toString();

                    // PHOTO UPLOAD

                    File newImageFile = new File(postImageURI.getPath());
                    try{

                        compressedImageFile = new Compressor(NewPostActivity.this)
                                .setMaxHeight(720)
                                .setMaxWidth(720)
                                .setQuality(50)
                                .compressToBitmap(newImageFile);
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] imageData = baos.toByteArray();


                    UploadTask image_path = storageReference.child("feed_images").child(randomName + ".jpg").putBytes(imageData);
                    image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            final String downloadUri = task.getResult().getStorage().getDownloadUrl().toString();

                            if(task.isSuccessful()){

                                File newThumbFile = new File(postImageURI.getPath());

                                try {

                                    compressedImageFile = new Compressor(NewPostActivity.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(1)
                                            .compressToBitmap(newThumbFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG,100,baos);
                                byte[] thumbData = baos.toByteArray();

                                UploadTask uploadTask = storageReference.child("feed_images/thumbs")
                                        .child(randomName + ".jpg").putBytes(thumbData);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        String downloadthumbUri = taskSnapshot.getStorage().getDownloadUrl().toString();

                                        Map<String, Object> postMap = new HashMap<>();
                                        postMap.put("image_url", downloadUri);
                                        postMap.put("image_thumb", downloadthumbUri);
                                        postMap.put("desc", desc);
                                        postMap.put("user_id", user_id);
                                        postMap.put("timestamp", FieldValue.serverTimestamp());

                                        firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                if(task.isSuccessful()){

                                                    Toast.makeText(NewPostActivity.this, "Post was added", Toast.LENGTH_LONG).show();
                                                    Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
                                                    startActivity(mainIntent);
                                                    finish();

                                                } else {


                                                }

                                                newPostProgress.setVisibility(View.INVISIBLE);

                                            }
                                        });




                           /*             DatabaseReference myRef = database.getReference("feed_post").push();

                                        myRef.child("image_url").setValue(downloadUri);
                                        myRef.child("image_thumb").setValue(downloadthumbUri);
                                        myRef.child("desc").setValue(desc);
                                        myRef.child("user_id").setValue(user_id);

                                        Toast.makeText(NewPostActivity.this,"The post was added",Toast.LENGTH_LONG).show();
                                        Intent mainIntent  = new Intent(NewPostActivity.this, MainActivity.class);
                                        startActivity(mainIntent);
                                        finish();*/

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        //Error Handling

                                    }
                                });



                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(NewPostActivity.this,error,Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(NewPostActivity.this,"Please fill both the entries",Toast.LENGTH_LONG).show();
                }
            }
        });


   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postImageURI = result.getUri();
                setupImage.setImageURI(postImageURI);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }

}
