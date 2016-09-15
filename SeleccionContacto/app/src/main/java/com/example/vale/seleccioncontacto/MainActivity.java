package com.example.vale.seleccioncontacto;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


/**
 * TODO Dividir el siguiente código en funciones/métodos en el máximo número de funciones con entidad o logica
 * haciendo el código más lógico y entendible
 */
public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, 100);//si hiciera varias llamadas, podría modificar el código para idnetificar cada petición
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Cursor cursor = null;
        String phoneNo = null ;
        String name = null;
        Uri uri = null;

        if (resultCode == RESULT_OK) {
            try {

                uri = data.getData();
                Log.d(getClass().getCanonicalName(), "Selecciono los detalles de la base datos/ registro : (uri) = " + uri.toString());

                cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                phoneNo = cursor.getString(phoneIndex);
                name = cursor.getString(nameIndex);

                TextView textView1 = (TextView) findViewById(R.id.telfcontacto);
                textView1.setText(name);

                TextView textView2 = (TextView) findViewById(R.id.nomcontacto);
                textView2.setText(phoneNo);

            } catch (Exception e)
            {
                Log.e(getClass().getCanonicalName(), "ERROR procesando el intent del Contacto ", e);
            }
        } else
        {
            Log.e(getClass().getCanonicalName(), "Error seleccionando contacto");
        }
    }

}
