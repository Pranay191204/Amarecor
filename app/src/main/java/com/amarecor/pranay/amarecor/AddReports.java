package com.amarecor.pranay.amarecor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amarecor.pranay.amarecor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
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

public class AddReports extends AppCompatActivity {

    Calendar c=Calendar.getInstance();
    int year=c.get(Calendar.YEAR);
    int month=c.get(Calendar.MONTH);
    int date=c.get(Calendar.DAY_OF_MONTH);
    int hours= c.get(Calendar.HOUR_OF_DAY);
    int minutes= c.get(Calendar.MINUTE);
    EditText starttime,workedwith,datet,endtime,dates,et1,et2,et3,hqworked,workedarea,sampname,number,sr1,sr2,sr3,sr4,sr5,vt,giftn;
    SharedPreferences sp;
    SharedPreferences.Editor et;
    Button b2,b3;
    RadioGroup rg1,rg2;
    ImageButton btnAdd,btnAdd1;

    String URL = "http://amarecor.com/AndroidFiles/getEmpData.php";
    String URL2 ="http://amarecor.com/AndroidFiles/GetAllHQData.php";
    String URL3 ="http://amarecor.com/AndroidFiles/insertonReportnew.php";
    String URL4 ="http://amarecor.com/AndroidFiles/insertDoctor.php";
    String URL5 ="http://amarecor.com/AndroidFiles/Rcpadetails.php";

