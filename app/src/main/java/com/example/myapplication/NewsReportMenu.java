package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewsReportMenu extends AppCompatActivity {
    private ImageView iw1;
    private TextView tw1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_report_menu);

        iw1 =(ImageView)findViewById(R.id.accident);
        tw1 =(TextView) findViewById(R.id.accidentText);

        //Agrega el icono al action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


    }

    public void passNewForm(View view){
        Intent i = new Intent(this, newsForm.class);
        i.putExtra("dato", iw1.getContentDescription().toString());
        i.putExtra("code", tw1.getContentDescription().toString());
        startActivity(i);
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
}