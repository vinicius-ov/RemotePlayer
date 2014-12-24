package com.ihc.trabalho.ihcplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.widget.Toast.*;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.EscolheMusica);
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Escolhendo musica!", Toast.LENGTH_LONG).show();
                Log.d("DEBUG","Clicou area escolher!");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent,1);
            }
        });
        RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.EnviaMusica);
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Arquivo enviado para o player!", Toast.LENGTH_LONG).show();
                Log.d("DEBUG","Clicou area enviar!");
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == 1){

            if(resultCode == Activity.RESULT_OK){
                Uri globalUri = data.getData();
                try {
                    FileInputStream musica = new FileInputStream(globalUri.getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(getApplication().getApplicationContext(),globalUri);

                String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                TextView song = (TextView) findViewById(R.id.textMusica);
                Log.d("DEBUG","Musica: "+albumName);
                albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
                Log.d("DEBUG","Artista: "+albumName);
                albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                Log.d("DEBUG","Álbum: "+albumName);
                byte[] foto = mmr.getEmbeddedPicture();
                Bitmap photo = BitmapFactory.decodeByteArray(foto,0,foto.length);
                ImageView image = (ImageView) findViewById(R.id.imagemAlbum);
                image.setImageBitmap(photo);

            }else Log.e("Debug", "NOT OK");

        }else Log.e("Debug", "REQUEST NOT 1");

    }

}
