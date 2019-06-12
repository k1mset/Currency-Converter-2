package com.dillancobb.currencyconverter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    // Doubles used for storing the currency rates from the JSON API
    double jsonUsd;
    double jsonJpy;
    double jsonGbp;
    double jsonAud;
    double jsonCad;
    double jsonChf;
    double jsonThb;
    double jsonSek;
    double jsonNzd;

    // Editable textviews on the UI
    TextView euroTxt;
    TextView rateTxt;
    TextView amountTxt;

    // ImageView on the Ui that gets edited
    ImageView currDisp;

    // Format for currency
    DecimalFormat currencyFormat = new DecimalFormat("0.00");

    // convertMoney method is called when the Convert button is pressed.
    // Takes the amount entered in the txtEuroTxt and multiplies it to the rates fetched
    // from the API. Alters the imageview to the currency selected, and alters the total information
    // inside the layout for the user
    public void convertMoney(View view) {
        /* Information on position according to the Spinner
         * Spinner ID's : URL
         * 0 - USD
         * 1 - JPY
         * 2 - GBP
         * 3 - AUD
         * 4 - CAD
         * 5 - CHF
         * 6 - THB
         * 7 - SEK
         * 8 - NZD
         */

        // Other needed variables
        EditText euroAmt = (EditText) findViewById(R.id.txtEuroAmt);
        Spinner currencyConvert = (Spinner) findViewById(R.id.spinner);
        int currencyPos = currencyConvert.getSelectedItemPosition();

        // Try catch to ensure the user placed an amount in the txtDollarInput
        try {
            double euro = Double.parseDouble(euroAmt.getText().toString());
            double total = 0;
            String sym = "";

            euroTxt.setText(Double.toString(euro));

            // Checks the spinner position and handles according compared to its position
            switch (currencyPos) {

                // EUR
                case 0:
                    total = Double.parseDouble(currencyFormat.format(euro * jsonUsd));
                    sym = "$";
                    rateTxt.setText(currencyFormat.format(jsonUsd));
                    currDisp.setImageResource(R.drawable.usd);
                    break;

                // JPY
                case 1:
                    total = Double.parseDouble(currencyFormat.format(euro * jsonJpy));
                    sym = "¥";
                    rateTxt.setText(currencyFormat.format(jsonJpy));
                    currDisp.setImageResource(R.drawable.jpy);
                    break;

                // GBP
                case 2:
                    total = Double.parseDouble(currencyFormat.format(euro * jsonGbp));
                    sym = "£";
                    rateTxt.setText(currencyFormat.format(jsonGbp));
                    currDisp.setImageResource(R.drawable.gbp);
                    break;

                // AUD
                case 3:
                    total = Double.parseDouble(currencyFormat.format(euro * jsonAud));
                    sym = "$";
                    rateTxt.setText(currencyFormat.format(jsonAud));
                    currDisp.setImageResource(R.drawable.aud);
                    break;

                // CAD
                case 4:
                    total = Double.parseDouble(currencyFormat.format(euro * jsonCad));
                    sym = "$";
                    rateTxt.setText(currencyFormat.format(jsonCad));
                    currDisp.setImageResource(R.drawable.cad);
                    break;

                // CHF
                case 5:
                    total = Double.parseDouble(currencyFormat.format(euro * jsonChf));
                    sym = "SFr.";
                    rateTxt.setText(currencyFormat.format(jsonChf));
                    currDisp.setImageResource(R.drawable.chf);
                    break;

                // THB
                case 6:
                    total = Double.parseDouble(currencyFormat.format(euro * jsonThb));
                    sym = "฿";
                    rateTxt.setText(currencyFormat.format(jsonThb));
                    currDisp.setImageResource(R.drawable.thb);
                    break;

                // SEK
                case 7:
                    total = Double.parseDouble(currencyFormat.format(euro * jsonSek));
                    sym = "kr";
                    rateTxt.setText(currencyFormat.format(jsonSek));
                    currDisp.setImageResource(R.drawable.sek);
                    break;

                // NZD
                case 8:
                    total = Double.parseDouble(currencyFormat.format(euro * jsonNzd));
                    sym = "$";
                    rateTxt.setText(currencyFormat.format(jsonNzd));
                    currDisp.setImageResource(R.drawable.nzd);
                    break;

                // Switch default cannot be reached
                default:
                    // Cannot be called
                    break;
            }

            amountTxt.setText(sym + total);
        } catch (NumberFormatException e) {
            // Creates a toast in case the user never entered in an ammout in txtDollarInput
            Toast.makeText(MainActivity.this, "Enter a euro amount.",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    // Handles the fetching of JSON data from the API
    public class DownloadTask extends AsyncTask<String, Void, String> {

        // Connects to the URL and pulls the information into the device to be used
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Failed";
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed";
            }
        }

        // Handles the JSON data after it was fetched, stores appropriate rate information into
        // rate variables
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject rateObject = new JSONObject(jsonObject.getString("rates"));

                jsonUsd = Double.parseDouble(currencyFormat.format(rateObject.getDouble
                        ("USD")));
                jsonJpy = Double.parseDouble(currencyFormat.format(rateObject.getDouble
                        ("JPY")));
                jsonGbp = Double.parseDouble(currencyFormat.format(rateObject.getDouble
                        ("GBP")));
                jsonAud = Double.parseDouble(currencyFormat.format(rateObject.getDouble
                        ("AUD")));
                jsonCad = Double.parseDouble(currencyFormat.format(rateObject.getDouble
                        ("CAD")));
                jsonChf = Double.parseDouble(currencyFormat.format(rateObject.getDouble
                        ("CHF")));
                jsonThb = Double.parseDouble(currencyFormat.format(rateObject.getDouble
                        ("THB")));
                jsonSek = Double.parseDouble(currencyFormat.format(rateObject.getDouble
                        ("SEK")));
                jsonNzd = Double.parseDouble(currencyFormat.format(rateObject.getDouble
                        ("NZD")));

                // For debugging, displays each rate from the JSON data
                Log.i("USD Rate", ""+jsonUsd);
                Log.i("JPY Rate", ""+jsonJpy);
                Log.i("GBP Rate", ""+jsonGbp);
                Log.i("AUD Rate", ""+jsonAud);
                Log.i("CAD Rate", ""+jsonCad);
                Log.i("CHK Rate", ""+jsonChf);
                Log.i("SEK Rate", ""+jsonSek);
                Log.i("NZD Rate", ""+jsonNzd);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Runs the call for the API and executes to gather the rate information
        DownloadTask task = new DownloadTask();
        task.execute("http://data.fixer.io/api/latest?access_key=036110653bcc9c74a83d5f6bc846ae0a&format=1");

        // Find the editing UI items
        euroTxt = (TextView) findViewById(R.id.txtEuro);
        rateTxt = (TextView) findViewById(R.id.txtRate);
        amountTxt = (TextView) findViewById(R.id.txtTotal);

        currDisp = (ImageView) findViewById(R.id.imageView);
    }
}
