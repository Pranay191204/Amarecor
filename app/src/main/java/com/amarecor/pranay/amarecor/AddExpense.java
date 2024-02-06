package com.amarecor.pranay.amarecor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class AddExpense extends AppCompatActivity {

    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int date = c.get(Calendar.DAY_OF_MONTH);
    EditText expdate, et1, et2, et3, sr1,edt43,edt44,edtdates,expdetail,exprs,dailyall,totalexp,remark,emrole;
    TextView tv;
    String URL1 = "http://amarecor.com/AndroidFiles/getExpData.php";
    String URL2 = "http://amarecor.com/AndroidFiles/insertExpmain.php";
    String URL3 = "http://amarecor.com/AndroidFiles/HQlist.php";
    String UPLOAD_URL = "http://amarecor.com/AndroidFiles/addfile.php";

    SharedPreferences sp;
    SharedPreferences.Editor et;
    Button button10, button11,button8;
    Intent intent;
    String empid,Erole;
    String hqid;
    //String emprole;
    ProgressDialog pd;
    JSONObject json = null;
    JSONObject json1 = null;
    String arid[];
    String[] arname;
    String[] arHq;
    String[] places;
    int id;
    Uri filePath;
    //int res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        SharedPreferences sp = getSharedPreferences("Session", MODE_PRIVATE);
        empid = sp.getString("Empid", "");
        hqid = sp.getString("EmpHQid", "");
        Erole = sp.getString("EmpRole", "");
        String role = "";
        if (Erole.equals("1")) {
            role = "Admin";
        } else if (Erole.equals("2")) {
            role = "Manager";
        } else if (Erole.equals("3")) {
            role = "Medical Representative";
        }
        emrole = (EditText) findViewById(R.id.role);
        emrole.setText(role);
        String daily = sp.getString("DailyAllow", "");
        String dailyallow = "";
        if (Erole.equals("2")) {
            dailyallow = "250";
        } else if (Erole.equals("3")) {
            dailyallow = "150";
        }
        dailyall = (EditText) findViewById(R.id.dailyallow);
        dailyall.setText(dailyallow);
        //Toast.makeText(this, empid+"", Toast.LENGTH_SHORT).show();
        sr1 = (EditText) findViewById(R.id.sr1);
        et1 = (EditText) findViewById(R.id.Expid);
        et2 = (EditText) findViewById(R.id.empid);
        et3 = (EditText) findViewById(R.id.editText42);
        edt43 = (EditText) findViewById(R.id.hq);
        edt44 = (EditText) findViewById(R.id.role);
        expdetail = (EditText) findViewById(R.id.expdetail);
        exprs = (EditText) findViewById(R.id.exprs);
        tv = (TextView)findViewById(R.id.files);
        //dailyallow = (EditText)findViewById(R.id.dailyallow);
        totalexp = (EditText) findViewById(R.id.totalexp);
        remark = (EditText) findViewById(R.id.remark);
        edtdates = (EditText) findViewById(R.id.expdate);
        button10 = (Button) findViewById(R.id.button10);
        button11 = (Button) findViewById(R.id.button11);
        button8 = (Button) findViewById(R.id.button8);

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 123);
            }
        });
        new getData().execute(empid,hqid);
        //   new getHQ().execute();

        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Expid = et1.getText().toString();
                String empid = et2.getText().toString();
                String empname = et3.getText().toString();
                String hq = edt43.getText().toString();
                String role = edt44.getText().toString();
                String date = edtdates.getText().toString();
                String place = sr1.getText().toString();
                String otherexpdetail = expdetail.getText().toString();
                String otherexp = exprs.getText().toString();
                String dailyallowance = dailyall.getText().toString();
                String total = totalexp.getText().toString();
                String remark1 = remark.getText().toString();
                //String attachement = editText7.getText().toString();
               // new UploadFile().execute();
                String file="";
                if(filePath==null)
                {
                   // Toast.makeText(AddExpense.this, "Please Enter File..", Toast.LENGTH_SHORT).show();
                }else {
                    uploadMultipart();
                    String path = FilePath.getPath(AddExpense.this, filePath);
                    String filename = path.substring(path.lastIndexOf("/")+1);

                    if (filename.indexOf(".") > 0) {
                        file = filename.substring(0, filename.lastIndexOf("."));
                    } else {
                        file =  filename;
                    }

                }//getting the actual path of the image

                new Adddata().execute(Expid,empid,empname,hq,Erole,date,place,otherexpdetail,otherexp,dailyallowance,total,remark1,file);



            }
        });
        sr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final  ArrayList<String> ar=new ArrayList<>();

                AlertDialog.Builder bu = new AlertDialog.Builder(AddExpense.this);
                bu.setTitle("Select Place");
                bu.setCancelable(false);
                bu.setMultiChoiceItems(places, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            ar.add(places[i]);
                        }
                        else
                        {
                            ar.remove(places[i]);
                        }
                    }
                });
                bu.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String temp="";
                        for(int i=0;i<ar.size();i++)
                        {
                            temp+=ar.get(i);
                            if(i<ar.size()-1){
                                temp+=",";
                            }
                        }
                        sr1.setText("");

                        sr1.setText(temp);

                        dialog.dismiss();
                    }
                });
                AlertDialog a = bu.create();
                a.show();
            }
        });

        edtdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp = new DatePickerDialog(AddExpense.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtdates.setText(dayOfMonth + "/" + (month+1) + "/" + year);

                    }
                }, year, month, date);
                dp.setTitle("Choose Date");
                dp.show();
            }
        });
        exprs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String DA=dailyall.getText().toString();
                String Exp=exprs.getText().toString();
                String regex = "\\d+";
                if(!Exp.isEmpty()){
                    if(Exp.matches(regex)){
                        int totAl=Integer.parseInt(DA)+Integer.parseInt(Exp);
                        totalexp.setText(totAl+"");
                    }
                    else{
                         Toast.makeText(AddExpense.this, "Enter the values in numbers", Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    totalexp.setText(dailyall.getText().toString());
                }

            }
        });
    }
    public void uploadMultipart() {
        //getting name for the image
        String name = tv.getText().toString().trim();

        //getting the actual path of the image
        String path = FilePath.getPath(this, filePath);
       String filename = path.substring(path.lastIndexOf("/")+1);
        String file;
        if (filename.indexOf(".") > 0) {
            file = filename.substring(0, filename.lastIndexOf("."));
        } else {
            file =  filename;
        }

        if (path == null) {

            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("Filename",file)
                        //.setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 123 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            tv.setText(filePath.getPath());
        }

    }

    private class getData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(AddExpense.this);
            pd.setTitle("Loading..");
            pd.setMessage("Please Wait...");
            pd.setIndeterminate(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String tempid = strings[0];
            String temphqid = strings[1];
            Log.e("ID", tempid);
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("userid", tempid));
            li.add(new BasicNameValuePair("hqid", temphqid));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(URL1);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                Log.e("data", str);
                JSONArray jArray = new JSONArray(str);

                JSONObject jo= jArray.getJSONObject(0);
                JSONArray jEmpData=jo.getJSONArray("EmpData");
                JSONArray jHQ=jo.getJSONArray("Places");
                places=new String[jHQ.length()];
                for(int i=0;i<jHQ.length();i++) {
                    json = jHQ.getJSONObject(i);
                    places[i]=json.getString("cityName");
                    //  workedwithar.add(json.getString("Name"));
                }
                json1=jEmpData.getJSONObject(0);

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
           pd.dismiss();
            try {
                String Res = json1.getString("res");
                String EmpName = json1.getString("name");
                String Empid = json1.getString("Empid");
                String expid = json1.getString("expno");
                String hqName = json1.getString("hqName");

                if (Res.equals("true")) {
                    et2.setText(Empid);
                    et1.setText(expid);
                    et3.setText(EmpName);
                    edt43.setText(hqName);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    }

    private class Adddata extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(AddExpense.this);
            pd.setTitle("Loading..");
            pd.setMessage("Please Wait...");
            pd.setIndeterminate(false);
            pd.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            String tempExpid = strings[0];
            String tempempid = strings[1];
            String tempempname = strings[2];
            String temphq = strings[3];
            String temprole = strings[4];
            String tempdate = strings[5];
            String tempplace = strings[6];
            String tempotherexpdetail = strings[7];
            String tempotherexp = strings[8];
            String tempdailyallowance = strings[9];
            String temptotal = strings[10];
            String tempremark = strings[11];
             String filename = strings[12];
Log.e("FileName",filename);
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("expense", tempExpid));
            li.add(new BasicNameValuePair("empid", tempempid));
            li.add(new BasicNameValuePair("name", tempempname));
            li.add(new BasicNameValuePair("hq", temphq));
            li.add(new BasicNameValuePair("role", temprole));
            li.add(new BasicNameValuePair("date", tempdate));
            li.add(new BasicNameValuePair("place", tempplace));
            li.add(new BasicNameValuePair("otherexpdetail", tempotherexpdetail));
            li.add(new BasicNameValuePair("otherexp", tempotherexp));
            li.add(new BasicNameValuePair("dailyallowance", tempdailyallowance));
            li.add(new BasicNameValuePair("total", temptotal));
            li.add(new BasicNameValuePair("remark", tempremark));
            li.add(new BasicNameValuePair("filename", filename));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(URL2);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                JSONArray jArray = new JSONArray(str);
                json = jArray.getJSONObject(0);

                Log.e("data", str);
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
            pd.dismiss();
            super.onPostExecute(s);
            try {
                String res=json.getString("res");
                String message=json.getString("status");
                showdialog("Expenses",message,res);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout)
        {
          //  et.clear();
            //et.commit();
            startActivity(new Intent(AddExpense.this,MainActivity.class));
            finish();
        }
        else{
            if(item.getItemId()==R.id.home)
                startActivity(new Intent(AddExpense.this,MainActivity.class));
            finish();
        }
        return true;
    }

    public void showdialog(String title,String message, String code)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddExpense.this);

        builder.setTitle(title);

        if(code.equals("true"))
        {
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i=new Intent(AddExpense.this,Expense.class);
                    startActivity(i);
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
        {
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i=new Intent(AddExpense.this,Expense.class);
                    startActivity(i);
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

}
