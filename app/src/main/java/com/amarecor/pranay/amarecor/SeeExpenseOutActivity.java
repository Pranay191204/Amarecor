package com.amarecor.pranay.amarecor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.amarecor.pranay.amarecor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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

public class SeeExpenseOutActivity extends AppCompatActivity {

    ListView listView1;
    String empid;
    String n;
    String Url="http://amarecor.com/AndroidFiles/viewoutsideExp.php";
    JSONObject json=null;
    ArrayList<HashMap<String,String>> al=new ArrayList<>();
    ProgressDialog pd;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_expense_out);

        SharedPreferences sp=getSharedPreferences("Session",MODE_PRIVATE);
        empid=sp.getString("Empid","");
        Bundle bundle = getIntent().getExtras();
        n = bundle.getString("name");
      //  Toast.makeText(getApplicationContext(),n,Toast.LENGTH_SHORT).show();
        //new GetJSON().execute(empid);
        new getExpoutData().execute(n);
        textView=(TextView)findViewById(R.id.empName);
        textView.setText(n);

        // listView1 = (ListView) findViewById(R.id.listview1);
        listView1 = (ListView) findViewById(R.id.listview1);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> hp=(HashMap<String,String>)adapterView.getAdapter().getItem(i);
                String val=hp.get("RepId");

                Intent in = new Intent(SeeExpenseOutActivity.this,OutsideExpenseView.class);
                in.putExtra("expid", val);
                startActivity(in);
               // Toast.makeText(getApplicationContext(),"Expense id : " +val, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private class getExpoutData extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            pd=new ProgressDialog(SeeExpenseOutActivity.this);
            pd.setTitle("Loading");
            pd.setMessage("Please Wait..");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String tempname = strings[0];
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("Name", tempname));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(Url);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                Log.e("Emp data", str);
                JSONArray jsonArray = new JSONArray(str);
                for (int i = 0; i < jsonArray.length(); i++) {

                    json = jsonArray.getJSONObject(i);
                    HashMap<String,String> hm=new HashMap<>();
                    hm.put("RepId",json.getString("id"));
                    hm.put("RepDate",json.getString("Date"));
                    hm.put("ExpNo",json.getString("Exp_No"));
                    al.add(hm);
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            String from[]={"RepDate","ExpNo"};
            int to[]={R.id.date,R.id.repid};
            SimpleAdapter sa=new SimpleAdapter(SeeExpenseOutActivity.this,al,R.layout.myhomeexp,from,to);
            listView1.setAdapter(sa);
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
           // et.clear();
            //et.commit();
            startActivity(new Intent(SeeExpenseOutActivity.this,MainActivity.class));
            finish();
        }
        else{
            if(item.getItemId()==R.id.home)
                startActivity(new Intent(SeeExpenseOutActivity.this,MainActivity.class));
            finish();
        }
        return true;
    }
}