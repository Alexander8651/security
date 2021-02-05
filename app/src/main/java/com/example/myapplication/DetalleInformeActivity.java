package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetalleInformeActivity extends AppCompatActivity {

    Informe informe;
    ImageView foto;
    TextView descipcion;
    TextView condicion;
    TextView fecha;
    TextView prioridad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_informe);

        informe = (Informe) getIntent().getExtras().get("informe");
        Log.d("pepepepe", informe.getPhotoPath());
        Log.d("pepepepe", informe.getDate().toString());

        foto = findViewById(R.id.foto);
        descipcion = findViewById(R.id.descripcion);
        condicion = findViewById(R.id.condicion);
        fecha = findViewById(R.id.fecha);
        prioridad = findViewById(R.id.prioridad);

        Glide.with(this).load(informe.getPhotoPath()).into(foto);
        descipcion.setText(informe.getDescription());
        condicion.setText(informe.getCondition());
        prioridad.setText(informe.getPriority());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(informe.getDate()));
        fecha.setText(dateString);

        Log.d("pepepepe", dateString);
    }
}