package org.grd_p.grd_project.userConnection;

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

public class LoginConnection extends AsyncTask<String, Void, String> {
    Context context;
    ProgressDialog loading;

    public LoginConnection(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url="http://101.101.163.32/login";
        if(type.equals("login")){
            try{
                String email=params[1];
                String pw = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pw,"UTF-8");
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
        loading = ProgressDialog.show(context,"Waiting for log in..",null,true,true);
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
