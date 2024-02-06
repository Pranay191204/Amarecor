package com.amarecor.pranay.amarecor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amarecor.pranay.amarecor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class MainActivity extends AppCompatActivity {
    Button b1;
    ImageView iv;
    EditText edtmail, edtpass;
    CheckBox saveLoginCheckBox;
    String URL = "http://amarecor.com/AndroidFiles/chklogin.php";
    JSONObject json = null;
    SharedPreferences sp;
    SharedPreferences.Editor et;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
*/
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("Session", MODE_PRIVATE);
        et = sp.edit();
        boolean statu = sp.getBoolean("Status", false);
        if (statu) {
                Intent i=new Intent(this,HomePage.class);
              startActivity(i);
              finish();
        } else {

            b1 = (Button) findViewById(R.id.button);
            iv = (ImageView) findViewById(R.id.imageView);
            edtpass = (EditText) findViewById(R.id.editText2);
            edtmail = (EditText) findViewById(R.id.editText);

            saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String editText = edtmail.getText().toString();
                    String editText2 = edtpass.getText().toString();

                    if (editText.length() > 0 && editText2.length() > 0) {
                        new Chklogin().execute(editText, editText2);
                    }
                    else {
                        Toast.makeText(getBaseContext(), "Enter Your Data", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private class Chklogin extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("");
            pd.setMessage("");
            pd.setIndeterminate(false);
            pd.show();
        }
        @Override
        protected String doInBackground(String... strings) {

            String tempname = strings[0];
            String temppassword = strings[1];
            Log.e(tempname,temppassword);
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("name", tempname));
            li.add(new BasicNameValuePair("password", temppassword));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(URL);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                Log.e("data", str);
                JSONArray jArray = new JSONArray(str);

                json = jArray.getJSONObject(0);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           pd.dismiss();
            try {
                String Res = json.getString("res");
                String Message = json.getString("status");
                String emp_id = json.getString("userid");
                String emp_name = json.getString("EName");
                String emp_role = json.getString("ERole");
                int emp_HQid = json.getInt("hqid");
                 Log.e("HQ",emp_HQid+"");
                if (Res.equals("true")) {

                    Intent i = new Intent(MainActivity.this, HomePage.class);
                    et.putString("Empid",emp_id);
                    et.putString("EmpName",emp_name);
                    et.putString("EmpRole",emp_role);
                    et.putString("EmpHQid",emp_HQid+"");

                   if(saveLoginCheckBox.isChecked()) {
                       et.putBoolean("Status", true);
                   }
                   else
                   {
                       et.putBoolean("Status", false);
                   }
                    et.commit();
                    startActivity(i);
                } else {

                    showdialog("Login fails", Message, Res, emp_id);

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }

        public void showdialog(String title, String message, String code, final String id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle(title);


            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}