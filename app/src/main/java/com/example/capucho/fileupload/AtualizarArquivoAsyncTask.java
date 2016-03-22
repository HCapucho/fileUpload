package com.example.capucho.fileupload;

import java.io.File;

/**
 * Created by Geen on 14/03/2016.
 */
public class AtualizarArquivoAsyncTask extends BaseAsyncTask<File, File> {
    private ArquivoHttp http;
    private File arquivo;

    public AtualizarArquivoAsyncTask(File arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    protected File doInBackground(File... params) {
        http = new ArquivoHttp();
        File file = null;
        if(arquivo != null){
            file = http.salvarArquivo(arquivo);
        }
        String teste = file.getPath();
        return file;
    }
}
