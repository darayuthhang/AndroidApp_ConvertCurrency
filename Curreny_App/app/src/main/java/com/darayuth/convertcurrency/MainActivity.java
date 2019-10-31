package com.darayuth.convertcurrency;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "ACTIVITY";
    public static final String khRiel = "kh-riel";
    public static final String usDollar = "us-dollar";
    public static final String onStateCurrency = "SAVESTATE";

    //private EditText fromEditText, toEditText;
    private TextView result;
    //private Button submit;
    private BootstrapButton submit;
    private BootstrapEditText fromEditText;
    private Spinner spinner1, spinner2;
    private double amount, resultOfCalculation;
    private String convertCurrencyToString, inputFromCurrency, inputToCurrency, valueSpinner1, valueSpinner2;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceProvider.registerDefaultIconSets();

        fromEditText = (BootstrapEditText) findViewById(R.id.fromEditText);
        result = (TextView) findViewById(R.id.resultTextView) ;
        submit = (BootstrapButton) findViewById(R.id.button);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        Adapter();
        initiApp();

    }
    public void Adapter(){
        final ArrayAdapter<String> mAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().
                getStringArray(R.array.currency_lists));
        mAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner1.setAdapter(mAdapter);
        //get the value when user pick either Khmer or US on the spinner1
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //since value initlize as field we can assign value to it.
                valueSpinner1 = mAdapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<String> secondAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().
                getStringArray(R.array.currency_lists2));
        secondAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner2.setAdapter(secondAdapter);
        //get the value when user pick either Khmer or US on the spinner2
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //since value initlize as field we can assign value to it.
                 valueSpinner2 = secondAdapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String currency = savedInstanceState.getString(onStateCurrency);
        Log.d(TAG, "onRestoreInstanceState: "+currency);
        result.setText(currency);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(onStateCurrency, convertCurrencyToString);
        super.onSaveInstanceState(outState);
    }

    public void initiApp(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginTheCalculationAfterCheckingSpinners();
            }
        });
    }
    //method: beginCalcualtion
    // the method will check for rield or dollar , and whether
    //the amount is only the number.
    public void beginTheCalculationAfterCheckingSpinners(){
        String regex = "[0-9]+";
        convertCurrencyToString =fromEditText.getText().toString();
        //check if currency is empty , and display Toast
        //to users.
        if(convertCurrencyToString.isEmpty()){
            CharSequence text = "The inputs cannot be empty";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }else if(convertCurrencyToString.matches(regex)){
            checkForSpinnerValue();
        }else{
            CharSequence text = "Input cannot be letters Or Signs";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }
    public void checkForSpinnerValue(){
        //convert the input currency string to the Double data type.
        amount = Math.round(Double.parseDouble(convertCurrencyToString) * 100)/100;
        try{
            //check if the input consist letter or empty.
            //Then, throw numberformat exception
            //else call the performCalulation method
            veriFyTheInput();
            //check for us riel to Us Dollar
            if ((valueSpinner1.equalsIgnoreCase(khRiel) && valueSpinner2.equalsIgnoreCase(khRiel))) {
                displayTheResultOfCalculation(amount, khRiel);
            }else if( (valueSpinner1.equalsIgnoreCase(usDollar) && valueSpinner2.equalsIgnoreCase(usDollar))){
                displayTheResultOfCalculation(amount, usDollar);
            } else if(valueSpinner1.equalsIgnoreCase(usDollar)){//convert dollar to riel.
                if (valueSpinner2.equalsIgnoreCase(khRiel)) {
                    //if currency less than 1 dollar.
                    if(amount < 1){
                        resultOfCalculation = (amount * 100) * 40.5613;;
                        displayTheResultOfCalculation(resultOfCalculation, khRiel);
                    }else{
                        resultOfCalculation = amount * 4055.44;
                        displayTheResultOfCalculation(resultOfCalculation, khRiel);
                    }
                }
                //convert dollar to riel
            }else if(valueSpinner1.equalsIgnoreCase(khRiel)){
                if(valueSpinner2.equalsIgnoreCase(usDollar)){
                    String rateType = "";
                    if(amount < 4000){
                        rateType = "Us-cent-Dollar";
                    }else{
                        rateType = usDollar;
                    }
                    //if currency less than 1 dollar.
                    resultOfCalculation = (amount / 1000) * 0.25;
                    displayTheResultOfCalculation(resultOfCalculation, rateType);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void veriFyTheInput() throws messageException{

        try{
            //test if the input is not string else thrown exception.
            amount = Double.parseDouble(fromEditText.getText().toString());
        }catch (NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Input cannot be letters.", Toast.LENGTH_SHORT).show();
            throw new messageException("Input cannot be letters.");
        }

    }
    /********
     **
     * @param resultOfCalculation symbol for show result to the TextView
     * @param currenyType symbol for show result to TextView
     *
     */
    public void displayTheResultOfCalculation(double resultOfCalculation, String currenyType){
        //convert currency to string
        convertCurrencyToString = String.valueOf(resultOfCalculation);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(convertCurrencyToString + " ");
        stringBuffer.append(currenyType);
        //display result to TextView as result.
        result.setText(stringBuffer);
        //set input FROM, TO currency to empty after
        //display currency.
        fromEditText.setText("");

    }


}

