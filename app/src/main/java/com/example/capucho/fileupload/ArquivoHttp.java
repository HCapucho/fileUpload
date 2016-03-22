package com.example.capucho.fileupload;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Capucho on 18/03/2016.
 */
public class ArquivoHttp {
    public File salvarArquivo(File file){
        try {
            String line = "\r\n";
            String twoHyphens = "--";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1*1024*1024;
            String boundary = Long.toString(System.currentTimeMillis());

            URL url = new URL("http://192.168.1.31/eventosesportivos/api/v1/upload");
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethodUtil.POST.name());
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + line);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"fileUpload\"; filename=\"" + file.getName() + "\"" + line);
            outputStream.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + line);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + line);
            outputStream.writeBytes(line);


            FileInputStream inputStream = new FileInputStream(file);
            bytesAvailable = inputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = inputStream.read(buffer, 0, bufferSize);
            while(bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = inputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = inputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(line);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + line);
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            connection.connect();
            int statusCode = connection.getResponseCode();
            statusCode++;


        } catch (Exception e) {
            String error = e.getMessage();
            error += "";
        }


        return null;
    }


}
