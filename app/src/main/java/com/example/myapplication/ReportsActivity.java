package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class ReportsActivity extends AppCompatActivity {

    ArrayList<Informe> informes;
    ArrayList<Informe> informeFiltrado;
    RecyclerView rvInformes;
    InformeAdapter informeAdapter;
    FloatingActionButton floatingActionButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);


        informes = new ArrayList();
        informeFiltrado = new ArrayList();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("news_report").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document: task.getResult()){
                            Informe informe = document.toObject(Informe.class);
                            informes.add(informe);
                        }
                        Log.d("meca", informes.get(0).getPhotoPath());
                        //informeAdapter = new InformeAdapter(informes);

                        //rvInformes = findViewById(R.id.rvInformes);
                        //rvInformes.setAdapter(informeAdapter);

                    }else{
                        Log.w("meca", "error meca");
                    }
                });

        floatingActionButton = findViewById(R.id.filtrarInforme);

        floatingActionButton.setOnClickListener(view ->{

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.filtroinforme, null);
            DatePicker desde = v.findViewById(R.id.calendariodesde);
            DatePicker hasta = v.findViewById(R.id.calendariohasta);

            final Long[] millisdesde = new Long[1];
            final Long[] millishasta = new Long[1];

            CheckBox alto = v.findViewById(R.id.alto);
            CheckBox medio = v.findViewById(R.id.medio);
            CheckBox bajo = v.findViewById(R.id.bajo);

            desde.setOnDateChangedListener((view1, year, monthOfYear, dayOfMonth) -> {

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, desde.getDayOfMonth());
                cal.set(Calendar.MONTH, desde.getMonth());
                cal.set(Calendar.YEAR, desde.getYear());
                millisdesde[0] = cal.getTimeInMillis();

                Log.d("año", String.valueOf(millisdesde[0]));

            });


            hasta.setOnDateChangedListener((view1, year, monthOfYear, dayOfMonth) -> {

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, hasta.getDayOfMonth());
                cal.set(Calendar.MONTH, hasta.getMonth());
                cal.set(Calendar.YEAR, hasta.getYear());
                millishasta[0] = cal.getTimeInMillis();

                Log.d("año", String.valueOf(millishasta[0]));

            });

            builder.setView(v);
            builder.setPositiveButton("Buscar Informes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    for (Informe informe:informes){
                        if (alto.isChecked() ){
                            if (informe.getDate()  > millisdesde[0]  &&  informe.getDate() < millishasta[0] ){
                                if (informe.getPriority().equals("Alto")){
                                    Log.d("altoo",  informe.getPriority());
                                    informeFiltrado.add(informe);

                                }
                            }
                        }

                        if (medio.isChecked() ){
                            if (informe.getDate()  > millisdesde[0]  &&  informe.getDate() < millishasta[0] ){
                                if (informe.getPriority().equals("Medio")){
                                    Log.d("altoo",  informe.getPriority());
                                    informeFiltrado.add(informe);

                                }
                            }
                            //Log.d("altoo", String.valueOf(millisdesde[0]));
                        }

                        if (bajo.isChecked() ){
                            if (informe.getDate()  > millisdesde[0]  &&  informe.getDate() < millishasta[0] ){
                                if (informe.getPriority().equals("Bajo")){
                                    Log.d("altoo",  informe.getPriority());
                                    informeFiltrado.add(informe);

                                }
                            }
                            //Log.d("altoo", String.valueOf(millisdesde[0]));
                        }
                    }

                    informeAdapter = new InformeAdapter(informeFiltrado);

                    rvInformes = findViewById(R.id.rvInformes);
                    rvInformes.setAdapter(informeAdapter);


                }
            });
            builder.setNegativeButton("Cancelar", (dialog, which) -> {

            });
            builder.show();
        });

    }
}