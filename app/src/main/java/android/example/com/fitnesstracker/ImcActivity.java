package android.example.com.fitnesstracker;

import static androidx.core.os.LocaleListCompat.create;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ImcActivity extends AppCompatActivity {
    private EditText editHeight;
    private EditText editWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);
        editHeight = findViewById(R.id.edit_imc_height);
        editWeight = findViewById(R.id.edit_imc_weight);

        Button btnSend = findViewById(R.id.btn_imc_send);

        btnSend.setOnClickListener(v -> {
            if (!validate()){
                Toast.makeText(ImcActivity.this, R.string.fields_messages, Toast.LENGTH_LONG).show();
                return;
            }

            String sHeight = editHeight.getText().toString();
            String sWeight = editWeight.getText().toString();

            int height = Integer.parseInt(sHeight);
            int weight = Integer.parseInt(sWeight);

            double result = calculateImc(height, weight);
            Log.d("Teste", "resultado: " + result);

            int imcResponceId = imcResponce(result);
            //Toast.makeText(ImcActivity.this, imcResponceId, Toast.LENGTH_LONG).show();

            AlertDialog dialog = new AlertDialog.Builder(ImcActivity.this)
                    .setTitle(getString(R.string.imc_response, result))
                    .setMessage(imcResponceId)
                    .setPositiveButton(android.R.string.ok, (dialog1, which) -> {

                    })
                    .setNegativeButton(R.string.save, (dialog1, which) -> {

                        new Thread(() ->{
                            long calcId = SqlHelper.getInstance(ImcActivity.this).addItem("imc", result);
                            runOnUiThread(()->{
                                if (calcId > 0)
                                    Toast.makeText(ImcActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
                                    openListCalcActivity();
                            });
                        }).start();



                    })
                    .create();
            dialog.show();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editHeight.getWindowToken(), 0);

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_list:
                openListCalcActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openListCalcActivity(){
        Intent intent = new Intent(ImcActivity.this, ListCalcActivity.class);
        intent.putExtra("type", "imc");
        startActivity(intent);
    }

    @StringRes
    private int imcResponce(double imc){
        if (imc < 15)
            return R.string.imc_severely_low_weight;
        else if (imc < 16)
            return R.string.imc_very_low_weight;
        else if (imc < 18.5)
            return R.string.imc_very_low_weight;
        else if (imc < 25)
            return R.string.normal;
        else if (imc < 30)
            return R.string.imc_high_weight;
        else if (imc < 35)
            return R.string.imc_so_high_weight;
        else if (imc < 40)
            return R.string.imc_severely_high_weight;
        else
            return R.string.imc_extreme_weight;

    }

    private double calculateImc(int height, int weight){
        //peso / (altura* altura)
        return weight / (((double) height / 100) * ((double) height / 100 ));
    }

    private boolean validate(){
        if (!editHeight.getText().toString().startsWith("0")
            && !editWeight.getText().toString().startsWith("0")
                && !editHeight.getText().toString().isEmpty()
                && !editWeight.getText().toString().isEmpty()
        ){
            return true;
        }
        return false;
    }
}