package org.grd_p.grd_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class UpdateConnection extends AsyncTask<String, Void, String> {
    Context context;
    ProgressDialog loading;

    public UpdateConnection(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String update_url="http://ec2-13-125-62-98.ap-northeast-2.compute.amazonaws.com";
        if(type.equals("updateInfo")){
            try{
                //result = UpdateConnection.execute(type, age, sex, height, weight).get();
                String age=params[1];
                String sex = params[2];
                String height=params[3];
                String weight = params[4];
                URL url = new URL(update_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("age","UTF-8")+"="+URLEncoder.encode(age,"UTF-8")+"&"
                        +URLEncoder.encode("sex","UTF-8")+"="+URLEncoder.encode(sex,"UTF-8")+"&"
                        +URLEncoder.encode("height","UTF-8")+"="+URLEncoder.encode(height,"UTF-8")+"&"
                        +URLEncoder.encode("weight","UTF-8")+"="+URLEncoder.encode(weight,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8")); //UTF-8?
                String result="";
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result; //서버에서 처리한 결과 문자열 리턴받음

            }catch (MalformedURLException e){
                Log.d("DBGLOG","MalformedURLException in doInBackGround");
                e.printStackTrace();
            }catch (IOException e){
                Log.d("DBGLOG","IOException in doInBackGround");
                e.printStackTrace();
            }catch (Exception e){
                Log.d("DBGLOG","Exception in doInBackGround");
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(context,"Waiting for updating..",null,true,true);
    }

    @Override
    protected void onPostExecute(String s) { // s: doInBackground에서 return한 결과 문자열
        super.onPostExecute(s);
        loading.dismiss();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
