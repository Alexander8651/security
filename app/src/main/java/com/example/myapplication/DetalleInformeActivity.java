package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DetalleInformeActivity extends AppCompatActivity {

    Informe informe;
    ImageView foto;
    TextView descipcion;
    TextView condicion;
    TextView fecha;
    TextView prioridad;

    Context mContext;

    String dest;
    String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_informe);

        mContext = getApplicationContext();



        File path = new File(
                Environment.getExternalStorageDirectory(),
                "pdf"
        );

        path.mkdir();



        File dir = new File(android.os.Environment.getExternalStorageDirectory(), "pdfs");
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir,new SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) +".pdf");

        dest = file.getPath() ;


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

        Button button = findViewById(R.id.crearpdf);


        //Este handler será ejecutado 5 segundos después.
        new Handler().postDelayed(new Runnable() {
            public void run() {

            };
        }, 2000);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf(dest);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                File file = new File(dest);

                Uri uri =  FileProvider.getUriForFile(getApplicationContext(),
                        "com.example.android.fileprovider",
                        file);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(sharingIntent);
            }
        });


    }

    public void createPdf(String dest) {

        if (new File(dest).exists()) {
            new File(dest).delete();
            Log.d("meejecuto", "meejecuto");

        }

        try {
            /**
             * Creating Document
             */
            PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(dest));
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            PdfDocumentInfo info = pdfDocument.getDocumentInfo();

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = formatter.format(new Date(informe.getDate()));

            Log.d("meejecuto", "meejecuto");

            info.setTitle("Example of iText7 by Pratik Butani");
            info.setAuthor("Pratik Butani");
            info.setSubject("iText7 PDF Demo");
            info.setKeywords("iText, PDF, Pratik Butani");
            info.setCreator("A simple tutorial example");

            Document document = new Document(pdfDocument, PageSize.A4, true);

            /***
             * Variables for further use....
             */
            Color mColorAccent = new DeviceRgb(153, 204, 255);
            Color mColorBlack = new DeviceRgb(0, 0, 0);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;

            /**
             * How to USE FONT....
             */
            PdfFont font = PdfFontFactory.createFont("assets/fonts/brandon_medium.otf", "UTF-8", true);

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator(new DottedLine());
            lineSeparator.setStrokeColor(new DeviceRgb(0, 0, 68));

            //foto informe

            File carpeta = new File(android.os.Environment.getExternalStorageDirectory(), "fotosinforme");
            if (!carpeta.exists()) {
                carpeta.mkdir();
            }
            File file = new File(carpeta,new SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) +".pdf");
            file.createNewFile();


            //Convert bitmap to byte array
            Bitmap bitmap = ((BitmapDrawable)foto.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            ImageData data = ImageDataFactory.create(file.getPath());
            Image img = new Image(data);
            document.add(img);

            // Title Order Details...
            // Adding Title....
            Text mOrderDetailsTitleChunk = new Text("Informe").setFont(font).setFontSize(36.0f).setFontColor(mColorBlack);
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(mOrderDetailsTitleParagraph);

            // Fields of Order Details...
            // Adding Chunks for Title and value
            Text mOrderIdChunk = new Text("Priodidad Informe:").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent);
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);

            Text mOrderIdValueChunk = new Text(informe.getPriority()).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Paragraph mOrderIdValueParagraph = new Paragraph(mOrderIdValueChunk);
            document.add(mOrderIdValueParagraph);

            // Adding Line Breakable Space....
            document.add(new Paragraph(""));
            // Adding Horizontal Line...
            document.add(lineSeparator);
            // Adding Line Breakable Space....
            document.add(new Paragraph(""));

            // Fields of Order Details...
            Text mOrderDateChunk = new Text("Fecha Informe:").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent);
            Paragraph mOrderDateParagraph = new Paragraph(mOrderDateChunk);
            document.add(mOrderDateParagraph);

            Text mOrderDateValueChunk = new Text(dateString).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Paragraph mOrderDateValueParagraph = new Paragraph(mOrderDateValueChunk);
            document.add(mOrderDateValueParagraph);

            document.add(new Paragraph(""));
            document.add(lineSeparator);
            document.add(new Paragraph(""));

            // Fields of Order Details...
            Text mOrderAcNameChunk = new Text("Descripcion:").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent);
            Paragraph mOrderAcNameParagraph = new Paragraph(mOrderAcNameChunk);
            document.add(mOrderAcNameParagraph);

            Text mOrderAcNameValueChunk = new Text(informe.getDescription()).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Paragraph mOrderAcNameValueParagraph = new Paragraph(mOrderAcNameValueChunk);
            document.add(mOrderAcNameValueParagraph);

            document.add(new Paragraph(""));
            document.add(lineSeparator);
            document.add(new Paragraph(""));

            document.close();

            Toast.makeText(mContext, "Creado... :)", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {

            Log.d("createPdf: Error " , e.getLocalizedMessage());


        } catch (ActivityNotFoundException ae) {
            Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }

        Log.d("destinooo", dest);

    }

    public static String getAppPath(Context context) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory()
                + File.separator
                + context.getResources().getString(R.string.app_name)
                + File.separator);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getPath() + File.separator;
    }
}