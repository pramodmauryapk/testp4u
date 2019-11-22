package com.p4u.parvarish.menu_data;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Download_GIF extends AsyncTask<String, Void, String> {
    static String url_image=null;

    public Download_GIF(String url) {
        this.url_image = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String filepath = null;
        try {
            //set the download URL, a url that points to a file on the internet
            //this is the file to be downloaded
            URL url = new URL(url_image);
            //create the new connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //set up some things on the connection
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            //and connect!
            urlConnection.connect();
            //set the path where we want to save the file
            //in this case, going to save it on the root directory of the
            //sd card.
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, specifying the path, and the filename
            //which we want to save the file as.

            String filename = "downloadedFile.gif";   // you can download to any type of file ex:.jpeg (image) ,.txt(text file),.mp3 (audio file)
            Log.i("Local filename:", "" + filename);
            File file;
            file = new File(SDCardRoot, filename);
            if (file.createNewFile()) {
                file.createNewFile();
            }

            //this will be used to write the downloaded data into the file we created
            FileOutputStream fileOutput = new FileOutputStream(file);

            //this will be used in reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file
            int totalSize = urlConnection.getContentLength();
            //variable to store total downloaded bytes
            int downloadedSize = 0;

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0; //used to store a temporary size of the buffer

            //now, read through the input buffer and write the contents to the file
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                //add the data in the buffer to the file in the file output stream (the file on the sd card
                fileOutput.write(buffer, 0, bufferLength);
                //add up the size so we know how much is downloaded
                downloadedSize += bufferLength;
                //this is where you would do something to report the prgress, like this maybe
                Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);

            }
            //close the output stream when done
            fileOutput.close();
            if (downloadedSize == totalSize) filepath = file.getPath();

            //catch some possible errors...
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            filepath = null;
            e.printStackTrace();
        }
        Log.i("filepath:", " " + filepath);

        return filepath;


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
