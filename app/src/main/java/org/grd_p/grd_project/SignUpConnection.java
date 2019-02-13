package org.grd_p.grd_project;

import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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

public class SignUpConnection extends AsyncTask<String, Void, String> {
    Context context;
    ProgressDialog loading;
    AlertDialog alertDialog;

    public SignUpConnection(Context context, AlertDialog alertDialog) {
        this.context=context;
        this.alertDialog = alertDialog;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String signup_url="http://..../signup.php";
        if(type.equals("signUp")){
            try{
                String name = params[1];
                String pw = params[2];
                String serialNum = params[3];
                String email = params[4];
                URL url = new URL(signup_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pw,"UTF-8")+"&"
                        +URLEncoder.encode("serialnumber","UTF-8")+"="+URLEncoder.encode(serialNum,"UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1")); //UTF-8?
                String result="";
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(context,"Waiting for sign up..",null,true,true);
    }

    @Override
    protected void onPostExecute(String s) { // s: doInBackground에서 return한 결과 문자열
        super.onPostExecute(s);
        loading.dismiss();
        if(!s.equals("success")){ //success가 아닌 실패 오류 원인 문자열 넘어오면
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("회원가입 실패");
            alertDialog.setMessage(s);
            alertDialog.show(); //실패 알림창 띄움
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}