package com.amarecor.pranay.amarecor;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Basic extends Fragment {
    String id;
    JSONObject json1=null;
    ProgressDialog pd;
    EditText e1,e2,e3,e4,e5,e6,e7,e8,e9;
    String repid;
    String Url1="http://amarecor.com/AndroidFiles/getreportBasic.php";
    public Basic() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V=inflater.inflate(R.layout.fragment_basic, container, false);
        e1=(EditText)V.findViewById(R.id.reportid);
        e2=(EditText)V.findViewById(R.id.editText17);
        e3=(EditText)V.findViewById(R.id.editText21);
        e4=(EditText)V.findViewById(R.id.hqworked);
        e5=(EditText)V.findViewById(R.id.workedwith);
        e6=(EditText)V.findViewById(R.id.workedarea);
        e7=(EditText)V.findViewById(R.id.starttime);
        e8=(EditText)V.findViewById(R.id.endtime);
        e9=(EditText)V.findViewById(R.id.date);

        Bundle bundle =getActivity().getIntent().getExtras();
        id = bundle.getString("repid");
        new getRepData().execute(id);
        // Inflate the layout for this fragment
        return V;
    }

    private class getRepData extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            pd=new ProgressDialog(getActivity());
            pd.setTitle("Loading");
            pd.setMessage("Please Wait..");
            pd.setIndeterminate(false);
           pd.setCancelable(false);
           pd.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String rep_id = strings[0];
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("rep_id", rep_id));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(Url1);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                Log.e("Emp data11", str);
                JSONArray jsonArray1 = new JSONArray(str);
                json1 =jsonArray1.getJSONObject(0);

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
                repid=json1.getString("rep_srno");
                Log.e("repid",repid);
                String emp_id=json1.getString("emp_id");
                String employee_name=json1.getString("employee_name");
                String area_name=json1.getString("area_name");
                String worked=json1.getString("worked");
                String datereport1=json1.getString("datereport1");
                String start_time=json1.getString("start_time");
                String end_time=json1.getString("end_time");
                String select_hq=json1.getString("select_hq");

                e1.setText(repid);
                e2.setText(emp_id);
                e3.setText(employee_name);
                e4.setText(select_hq);
                e5.setText(worked);
                e6.setText(area_name);
                e7.setText(start_time);
                e8.setText(end_time);
                e9.setText(datereport1);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
