package com.amarecor.pranay.amarecor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amarecor.pranay.amarecor.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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

public class AddExpenseHq extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private AutoCompleteTextView mAutocompleteTextViewto;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    ImageButton btnAdd1;
    Button b2, b3, b4;
    EditText et1, et2, et3, hqdat, dailyall, emrole, edttxt44, hqworked, place, otherexpdetail,otherexprs, dailyallowance, finaltotal,remark;
    TextView tv;
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int date = c.get(Calendar.DAY_OF_MONTH);
    SharedPreferences sp;
    SharedPreferences.Editor et;
    String empid;
    String hqid;
    String emprole;
    String id;
    Intent intent;
    String[] places;
    ProgressDialog pd;
    JSONObject json = null;
    String[] arname;
    String[] arHq;
    String URL = "http://amarecor.com/AndroidFiles/getExpData.php";
    String URL_PLACE = "http://amarecor.com/AndroidFiles/getPlaces.php";
    String URL3 = "http://amarecor.com/AndroidFiles/HQlist.php";
   // String URL2 = "http://amarecor.com/AndroidFiles/getplacename.php";
    String URL4 = "http://amarecor.com/AndroidFiles/addexpoutside.php";
    String URL5 = "http://amarecor.com/AndroidFiles/addlocation.php";
    String UPLOAD_URL = "http://amarecor.com/AndroidFiles/addfilehq.php";
   // String tempfrom;
    Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense_hq);
        mGoogleApiClient = new GoogleApiClient.Builder(AddExpenseHq.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mPlaceArrayAdapter = new PlaceArrayAdapter(AddExpenseHq.this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);

        SharedPreferences sp = getSharedPreferences("Session", MODE_PRIVATE);
        empid = sp.getString("Empid", "");
        hqid = sp.getString("EmpHQid", "");
     //   Toast.makeText(this, hqid , Toast.LENGTH_SHORT).show();

        final String Erole = sp.getString("EmpRole", "");
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
        String editText69 = "";
        if (Erole.equals("2")) {
            editText69 = "700";
        } else if (Erole.equals("3")) {
            editText69 = "400";
        }
        dailyallowance = (EditText) findViewById(R.id.editText69);
        dailyallowance.setText(editText69);

        et1 = (EditText) findViewById(R.id.expid);
        et2 = (EditText) findViewById(R.id.emp_id);
        et3 = (EditText) findViewById(R.id.emp_name);
        hqdat = (EditText) findViewById(R.id.expdate);
        hqworked = (EditText) findViewById(R.id.hqworked);
        edttxt44= (EditText) findViewById(R.id.role);
        btnAdd1 = (ImageButton) findViewById(R.id.btnAdd1);
        otherexpdetail=(EditText) findViewById(R.id.otherexpdetail);
        otherexprs=(EditText) findViewById(R.id.otherexprs);
        finaltotal=(EditText) findViewById(R.id.finaltotal);
        place= (EditText) findViewById(R.id.place);
        tv = (TextView) findViewById(R.id.files);
        remark=(EditText)findViewById(R.id.remark);
        b2 = (Button) findViewById(R.id.button5);
        b3 = (Button) findViewById(R.id.button6);
        b4 = (Button) findViewById(R.id.button8);

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 123);
            }
        });
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final  ArrayList<String> ar=new ArrayList<>();

                AlertDialog.Builder bu = new AlertDialog.Builder(AddExpenseHq.this);
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
                        place.setText("");

                        place.setText(temp);

                        dialog.dismiss();
                    }
                });
                AlertDialog a = bu.create();
                a.show();
            }
        });

        hqdat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp = new DatePickerDialog(AddExpenseHq.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        hqdat.setText(dayOfMonth + "/" + (month+1) + "/" + year);

                    }
                }, year, month, date);
                dp.setTitle("Choose Date");
                dp.show();
            }
        });
       new getData().execute(empid);
         new getHQ().execute();

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expenseid = et1.getText().toString();
                String empid = et2.getText().toString();
                String empname = et3.getText().toString();
                String emprole = emrole.getText().toString();
                String hqworked1 = hqworked.getText().toString();
                String date = hqdat.getText().toString();
                String place1 = place.getText().toString();
                String otherexpdetail1 = otherexpdetail.getText().toString();
                String otherexprs1 = otherexprs.getText().toString();
                String dailyallowance1 = dailyallowance.getText().toString();
                String finaltotal1 = finaltotal.getText().toString();
                String remark1 = remark.getText().toString();

                String file="";
                if(filePath==null)
                {
                    // Toast.makeText(AddExpense.this, "Please Enter File..", Toast.LENGTH_SHORT).show();
                }else {
                    uploadMultipart();
                    String path = FilePath.getPath(AddExpenseHq.this, filePath);
                    String filename = path.substring(path.lastIndexOf("/")+1);

                    if (filename.indexOf(".") > 0) {
                        file = filename.substring(0, filename.lastIndexOf("."));
                    } else {
                        file =  filename;
                    }

                }//getting the actual path of the image
                new Adddata().execute(expenseid, empid, empname, Erole, hqworked1, date, place1, otherexpdetail1, otherexprs1, dailyallowance1,finaltotal1,remark1,file);


            }
        });

        btnAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout linearLayoutForm3 = (LinearLayout) findViewById(R.id.linearLayoutForm3);
                final LinearLayout newView2 = (LinearLayout) getLayoutInflater().inflate(R.layout.locationdetail, null);
                newView2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ImageButton btnRemove2 = (ImageButton) newView2.findViewById(R.id.btnRemove2);
                btnRemove2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        linearLayoutForm3.removeView(newView2);
                    }
                });
                mAutocompleteTextView = (AutoCompleteTextView) newView2.findViewById(R.id.autoCompleteTextView);
                mAutocompleteTextView.setThreshold(1);
                mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
                mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

                mAutocompleteTextViewto = (AutoCompleteTextView) newView2.findViewById(R.id.autoCompleteTextViewto);
                mAutocompleteTextViewto.setThreshold(1);
                mAutocompleteTextViewto.setOnItemClickListener(mAutocompleteClickListener);
                mAutocompleteTextViewto.setAdapter(mPlaceArrayAdapter);
                final EditText dist = (EditText) newView2.findViewById(R.id.distance);
                final EditText fare = (EditText) newView2.findViewById(R.id.fare);

                mAutocompleteTextViewto.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String from = mAutocompleteTextView.getText().toString();
                        String to = mAutocompleteTextViewto.getText().toString();
                        Geocoder coder = new Geocoder(AddExpenseHq.this);
                        List<Address> address;
                        try {

                            address = coder.getFromLocationName(from, 5);
                            if (address == null) {
                                Log.d("", "############Address not correct #########");
                            }
                            Address location = address.get(0);
                            double lat = location.getLatitude();
                            double log = location.getLongitude();
                            //Toast.makeText(AddExpenseHq.this, lat + "\n" + log, Toast.LENGTH_SHORT).show();

                            List<Address> address1;

                            address1 = coder.getFromLocationName(to, 5);
                            if (address1 == null) {
                                Log.d("", "############Address not correct #########");
                            }
                            Address location1 = address1.get(0);
                            double lat1 = location1.getLatitude();
                            double log1 = location1.getLongitude();
                            // Toast.makeText(AddExpenseHq.this, lat1 + "\n" + log1, Toast.LENGTH_SHORT).show();
/*
                            double distance=distance(lat,log,lat1,log1);
                            dist.setText(distance+"");
*/
                         Location loc1 = new Location("");
                            loc1.setLatitude(lat);
                            loc1.setLongitude(log);

                            Location loc2 = new Location("");
                            loc2.setLatitude(lat1);


                                    loc2.setLongitude(log1);

                            float distanceInMeters = loc1.distanceTo(loc2);
                            dist.setText((distanceInMeters / 1000) + "");
                            fare.setText(((distanceInMeters / 1000) * 2) + "");
                            String DA=dailyall.getText().toString();
                            if(DA.isEmpty())
                            {
                                DA="0.0";
                            }
                            String Exp=otherexprs.getText().toString();
                            double totfare=Double.parseDouble(fare.getText().toString());


                            double totAl=Double.parseDouble(DA)+Double.parseDouble(Exp)+totfare;
                            finaltotal.setText(totAl+"");

                        } catch (Exception e) {
                            Log.d("", "MY_ERROR : ############Address Not Found");
                        }
                    }
                });

                mAutocompleteTextView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String from = mAutocompleteTextView.getText().toString();
                        String to = mAutocompleteTextViewto.getText().toString();
                        Geocoder coder = new Geocoder(AddExpenseHq.this);
                        List<Address> address;
                        try {

                            address = coder.getFromLocationName(from, 5);
                            if (address == null) {
                                Log.d("", "############Address not correct #########");
                            }
                            Address location = address.get(0);
                            double lat = location.getLatitude();
                            double log = location.getLongitude();
                            //  Toast.makeText(AddExpenseHq.this, lat + "\n" + log, Toast.LENGTH_SHORT).show();

                            List<Address> address1;

                            address1 = coder.getFromLocationName(to, 5);
                            if (address1 == null) {
                                Log.d("", "############Address not correct #########");
                            }
                            Address location1 = address1.get(0);
                            double lat1 = location1.getLatitude();
                            double log1 = location1.getLongitude();
//                          Toast.makeText(AddExpenseHq.this, lat1 + "\n" + log1, Toast.LENGTH_SHORT).show();

                            Location loc1 = new Location("");
                            loc1.setLatitude(lat);
                            loc1.setLongitude(log);

                            Location loc2 = new Location("");
                            loc2.setLatitude(lat1);
                            loc2.setLongitude(log1);

                            float distanceInMeters = loc1.distanceTo(loc2);
                            dist.setText((distanceInMeters / 1000) + "");
                            fare.setText(((distanceInMeters / 1000) * 2) + "");
                            String DA=dailyall.getText().toString();
                            if(DA.isEmpty())
                            {
                                DA="0.0";
                            }
                            String Exp=otherexprs.getText().toString();
                            double totfare=Double.parseDouble(fare.getText().toString());


                            double totAl=Double.parseDouble(DA)+Double.parseDouble(Exp)+totfare;
                            finaltotal.setText(totAl+"");
                        } catch (Exception e) {
                            Log.d("", "MY_ERROR : ############Address Not Found");
                        }
                    }
                });
                linearLayoutForm3.addView(newView2);
            }
        });
        hqworked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder bu = new AlertDialog.Builder(AddExpenseHq.this);
                bu.setTitle("Select HQ");
                bu.setCancelable(false);
                bu.setSingleChoiceItems(arHq, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hqworked.setText(arHq[i]);
                        new HQWorkedWith().execute(empid,arHq[i]);
                        dialogInterface.dismiss();
                    }
                });
                bu.show();
            }
        });
        otherexprs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String DA=dailyallowance.getText().toString();
                if(DA.isEmpty())
                {
                    DA="0.0";
                }
                String Exp=otherexprs.getText().toString();
                if(Exp.isEmpty())
                {
                    Exp="0.0";
                }
                LinearLayout locationdetail = (LinearLayout) findViewById(R.id.linearLayoutForm3);
                double totfare=0.0;
                for (int i = 0; i < locationdetail.getChildCount(); i++)
                {
                    LinearLayout innerLayout = (LinearLayout) locationdetail.getChildAt(i);
                    EditText ed3 =(EditText)innerLayout.findViewById(R.id.fare);
                    String tempfare = ed3.getText().toString();
                   // Toast.makeText(AddExpenseHq.this,tempfare, Toast.LENGTH_SHORT).show();
                     totfare+=Double.parseDouble(tempfare);
                }
                String regex = "\\d+";
                if(otherexprs.getText().toString().matches(regex)){

                    double totAl=Double.parseDouble(DA)+Double.parseDouble(Exp)+totfare;
                    finaltotal.setText(totAl+"");
                }
                else{
                    if(!otherexprs.getText().toString().isEmpty()) {
                        Toast.makeText(AddExpenseHq.this, "Enter the values in numbers", Toast.LENGTH_LONG).show();
                    }
                }
                if(otherexprs.getText().toString().isEmpty() && !finaltotal.getText().toString().isEmpty())
                {
                    Exp="0.0";
                    double totAl=Double.parseDouble(DA)+Double.parseDouble(Exp)+totfare;
                    finaltotal.setText(totAl+"");

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
                      //  .setNotificationConfig(new UploadNotificationConfig())
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

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i( "Selected: " , item.description+"");
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i("Fetching details: " , item.placeId+"");
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
        }
    };
    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i("", "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("", "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e("", "Google Places API connection suspended.");
    }
    private class getData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(AddExpenseHq.this);
            pd.setTitle("Loading..");
            pd.setMessage("Please Wait...");
            pd.setIndeterminate(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String tempid = strings[0];
            Log.e("ID", tempid);
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("userid", tempid));
           // li.add(new BasicNameValuePair("hqname", strings[1]));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(URL);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                Log.e("data", str);

                JSONArray jArray = new JSONArray(str);

                JSONObject jo= jArray.getJSONObject(0);
                JSONArray jsonarr=jo.getJSONArray("EmpData");
                json=jsonarr.getJSONObject(0);

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
                String Res = json.getString("res");
                String EmpName = json.getString("name");
                String Empid = json.getString("Empid");
                String expid = json.getString("expno");
               // String hqid = json.getString("hqName");

                if (Res.equals("true")) {
                    et2.setText(Empid);
                    et1.setText(expid);
                    et3.setText(EmpName);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    }
    private class HQWorkedWith extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String tempid = strings[0];
            Log.e("ID", tempid);
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("userid", tempid));
            li.add(new BasicNameValuePair("hqname", strings[1]));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(URL_PLACE);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                Log.e("data", str);

                JSONArray jArray = new JSONArray(str);
                places=new String[jArray.length()];
