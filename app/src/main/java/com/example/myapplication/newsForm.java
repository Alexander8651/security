package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.Manifest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class newsForm extends AppCompatActivity {

    DatabaseReference mRootReference;
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    private TextView tv1;
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_form);

        //se crea la instancia a la base de datos
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mRootReference = FirebaseDatabase.getInstance().getReference();
        textInputLayout=findViewById(R.id.dropDownOption);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        String[] options = {};

        tv1 =(TextView)findViewById(R.id.textViewTitle);
        String dato = getIntent().getStringExtra("dato");
        String code =  getIntent().getStringExtra("code");
        tv1.setText(dato);

        db.collection("subtypes_of_novelty").whereEqualTo("type_novelty_key", code).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Log.d("meca", document.getId() + "=> " + document.getData());
                              // options = add_element("hola");
                            }
                        }else{
                            Log.w("meca", "error meca");
                        }
                    }
                });

        if (ContextCompat.checkSelfPermission(newsForm.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(newsForm.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(newsForm.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }

        //se crea el arraydapater
        String[] nombres = new String[]{"luna", "pedro", "martha"};
        ArrayAdapter<String> adapter= new ArrayAdapter<>(
                this, R.layout.dropdown_item, options
        );
        autoCompleteTextView.setAdapter(adapter);
    }

    //metodo para mostrar y ocultar el menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    //metodo para asignar funcion a las opciones del menu
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.mainMenu){
            //debe redirigir al activity del menu principal
            Toast.makeText(this, "menu principal", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.signOff){
            //debe cerrar sesion
            Toast.makeText(this, "cerrar sesion", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    //metodo para crear nombre unico a cada foto
    String mCurrentPhotoPath;
    private File createImageFile() throws IOException{
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Backup_" + timeStamp +"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    //Metodo que toma foto y crea el archivo
    static final int REQUEST_TAKE_PHOTO = 1;
    public void takePicture(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile=null;
            try{
                photoFile=createImageFile();

            }catch (IOException ex){

            }

            if(photoFile != null){
                Uri photoURI= FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI.toString());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }

        }

    }



}