package com.magicstar.abvp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private static final int SELECT_PICTURE = 0;
    private int STORAGE_PERMISSION_CODE = 1;
    private ImageView imageView;
    Bitmap bitmap;


    EditText editTextHeroId, editTextName, editTextCity, editTextCollege, editTextCell, editTextEmail;
    Spinner spinnerBlood, education, gender;
    ProgressBar progressBar;
    static TextView dob;
    static String dob1;
    int id;
    //ListView listView;
    Button buttonAddUpdate;

    List<Hero> heroList;

    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextHeroId = (EditText) findViewById(R.id.editTextHeroId);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextCity = (EditText) findViewById(R.id.editTextCity);
        editTextCollege = (EditText) findViewById(R.id.editTextCollege);
        editTextCell = (EditText) findViewById(R.id.editTextCell);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        spinnerBlood = (Spinner) findViewById(R.id.spinnerTeamAffiliation);
        education = (Spinner) findViewById(R.id.spinnerTeamAffiliation1);
        gender = (Spinner) findViewById(R.id.spinnerTeamAffiliation2);
        buttonAddUpdate = (Button) findViewById(R.id.buttonAddUpdate);
        dob = (TextView) findViewById(R.id.dob);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        heroList = new ArrayList<>();

       // Button btn = findViewById(R.id.buttonAddPhoto);
        buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdating) {
                    createHero();
                } else {
                    createHero();
                }
            }
        });

    }


    private void createHero() {

        String name = editTextName.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String College = editTextCollege.getText().toString().trim();
        String Mobile = editTextCell.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        //String realname = editTextRealname.getText().toString().trim();

        //int rating = (int) ratingBar.getRating();

        String blood = spinnerBlood.getSelectedItem().toString();
        String gen = gender.getSelectedItem().toString();
        String edu = education.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Please enter name");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(city)) {
            editTextCity.setError("Please enter City");
            editTextCity.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(Mobile)) {
            editTextCell.setError("Please enter Mobile No");
            editTextCell.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter Email");
            editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(College)) {
            editTextCollege.setError("Please enter College");
            editTextCollege.requestFocus();
            return;
        }


        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("city", city);
        params.put("college", College);
        params.put("Mobile", Mobile);
        params.put("email", email);
        params.put("dob", dob1);
        params.put("blood", blood);
        params.put("Education", edu);
        params.put("gender", gen);

       /* params.put("name", "XYZ");
        params.put("city", "Hubli");
        params.put("college", "KLE");
        params.put("Mobile", "9757483764");
        params.put("email", "ganeshhulyal@gmail.com");
        params.put("dob","30-08-1999" );
        params.put("blood", "A+");
        params.put("Education", "Enginnering");
        params.put("gender", "Male");*/

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_HERO, params, CODE_POST_REQUEST);
        request.execute();
        readHeroes();
        onClear();

    }

    private void readHeroes() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_HEROES, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void deleteHero(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_HERO + id, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshHeroList(JSONArray heroes) throws JSONException {
        heroList.clear();

        for (int i = 0; i < heroes.length(); i++) {
            JSONObject obj = heroes.getJSONObject(i);
                id=obj.getInt("id");
        }
        EditText name = (EditText) this.findViewById(R.id.editTextName);
        EditText city = (EditText) this.findViewById(R.id.editTextCity);
        EditText college = (EditText) this.findViewById(R.id.editTextCollege);
        EditText mobile = (EditText) this.findViewById(R.id.editTextCell);
        EditText email = (EditText) this.findViewById(R.id.editTextEmail);
        TextView date = (TextView) this.findViewById(R.id.dob);
        spinnerBlood = (Spinner) findViewById(R.id.spinnerTeamAffiliation);
        String name1 = name.getText().toString();
        String city1 = city.getText().toString();
        String college1 = college.getText().toString();
        String blood = spinnerBlood.getSelectedItem().toString();
        Intent intent = new Intent(this, AfterReg.class);
        intent.putExtra("name", name1);
        intent.putExtra("city", city1);
        intent.putExtra("college", college1);
        intent.putExtra("blood", blood);
        intent.putExtra("id",id);
        startActivity(intent);
       // HeroAdapter adapter = new HeroAdapter(heroList);
        //listView.setAdapter(adapter);
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroList(object.getJSONArray("heroes"));
                } else {
                    Toast.makeText(getApplicationContext(), "Registration Failed!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST) return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    class HeroAdapter extends ArrayAdapter<Hero> {
        List<Hero> heroList;

        public HeroAdapter(List<Hero> heroList) {
            super(MainActivity.this, R.layout.layout_hero_list, heroList);
            this.heroList = heroList;
        }

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month = month + 1;
            dob.setText(day + "-" + month + "-" + year);
            dob1 = day + "-" + month + "-" + year;
        }
    }



    private void onClear() {

    }



}
