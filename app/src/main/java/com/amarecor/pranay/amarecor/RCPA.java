package com.amarecor.pranay.amarecor;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class RCPA extends Fragment {
    ListView listView1;
    String id;
    ArrayList<HashMap<String,String>> al=new ArrayList<>();
    String Url1="http://amarecor.com/AndroidFiles/getRcpaReport.php";
    JSONObject json;
   // ProgressDialog pd;

    public RCPA() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_rc, container, false);
        Bundle bundle =getActivity().getIntent().getExtras();
        id = bundle.getString("repid");

        listView1 = (ListView) v.findViewById(R.id.listview1);
        //listView1.setAdapter(null);


        new getSqlDetails2().execute(id);

        // Inflate the layout for this fragment
        return v;
    }



    private class getSqlDetails2 extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
         /*   pd=new ProgressDialog(getActivity());
            pd.setTitle("Loading");
            pd.setMessage("Please Wait..");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();
*/
        }

        @Override
        protected String doInBackground(String... strings) {
            String tempname = strings[0];
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("rep_id", tempname));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(Url1);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                Log.e("Emp data", str);
                JSONArray jsonarray = new JSONArray(str);
                al.clear();
                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject json = jsonarray.getJSONObject(i);
                    HashMap<String,String> hm=new HashMap<>();
                    hm.put("rcpid",json.getString("rcpid"));
                    hm.put("place",json.getString("place"));
                    hm.put("chemist",json.getString("chemist"));
                    hm.put("rcpa_date",json.getString("rcpa_date"));
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
           //d pd.dismiss();
            String from[]={"rcpid","place","chemist","rcpa_date"};
            int to[]={R.id.number,R.id.sr4,R.id.sr5,R.id.dates};
            SimpleAdapter sa1=new SimpleAdapter(getActivity(),al,R.layout.rcpaitems,from,to);
            listView1.setAdapter(sa1);

        }
    }

}
