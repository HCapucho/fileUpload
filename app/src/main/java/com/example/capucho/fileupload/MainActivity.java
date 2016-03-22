package com.example.capucho.fileupload;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private static final int SELECT_IMAGE = 1;
    private ImageView imageView;
    private Button btnImage;
    private Button btnEnviar;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);
        btnImage = (Button) findViewById(R.id.btn_image);
        btnEnviar = (Button) findViewById(R.id.btn_enviar);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.lbl_selecione_uma_imagem)), SELECT_IMAGE);
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AtualizarArquivoAsyncTask(file).execute();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            String tempPath = getApplicationContext().getFilesDir() + "/Pictures/FileUpload";
            Uri uri = data.getData();
            String selectedImagePath = getPath(uri);
            try {
                File atual = new File(selectedImagePath);
                File novo = new File(tempPath);
                novo.mkdirs();
                File output = new File(novo, atual.getName());
                copy(atual, output);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse("file:"+output.getAbsolutePath()));
                bitmap = resize(bitmap);
                imageView.setImageBitmap(bitmap);
                file = new File(output.getAbsolutePath());
                String sjdfhsdhf = "";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected Bitmap resize(Bitmap bitmap){
        int w = 0;
        int h = 0;
        int lateral = 256; // tamanho final da dimensão maior da imagem
        //define um indice = 1 pois se der erro vai manter a imagem como está.
        Integer idx = 1;
        // reupera as dimensões da imagem
        w = bitmap.getWidth();
        h = bitmap.getHeight();
        // verifica qual a maior dimensão e divide pela lateral final para definir qual o indice de redução
        if(w <= lateral){
            lateral = w;
        }
        if(h <= lateral){
            lateral = h;
        }
        if ( w <= h){
            idx = w / lateral;
        } else {
            idx = h / lateral;
        }
        // aplica o indice de redução nas novas dimensões
        w = w / idx;
        h = h / idx;
        // cria nova instancia da imagem já redimensionada
        Bitmap bmpReduzido = Bitmap.createScaledBitmap(bitmap, w, h, true);

        return bmpReduzido;
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

}