for(int i=0;i<jArray.length();i++)
{
    json=jArray.getJSONObject(i);
    places[i]=json.getString("cityName");
}
                json=jArray.getJSONObject(0);

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
        private class getHQ extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {

                try {
                    HttpClient hc = new DefaultHttpClient();
                    HttpPost hp = new HttpPost(URL3);
                    HttpResponse hr = hc.execute(hp);
                    String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                    Log.e("data", str);
                    JSONArray jArray = new JSONArray(str);
                    arHq = new String[jArray.length()];
                    for (int i = 0; i < jArray.length(); i++) {
                        json = jArray.getJSONObject(i);
                        arHq[i] = json.getString("HQName");
                        //  workedwithar.add(json.getString("Name"));
                    }
                    Log.d("this is my array", "arr: " + Arrays.toString(arname));
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
            startActivity(new Intent(AddExpenseHq.this,MainActivity.class));
            finish();
        }
        else{
            if(item.getItemId()==R.id.home)
                startActivity(new Intent(AddExpenseHq.this,MainActivity.class));
            finish();
        }
        return true;

    }

    private class Adddata extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {

            String tempexpenseid = strings[0];
            String tempempid = strings[1];
            String tempempname = strings[2];
            String temphqworked1 = strings[4];
            String tempemprole = strings[3];
            String tempdate = strings[5];
            String tempplace = strings[6];
            String tempotherexpdetail = strings[7];
            String tempotherexp = strings[8];
            String tempdailyallow = strings[9];
            String tempfinaltotal = strings[10];
            String tempremark = strings[11];
            String filename = strings[12];
           // String tempchoosefile = strings[11];
            Log.e("FileName",filename);

            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("expno", tempexpenseid));
            li.add(new BasicNameValuePair("empid", tempempid));
            li.add(new BasicNameValuePair("name", tempempname));
            li.add(new BasicNameValuePair("hqwork", temphqworked1));
            Log.e("Role",tempemprole);
            li.add(new BasicNameValuePair("role", tempemprole));
            li.add(new BasicNameValuePair("date", tempdate));
            li.add(new BasicNameValuePair("place", tempplace));
            li.add(new BasicNameValuePair("otherexpdetail", tempotherexpdetail));
            li.add(new BasicNameValuePair("otherexp", tempotherexp));
            li.add(new BasicNameValuePair("dailyallowance", tempdailyallow));
            li.add(new BasicNameValuePair("finaltotal", tempfinaltotal));
            li.add(new BasicNameValuePair("remark", tempremark));
            li.add(new BasicNameValuePair("filename", filename));
            //li.add(new BasicNameValuePair("choosefile", tempchoosefile));
            li.add(new BasicNameValuePair("HqId", hqid));


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
            pd.dismiss();
            try {
                id = json.getString("id");
               String res = json.getString("res");
                String Message = json.getString("msg");
                //  Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();

                if (res.equals("true")) {


                    LinearLayout locationdetail = (LinearLayout) findViewById(R.id.linearLayoutForm3);
                    for (int i = 0; i < locationdetail.getChildCount(); i++)
                    {
                        LinearLayout innerLayout = (LinearLayout) locationdetail.getChildAt(i);
                        EditText ed1 = (EditText)innerLayout.findViewById(R.id.number);
                        mAutocompleteTextView = (AutoCompleteTextView) innerLayout.findViewById(R.id.autoCompleteTextView);
                        mAutocompleteTextViewto = (AutoCompleteTextView) innerLayout.findViewById(R.id.autoCompleteTextViewto);
                        EditText ed2 =(EditText)innerLayout.findViewById(R.id.distance);
                        EditText ed3 =(EditText)innerLayout.findViewById(R.id.fare);

                        String tempno = ed1.getText().toString();
                        String tempfr = mAutocompleteTextView.getText().toString();
                        String tempto = mAutocompleteTextViewto.getText().toString();
                        String tempdist = ed2.getText().toString();
                        String tempfare = ed3.getText().toString();
                        //   Toast.makeText(AddReports.this, , Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(getApplicationContext(),tempfr , Toast.LENGTH_LONG).show();
                        new AddLocation().execute(tempno,tempfr,tempto,tempdist,tempfare,i+1+"");
                    }
                } else {

                    showdialog("Insertion failed", Message, id);

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }


    }
    public void showdialog(String title, String message, String code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenseHq.this);

        builder.setTitle(title);

        if (code.equals("true")) {
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i = new Intent(AddExpenseHq.this, Expense.class);
                    startActivity(i);
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i = new Intent(AddExpenseHq.this, Expense.class);
                    startActivity(i);
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    private class AddLocation extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(AddExpenseHq.this);
            pd.setTitle("Adding Expense");
            pd.setMessage("Please Wait..");
            pd.setIndeterminate(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String tempno = strings[0];
            String tempfr=strings[1];
            String tempto=strings[2];
            String tempdist = strings[3];
            String tempfare = strings[4];
            Log.e("From",tempfr);
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("no", tempno));
            li.add(new BasicNameValuePair("from", tempfr));
            li.add(new BasicNameValuePair("to", tempto));
            li.add(new BasicNameValuePair("distance", tempdist));
             li.add(new BasicNameValuePair("fare", tempfare));
            li.add(new BasicNameValuePair("Exp_id", id));
            //li.add(new BasicNameValuePair("Role", emprole));

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
                String Message = json.getString("msg");
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