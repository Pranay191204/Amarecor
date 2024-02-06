package com.amarecor.pranay.amarecor;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseDetails extends Fragment {
    String id;
    JSONObject json1=null;
    ProgressDialog pd;
    String expid;
    EditText e1,e2,e3,e4,e5,e6;
    String Url1="http://amarecor.com/AndroidFiles/expoutsidebasic.php";

    public ExpenseDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_expense_details, container, false);
        e1 = (EditText) V.findViewById(R.id.expid);
        e2 = (EditText) V.findViewById(R.id.emp_id);
        e3 = (EditText) V.findViewById(R.id.emp_name);
        e4 = (EditText) V.findViewById(R.id.hqworked);
        e5 = (EditText) V.findViewById(R.id.role);
        e6 = (EditText) V.findViewById(R.id.expdate);
        Bundle bundle =getActivity().getIntent().getExtras();
        id = bundle.getString("expid");
      //  Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
         new ExpenseDetails.getExData().execute(id);
        // Inflate the layout for this fragment
        return V;
    }


    private class getExData extends AsyncTask<String,Void,String> {
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
            li.add(new BasicNameValuePair("exp_id", rep_id));

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
               // expid=json1.getString("exp_id");
               // Log.e("repid",repid);
                String exp_no=json1.getString("exp_no");
                String emp_id=json1.getString("emp_id");
                String emp_name=json1.getString("employee_name");
                String hqworked=json1.getString("hq");
                String Erole=json1.getString("emp_role");
                String expdate=json1.getString("date_exp");
                String role="";
                if(Erole.equals("1"))
                {
                    role="Admin";
                } else if (Erole.equals("2")) {
                    role="Manager";
                } else if (Erole .equals("3")) {
                    role="Medical Representative";
                }
             //   e1.setText(expid);
                e1.setText(exp_no);
                e2.setText(emp_id);
                e3.setText(emp_name);
                e4.setText(hqworked);
                e5.setText(role);
                e6.setText(expdate);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}