    String empid;
    String emprole;
    String hqid;
    // String rep_id;
    ProgressDialog pd;
    JSONObject json = null,jo=null;
    String[] arname;
    String[] Warea;
    String[] docname;
    String[] docspec;
    String[] arHq;
    String[] chemname;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reports);
        SharedPreferences sp=getSharedPreferences("Session",MODE_PRIVATE);
        empid=sp.getString("Empid","");
        emprole=sp.getString("EmpRole","");
        hqid=sp.getString("EmpHQid","");
       // Toast.makeText(getApplicationContext(),hqid.toString(),Toast.LENGTH_LONG).show();

        starttime = (EditText) findViewById(R.id.starttime);
        endtime = (EditText) findViewById(R.id.endtime);
        workedwith = (EditText) findViewById(R.id.workedwith);
        workedarea = (EditText) findViewById(R.id.workedarea);
        hqworked = (EditText) findViewById(R.id.hqworked);
        datet = (EditText) findViewById(R.id.date);
        dates = (EditText) findViewById(R.id.dates);
        vt=(EditText)findViewById(R.id.visittime);
        et1 = (EditText) findViewById(R.id.reportid);
        et2 = (EditText) findViewById(R.id.editText17);
        et3 = (EditText) findViewById(R.id.editText21);

        btnAdd=(ImageButton)findViewById(R.id.btnAdd);
        btnAdd1=(ImageButton)findViewById(R.id.btnAdd1);
        b2=(Button)findViewById(R.id.button5);
        b3=(Button)findViewById(R.id.button6);

        new getData().execute(empid);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout linearLayoutForm = (LinearLayout)findViewById(R.id.linearLayoutForm);
                final LinearLayout newView = (LinearLayout)getLayoutInflater().inflate(R.layout.docdetails, null);
                newView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ImageButton btnRemove = (ImageButton) newView.findViewById(R.id.btnRemove);
                btnRemove.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        linearLayoutForm.removeView(newView);
                    }
                });
                final EditText e1=(EditText)newView.findViewById(R.id.sr1);
                final EditText e2=(EditText)newView.findViewById(R.id.sr2);
                final EditText e3=(EditText)newView.findViewById(R.id.sr3);

                e1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder bu = new AlertDialog.Builder(AddReports.this);
                        bu.setTitle("Select Visit Place");
                        bu.setCancelable(false);
                        bu.setSingleChoiceItems(Warea, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                e1.setText(Warea[i]);
                                // new getWorkedwith().execute(empid,Warea[i]);
                                dialogInterface.dismiss();
                            }
                        });
                        bu.show();
                    }
                });
                e2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder bu = new AlertDialog.Builder(AddReports.this);
                        bu.setTitle("Select Doctor Name");
                        bu.setCancelable(false);
                        bu.setSingleChoiceItems(docname, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                e2.setText(docname[i]);
                                // new getWorkedwith().execute(empid,Warea[i]);
                                dialogInterface.dismiss();
                            }
                        });
                        bu.show();
                    }
                });
                e3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder bu = new AlertDialog.Builder(AddReports.this);
                        bu.setTitle("Select Doctor Specialization");
                        bu.setCancelable(false);
                        bu.setSingleChoiceItems(docspec, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                e3.setText(docspec[i]);
                                // new getWorkedwith().execute(empid,Warea[i]);
                                dialogInterface.dismiss();
                            }
                        });
                        bu.show();
                    }
                });
                final EditText vt = (EditText) newView.findViewById(R.id.visittime);
                vt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog tp = new TimePickerDialog(AddReports.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                vt.setText(hourOfDay%12 + ":" + minute + " " + ((hourOfDay>=12) ? "PM" : "AM"));

                            }
                        }, hours, minutes,false);
                        tp.setTitle("Choose Time");
                        tp.show();
                    }
                });

                linearLayoutForm.addView(newView);
            }
        });

        btnAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout linearLayoutForm1 = (LinearLayout) findViewById(R.id.linearLayoutForm1);
                final LinearLayout newView1 = (LinearLayout) getLayoutInflater().inflate(R.layout.rcpadetail, null);
                newView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ImageButton btnRemove1 = (ImageButton) newView1.findViewById(R.id.btnRemove1);
                btnRemove1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        linearLayoutForm1.removeView(newView1);
                    }
                });
                final EditText e4 = (EditText) newView1.findViewById(R.id.sr4);
                final EditText e5 = (EditText) newView1.findViewById(R.id.sr5);

                e4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder bu = new AlertDialog.Builder(AddReports.this);
                        bu.setTitle("Select Visit Place");
                        bu.setCancelable(false);
                        bu.setSingleChoiceItems(Warea, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                e4.setText(Warea[i]);
                                // new getWorkedwith().execute(empid,Warea[i]);
                                dialogInterface.dismiss();
                            }
                        });
                        bu.show();
                    }
                });
                e5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder bu = new AlertDialog.Builder(AddReports.this);
                        bu.setTitle("Select Name of Chemist");
                        bu.setCancelable(false);
                        bu.setSingleChoiceItems(chemname, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                e5.setText(chemname[i]);
                                // new getWorkedwith().execute(empid,Warea[i]);
                                dialogInterface.dismiss();
                            }
                        });
                        bu.show();
                    }
                });
                final EditText dates = (EditText) newView1.findViewById(R.id.dates);
                dates.setOnClickListener(new View.OnClickListener() {
                    @Override
                        public void onClick (View view){
                        TimePickerDialog tp = new TimePickerDialog(AddReports.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                dates.setText(hourOfDay%12 + ":" + minute + " " + ((hourOfDay>=12) ? "PM" : "AM"));

                            }
                        }, hours, minutes,false);
                        tp.setTitle("Choose Time");
                        tp.show();
                    }
                });

                linearLayoutForm1.addView(newView1);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reportid = et1.getText().toString();
                String empid= et2.getText().toString();
                String empname= et3.getText().toString();
                String hqworked1= hqworked.getText().toString();
                String workedwith1= workedwith.getText().toString();
                String workedarea1= workedarea.getText().toString();
                String starttime1= starttime.getText().toString();
                String endtime1= endtime.getText().toString();
                String date= datet.getText().toString();
            new Adddata().execute(reportid,empid,empname,hqworked1,workedwith1,workedarea1,starttime1,endtime1,date);
                 // Toast.makeText(AddReports.this, reportid, Toast.LENGTH_SHORT).show();

            }
        });



        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddReports.this,AddReports.class);
                startActivity(i);
                finish();
            }
        });
        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog tp = new TimePickerDialog(AddReports.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        starttime.setText(hourOfDay%12 + ":" + minute + " " + ((hourOfDay>=12) ? "PM" : "AM"));

                    }
                }, hours, minutes, false);
                tp.setTitle("Choose Time");
                tp.show();
            }
        });

        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog tp = new TimePickerDialog(AddReports.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endtime.setText(hourOfDay%12 + ":" + minute + " " + ((hourOfDay>=12) ? "PM" : "AM"));

                    }
                }, hours, minutes, false);
                tp.setTitle("Choose Time");
                tp.show();
            }
        });
        hqworked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder bu = new AlertDialog.Builder(AddReports.this);
                bu.setTitle("Select HQ Worked with");
                bu.setCancelable(false);
                bu.setSingleChoiceItems(arHq, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hqworked.setText(arHq[i]);
                        new getWorkedwith().execute(empid,arHq[i]);
                        dialogInterface.dismiss();
                    }
                });
                bu.show();
            }
        });

        workedwith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final  ArrayList<String> ar=new ArrayList<>();

                AlertDialog.Builder bu = new AlertDialog.Builder(AddReports.this);
                bu.setTitle("Select Employee");
                bu.setCancelable(false);
                bu.setMultiChoiceItems(arname, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            ar.add(arname[i]);
                        }
                        else
                        {
                            ar.remove(arname[i]);
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
                        workedwith.setText("");

                        workedwith.setText(temp);

                        dialog.dismiss();
                    }
                });
                AlertDialog a = bu.create();
                a.show();
            }
        });

        workedarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> ar = new ArrayList<>();

                AlertDialog.Builder bu = new AlertDialog.Builder(AddReports.this);
                bu.setTitle("Select Area");
                bu.setCancelable(false);
                bu.setMultiChoiceItems(Warea, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            ar.add(Warea[i]);
                        } else {
                            ar.remove(Warea[i]);
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
                        workedarea.setText("");

                        workedarea.setText(temp);

                        dialog.dismiss();
                    }
                });
                AlertDialog a = bu.create();
                a.show();
            }
        });

        datet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp = new DatePickerDialog(AddReports.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datet.setText(dayOfMonth + "/" + (month+1) + "/" + year);

                    }
                }, year, month, date);
                dp.setTitle("Choose Date");
                dp.show();
            }
        });

    }

    class getData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pd=new ProgressDialog(AddReports.this);
            pd.setTitle("");
            pd.setMessage("");
            pd.setIndeterminate(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String tempid = strings[0];
            Log.e("ID",tempid);
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("userid", tempid));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(URL);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                Log.e("data", str);
                JSONArray jArray = new JSONArray(str);

                json = jArray.getJSONObject(0);
                JSONArray jEmpData=json.getJSONArray("EmpData");
                JSONArray jHQ=json.getJSONArray("HQ");
                arHq=new String[jHQ.length()];
                for(int i=0;i<jHQ.length();i++) {
                    json = jHQ.getJSONObject(i);
                    arHq[i]=json.getString("HQName");
                    //  workedwithar.add(json.getString("Name"));
                }
                jo=jEmpData.getJSONObject(0);

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
                String Res = jo.getString("res");
                String EmpName = jo.getString("name");
                String Empid = jo.getString("Empid");
                String report = jo.getString("report");


                if (Res.equals("true")) {
                    et1.setText(report);
                    et2.setText(Empid);
                    et3.setText(EmpName);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    }
    class  getWorkedwith extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String tempid = strings[0];
            //Log.e("ID",tempid);
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("userid", tempid));
            li.add(new BasicNameValuePair("hqname", strings[1]));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(URL2);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                // Log.e("data", str);
                JSONArray jArray = new JSONArray(str);
                json = jArray.getJSONObject(0);
                JSONArray jWorkedWith=json.getJSONArray("WorkedWith");
                JSONArray jVisitPlace=json.getJSONArray("VisitPlace");
                JSONArray jDocName=json.getJSONArray("DocName");
                JSONArray jDocSpec=json.getJSONArray("DocSpec");
                JSONArray jChemName=json.getJSONArray("ChemName");
                Log.e("data1", jVisitPlace.toString());
                Log.e("data2", jDocName.toString());
                Log.e("data3", jDocSpec.toString());
                arname=new String[jWorkedWith.length()];

                for(int i=0;i<jWorkedWith.length();i++)
                {
                    JSONObject jo=jWorkedWith.getJSONObject(i);
                    Log.e("data", jo.toString());
                    arname[i]=jo.getString("Name");

                }
                Warea=new String[jVisitPlace.length()];

                for(int i=0;i<jVisitPlace.length();i++)
                {
                    JSONObject jo=jVisitPlace.getJSONObject(i);
                    Log.e("data", jo.toString());
                    Warea[i]=jo.getString("cityName");

                }
                docname=new String[jDocName.length()];

                for(int i=0;i<jDocName.length();i++)
                {
                    JSONObject jo=jDocName.getJSONObject(i);
                    Log.e("data", jo.toString());
                    docname[i]=jo.getString("drName");

                }
                docspec=new String[jDocSpec.length()];

                for(int i=0;i<jDocSpec.length();i++)
                {
                    JSONObject jo=jDocSpec.getJSONObject(i);
                    Log.e("data", jo.toString());
                    docspec[i]=jo.getString("drSpec");

                }
                chemname=new String[jChemName.length()];

                for(int i=0;i<jChemName.length();i++)
                {
                    JSONObject jo=jChemName.getJSONObject(i);
                    Log.e("data", jo.toString());
                    chemname[i]=jo.getString("chemName");

                }
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
            et.clear();
            et.commit();
            startActivity(new Intent(AddReports.this,MainActivity.class));
            finish();
        }
        else{
            if(item.getItemId()==R.id.home)
                startActivity(new Intent(AddReports.this,MainActivity.class));
            finish();
        }

        return true;

    }

    private class Adddata extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {

            String tempreportid=strings[0];
            String tempempid=strings[1];
            String tempempname=strings[2];
            String temphqworked1=strings[3];
            String tempworkedwith1=strings[4];
            String tempworkedarea1=strings[5];
            String tempstarttime1=strings[6];
            String tempendtime1=strings[7];
            String tempdate=strings[8];
            //String temphqid=strings[9];
            //String temprole=strings[10];

            List<NameValuePair> li=new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("report",tempreportid));
            li.add(new BasicNameValuePair("empid",tempempid));
            li.add(new BasicNameValuePair("name",tempempname));
            li.add(new BasicNameValuePair("hqwork",temphqworked1));
            li.add(new BasicNameValuePair("workwith",tempworkedwith1));
            li.add(new BasicNameValuePair("workarea",tempworkedarea1));
            li.add(new BasicNameValuePair("starttime",tempstarttime1));
            li.add(new BasicNameValuePair("endtime",tempendtime1));
            li.add(new BasicNameValuePair("date",tempdate));
            li.add(new BasicNameValuePair("HqId",hqid));
         //   li.add(new BasicNameValuePair("hqid",temphqid));
            li.add(new BasicNameValuePair("Role",emprole));

            try {
                HttpClient hc=new DefaultHttpClient();
                HttpPost hp= new HttpPost(URL3);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr=hc.execute(hp);
                String str= EntityUtils.toString(hr.getEntity(),"UTF-8");
                JSONArray jArray = new JSONArray(str);
                json = jArray.getJSONObject(0);

                Log.e("data",str);
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
            try {
                id=json.getString("id");
                String res  = json.getString("res");
                String Message = json.getString("status");
              //  Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();

                if (res.equals("true")) {
                    String reportid = et1.getText().toString();
                    String hqworked1= hqworked.getText().toString();

                    LinearLayout docdetails = (LinearLayout)findViewById(R.id.linearLayoutForm);
                    for (int i = 0; i < docdetails.getChildCount(); i++)
                    {
                        LinearLayout innerLayout = (LinearLayout) docdetails.getChildAt(i);

                        EditText ed1 = (EditText) innerLayout.findViewById(R.id.sr1);
                        EditText ed2 = (EditText) innerLayout.findViewById(R.id.sr2);
                        EditText ed3 = (EditText) innerLayout.findViewById(R.id.sr3);
                        EditText ed4 = (EditText) innerLayout.findViewById(R.id.product);
                        EditText ed5 = (EditText) innerLayout.findViewById(R.id.visittime);
                        EditText ed6 = (EditText) innerLayout.findViewById(R.id.sampname);
                        EditText ed7= (EditText) innerLayout.findViewById(R.id.giftnam);
                        RadioGroup rg1=(RadioGroup)innerLayout.findViewById(R.id.rg1);
                        RadioGroup rg2=(RadioGroup)innerLayout.findViewById(R.id.rg2);
                        int id1=rg1.getCheckedRadioButtonId();
                        int id2=rg2.getCheckedRadioButtonId();
                        RadioButton rd1=(RadioButton)innerLayout.findViewById(id1);
                        RadioButton rd2=(RadioButton)innerLayout.findViewById(id2);
                        String place= ed1.getText().toString();
                        String docname= ed2.getText().toString();
                        String doctype= ed3.getText().toString();
                        String product= ed4.getText().toString();
                        String visittime= ed5.getText().toString();
                        String samplename= ed6.getText().toString();
                        String giftname= ed7.getText().toString();
                        String sgiven= rd1.getText().toString();
                        String ggiven= rd2.getText().toString();
                        new AddDocDetails().execute(place,docname,doctype,product,visittime,samplename,giftname,ggiven,sgiven,empid,emprole,reportid,hqworked1,i+1+"");
                        // Toast.makeText(AddReports.this, id, Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(AddReports.this, "Report Not Added.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }


    }
    public void showdialog(String title,String message, String code)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddReports.this);

        builder.setTitle(title);

        if(code.equals("true"))
        {
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i=new Intent(AddReports.this,Report.class);
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
                    Intent i=new Intent(AddReports.this,Report.class);
                    startActivity(i);
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private class AddDocDetails extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("place", strings[0]));
            li.add(new BasicNameValuePair("docname", strings[1]));
            li.add(new BasicNameValuePair("doctype", strings[2]));
            li.add(new BasicNameValuePair("product", strings[3]));
            li.add(new BasicNameValuePair("visittime", strings[4]));
            li.add(new BasicNameValuePair("samplename", strings[5]));
            li.add(new BasicNameValuePair("giftname", strings[6]));
            li.add(new BasicNameValuePair("ggiven", strings[7]));
            li.add(new BasicNameValuePair("sgiven", strings[8]));
            li.add(new BasicNameValuePair("empid", strings[9]));
            li.add(new BasicNameValuePair("emprole", strings[10]));
            li.add(new BasicNameValuePair("reportid", id));
            li.add(new BasicNameValuePair("hqworked1", strings[12]));
            li.add(new BasicNameValuePair("drsrno", strings[13]));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(URL4);
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
            super.onPostExecute(s);
            String res  = null;
            try {
                res = json.getString("res");
                String Message = json.getString("status");
                //  Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();

                if (res.equals("true")) {
                    LinearLayout rcpadetails = (LinearLayout) findViewById(R.id.linearLayoutForm1);
                    for (int i = 0; i < rcpadetails.getChildCount(); i++) {
                        LinearLayout innerLayout = (LinearLayout) rcpadetails.getChildAt(i);

                        EditText sr4 = (EditText) findViewById(R.id.sr4);
                        EditText sr5 = (EditText) findViewById(R.id.sr5);
                        EditText number = (EditText) findViewById(R.id.number);
                        EditText dates = (EditText) findViewById(R.id.dates);
                        String no = number.getText().toString();
                        String place = sr4.getText().toString();
                        String nameofchem = sr5.getText().toString();
                        String product = dates.getText().toString();
                        new RcpaDetails().execute(no, place, nameofchem, product, empid, emprole, hqid, i + 1 + "");
                        //   Toast.makeText(AddReports.this, , Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

        private class RcpaDetails extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd=new ProgressDialog(AddReports.this);
                pd.setTitle("Adding Report");
                pd.setMessage("Please Wait..");
                pd.setIndeterminate(false);
                pd.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                List<NameValuePair> li = new ArrayList<NameValuePair>();
                li.add(new BasicNameValuePair("no", strings[0]));
                li.add(new BasicNameValuePair("place", strings[1]));
                li.add(new BasicNameValuePair("nameofchem", strings[2]));
                li.add(new BasicNameValuePair("product", strings[3]));
                li.add(new BasicNameValuePair("reportid", id));
                li.add(new BasicNameValuePair("empid", strings[4]));
                li.add(new BasicNameValuePair("emprole", strings[5]));
                li.add(new BasicNameValuePair("hqid", strings[6]));

                try {
                    HttpClient hc = new DefaultHttpClient();
                    HttpPost hp = new HttpPost(URL5);
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
                super.onPostExecute(s);
                pd.dismiss();
                String res  = null;
                try {
                    res = json.getString("res");
                    String Message = json.getString("status");
                    //  Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();
               if(res.equals("true"))
               {
                   showdialog("Successful",Message,res);
               }
               else
               {
                   showdialog("Failed",Message,res);
               }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                }
        }
}