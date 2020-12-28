package com.berkantcanerkanat.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    EditText english,turkish,level;
    TextView alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        english = findViewById(R.id.wordEText);
        turkish = findViewById(R.id.wordTText);
        level = findViewById(R.id.levelText);
        alert = findViewById(R.id.alert);
    }
    public void ekle(View view){
        if(level.getText().toString().toLowerCase().equals("a1") || level.getText().toString().toLowerCase().equals("a2") ||
                level.getText().toString().toLowerCase().equals("b1") || level.getText().toString().toLowerCase().equals("b2") ||
        level.getText().toString().toLowerCase().equals("c1") || level.getText().toString().toLowerCase().equals("c2") || level.getText().toString().toLowerCase().equals("dk")){
            if(!english.getText().toString().equals("") && !turkish.getText().toString().equals("")){
                if(controlIfExist()){
                Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                intent.putExtra("info",1);
                intent.putExtra("english",english.getText().toString().toLowerCase());
                intent.putExtra("turkish",turkish.getText().toString());
                intent.putExtra("level",level.getText().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                }else{
                    alert.setText("eklemek istediginiz kelime zaten var");
                }
            }else{
                alert.setText("lutfen bos birakmayiniz");
            }
        }else{
            alert.setText("lutfen belirtilenlerden birini giriniz bilmiyorsaniz DK giriniz");
        }
    }
    public boolean controlIfExist(){
        if(MainActivity.words.size() == 0) return true;
        for(String kelime: MainActivity.words){
            if(english.getText().toString().toLowerCase().equals(kelime)){
                return false;
            }
        }
        return true;
    }
}