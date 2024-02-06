package com.amarecor.pranay.amarecor;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Location extends Fragment {
    ListView listView2;
    String id;
    ArrayList<HashMap<String,String>> al=new ArrayList<>();
    String Url="http://amarecor.com/AndroidFiles/getfarereport.php";
    JSONObject json;

    public Location() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_location, container, false);
        Bundle bundle =getActivity().getIntent().getExtras();
        id = bundle.getString("expid");

        listView2 = (ListView) v.findViewById(R.id.listview2);
        //listView.setAdapter(null);
       // Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
        new Locationdetails().execute(id);
        // Inflate the layout for this fragment
        return v;

    }
    private class Locationdetails extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
           /* pd=new ProgressDialog(getActivity());
            pd.setTitle("Loading");
            pd.setMessage("Please Wait..");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();*/
        }
            @Override
        protected String doInBackground(String... strings) {
            String tempname = strings[0];
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("exp_id", tempname));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(Url);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                Log.e("Emp data", str);
                JSONArray jsonarray = new JSONArray(str);
                al.clear();
                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject json = jsonarray.getJSONObject(i);
                    HashMap<String,String> hm=new HashMap<>();
                    hm.put("fare_reportNo",json.getString("fare_reportNo"));
                    hm.put("form_Place",json.getString("form_Place"));
                    hm.put("end_place",json.getString("end_place"));
                    hm.put("distance",json.getString("distance"));
                    hm.put("fareTotal",json.getString("fareTotal"));

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
            //   pd.dismiss();
            String from[]={"fare_reportNo","form_Place","end_place","distance","fareTotal"};
            int to[]={R.id.number,R.id.autoCompleteTextView,R.id.autoCompleteTextViewto,R.id.distance,R.id.fare};
            SimpleAdapter sa=new SimpleAdapter(getActivity(),al,R.layout.locationitem,from,to);
            listView2.setAdapter(sa);

        }
    }
}