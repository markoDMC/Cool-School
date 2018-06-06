package com.example.marko_dmc.cool_school_3m;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class New_Post extends AppCompatActivity {

    private ProgressBar prog_bar;
    private Button post_btn;
    private ImageView post_img;
    private EditText post_text;

    private FirebaseAuth mAuth;
    private StorageReference storage;
    private FirebaseFirestore mStorage;

    //TVAC Github
    private Uri postImageUri = null;
    private String current_user_id;
    private Bitmap compressedImageFile;



    private String skola_id;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_new__post);

                storage = FirebaseStorage.getInstance().getReference();
                mStorage = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();

                current_user_id = mAuth.getCurrentUser().getUid();


                prog_bar=findViewById( R.id.progressBar2 );
                post_img = findViewById(R.id.new_post_image);
                post_text = findViewById(R.id.text_desc);
                post_btn = findViewById(R.id.post_btn);

                post_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setMinCropResultSize(512, 512)
                                .setAspectRatio(1, 1)
                                .start(New_Post.this);



                    }
                });

                mStorage.collection( "Users" ).document( current_user_id ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        skola_id=task.getResult().getString( "idSkole" );


                    }
                } );

                post_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String desc = post_text.getText().toString();

                        if(!TextUtils.isEmpty(desc) && postImageUri != null){

                            prog_bar.setVisibility(View.VISIBLE);

                            final String randomName = UUID.randomUUID().toString();

                            // PHOTO UPLOAD
                            File newImageFile = new File(postImageUri.getPath());
                            try {

                                compressedImageFile = new Compressor(New_Post.this)
                                        .setMaxHeight(720)
                                        .setMaxWidth(720)
                                        .setQuality(50)
                                        .compressToBitmap(newImageFile);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageData = baos.toByteArray();

                            // PHOTO UPLOAD

                            UploadTask filePath = storage.child("post_images").child(randomName + ".jpg").putBytes(imageData);
                            filePath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                                    final String downloadUri = task.getResult().getDownloadUrl().toString();

                                    if(task.isSuccessful()){

                                        File newThumbFile = new File(postImageUri.getPath());
                                        try {

                                            compressedImageFile = new Compressor(New_Post.this)
                                                    .setMaxHeight(100)
                                                    .setMaxWidth(100)
                                                    .setQuality(1)
                                                    .compressToBitmap(newThumbFile);

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        byte[] thumbData = baos.toByteArray();

                                        UploadTask uploadTask = storage.child("post_images/thumbs")
                                                .child(randomName + ".jpg").putBytes(thumbData);

                                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                String downloadthumbUri = taskSnapshot.getDownloadUrl().toString();

                                                Map<String, Object> postMap = new HashMap<>();
                                                postMap.put("image_url", downloadUri);
                                                postMap.put("image_thumb", downloadthumbUri);
                                                postMap.put("desc", desc);
                                                postMap.put("user_id", current_user_id);
                                                postMap.put("timestamp", FieldValue.serverTimestamp());
                                                postMap.put("idSkole",skola_id);

                                                mStorage.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                                        if(task.isSuccessful()){

                                                            Toast.makeText(New_Post.this, "Post was added", Toast.LENGTH_LONG).show();
                                                            Intent mainIntent = new Intent(New_Post.this, Main_Activity.class);
                                                            startActivity(mainIntent);
                                                            finish();

                                                        } else {


                                                        }

                                                        prog_bar.setVisibility(View.INVISIBLE);

                                                    }
                                                });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                //Error handling

                                            }
                                        });


                                    }else{
                                        prog_bar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });


                        }
                        else{
                            Toast.makeText(New_Post.this, "Molim te unesi tekst i sliku.", Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {

                        postImageUri = result.getUri();
                        post_img.setImageURI(postImageUri);

                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                        Exception error = result.getError();

                    }
                }

            }
}

