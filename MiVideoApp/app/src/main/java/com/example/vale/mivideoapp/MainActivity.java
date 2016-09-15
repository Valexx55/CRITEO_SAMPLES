package com.example.vale.mivideoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.MediaController;
import android.widget.VideoView;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int CODIGO_ACTIVIDAD = 100;
    private static final String SUFIJO_VIDEO = ".mp4";
    private static final String PREFIJO_VIDEO = "VALE_VID_";


    /**
     * Creo el fichero de video y obtengo la uri del mismo
     *
     * @return La URI que identifica al fichero.
     */
    private Uri crearFicheroVideo ()
    {
        Uri uri_dest = null;
        String momento_actual = null;
        String nombre_fichero = null;
        File f = null;

        momento_actual = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); //así nos garantizamos emplear un sufijo aleatorio: el nombre del archivo de la imagen incluirá el momento exacto
        nombre_fichero = PREFIJO_VIDEO + momento_actual + SUFIJO_VIDEO;

        String ruta_captura_video = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath()+"/"+nombre_fichero;

        Log.d(getClass().getCanonicalName(), "RUTA VIDEO = " + ruta_captura_video);


        f = new File(ruta_captura_video);


        try
        {
            if (f.createNewFile())
                Log.d(getClass().getCanonicalName(), "Fichero creado");
            else
                Log.d(getClass().getCanonicalName(), "Fichero NO creado (ya existía)");
        }
        catch (IOException e)
        {
            Log.e(getClass().getCanonicalName(), "Error creando el fichero", e);
        }

        uri_dest = Uri.fromFile(f);

        Log.d(getClass().getCanonicalName(), "URI VIDEO = " + uri_dest.toString());


        return uri_dest;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE); //preparo el intent para grabar vídeo

        Uri videoUri = crearFicheroVideo();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri); //INDICO la dirección del fichero donde quiero que se guarde

        startActivityForResult(intent, CODIGO_ACTIVIDAD);//el segundo parámetro es una forma de identificar la petición, para poder ser recibida posteriormente, además de indicarle a Android que será una Actividad HIJA
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(getClass().getCanonicalName(), "VUELVE DE hacer el Video"); //requestCode == CODIGO_ACTIVIDAD

        switch (resultCode)
        {
            case RESULT_OK:


                Log.d(getClass().getCanonicalName(), "La cosa fue bien Código " + resultCode);

                Uri videoUri = data.getData(); //recupero la Uri del intent

                VideoView videoView = (VideoView) findViewById(R.id.videoView);

                MediaController mediaController = new MediaController(this);//esto serán los controles (play, rev, fw), que aparecerán
                mediaController.setAnchorView(videoView);//asociados a la reproducción del vídeo

                videoView.setMediaController(mediaController);//lo asocio al viodeView
                videoView.setVideoURI(videoUri);//
                videoView.start();//y le "doy al play"


                break;

            case RESULT_CANCELED:
                Log.d(getClass().getCanonicalName(), "La cosa se canceló " + resultCode);
                break;

            default:
                Log.d(getClass().getCanonicalName(), "FIN CON CÓDIGO " + resultCode);

        }
    }

}
