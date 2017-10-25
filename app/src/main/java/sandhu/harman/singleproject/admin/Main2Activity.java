//package sandhu.harman.singleproject.admin;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.io.IOException;
//
//import sandhu.harman.singleproject.R;
//
//public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
//
//    private static final int SELECT_PHOTO = 1;
//    Button select,upload;
//    private FirebaseStorage storage;
//    private StorageReference storageRef;
//    private StorageReference imageRef;
//    private ProgressDialog progressDialog;
//    private UploadTask uploadTask;
//    private boolean imageSelected=false;
//ImageView iv;
//    private Uri filePath;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
//                findViewById(R.id.selectImg).setOnClickListener(this);
//        findViewById(R.id.uploadImg).setOnClickListener(this);
//        iv= (ImageView) findViewById(R.id.img);
//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReference();
//
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId())
//
//        {
//            case R.id.selectImg:selectImage();
//                break;
//            case R.id.uploadImg:
//
//                uploadFile();
//                break;
//        }
//    }
//    private void selectImage() {
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//    }
//
////    public void uploadImage() {
////        //create reference to images folder and assing a name to the file that will be uploaded
////        imageRef = storageRef.child("images/"+selectedImage.getLastPathSegment());
////        //creating and showing progress dialog
////        progressDialog = new ProgressDialog(this);
////        progressDialog.setMax(100);
////        progressDialog.setMessage("Uploading...");
////        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
////        progressDialog.show();
////        progressDialog.setCancelable(false);
////        //starting upload
////        uploadTask = imageRef.putFile(selectedImage);
////        // Observe state change events such as progress, pause, and resume
////        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
////            @Override
////            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
////                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
////                //sets and increments value of progressbar
////                progressDialog.incrementProgressBy((int) progress);
////            }
////        });
////        // Register observers to listen for when the download is done or if it fails
////        uploadTask.addOnFailureListener(new OnFailureListener() {
////            @Override
////            public void onFailure(@NonNull Exception exception) {
////                // Handle unsuccessful uploads
////                Toast.makeText(Main2Activity.this,"Error in uploading!",Toast.LENGTH_SHORT).show();
////                progressDialog.dismiss();
////            }
////        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
////            @Override
////            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
////                Uri downloadUrl = taskSnapshot.getDownloadUrl();
////                Toast.makeText(Main2Activity.this,"Upload successful",Toast.LENGTH_SHORT).show();
////                progressDialog.dismiss();
////                //showing the uploaded image in ImageView using the download url
////            }
////        });
////    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case SELECT_PHOTO:
//                if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
//                    filePath = data.getData();
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                        iv.setImageBitmap(bitmap);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//        }
//    }
//
//    private void uploadFile() {
//        //if there is a file to upload
//        if (filePath != null) {
//            //displaying a progress dialog while upload is going on
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading");
//            progressDialog.show();
//
//            StorageReference riversRef = storageRef.child("images/pic.jpg");
//            riversRef.putFile(filePath)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            //if the upload is successfull
//                            //hiding the progress dialog
//                            progressDialog.dismiss();
//                            taskSnapshot.getDownloadUrl();
//
//                            //and displaying a success toast
//                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            //if the upload is not successfull
//                            //hiding the progress dialog
//                            progressDialog.dismiss();
//
//                            //and displaying error message
//                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            //calculating progress percentage
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//
//                            //displaying percentage in progress dialog
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
//                        }
//                    });
//        }
//        //if there is not any file
//        else {
//            //you can display an error toast
//        }
//    }
//}
