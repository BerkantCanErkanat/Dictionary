package com.berkantcanerkanat.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    EditText wordSearch;
    ArrayAdapter arrayAdapter;
    static ArrayList<String> words;
    SQLiteDatabase db;
    ArrayList<Word> kelimeObjeleri;
    String eName,tName,level;
    TextView textView;
    Runnable runnable;
    Handler handler;
    ArrayList<String> yeniList = new ArrayList<>();
    boolean ictengeldik = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        listView = findViewById(R.id.listView);
        wordSearch = findViewById(R.id.word);
        wordSearch.addTextChangedListener(filterTextWatcher);
        db = this.openOrCreateDatabase("Words",MODE_PRIVATE,null);
        textView = findViewById(R.id.textView);
        if(intent.getIntExtra("info",0) == 1){
            eName = intent.getStringExtra("english");
            tName =  intent.getStringExtra("turkish");
            level = intent.getStringExtra("level");
            dbElemanAl();
            //tekrardan kendi classımın arraylist objesi icin acmam gerekti ama string arraylisti icin tekrardan acmama gerek kalmadı.
            kelimeObjeleri.add(new Word(eName,tName,level));
            dbElemanEkle();
            words.add(eName);
        }else{
            words = new ArrayList<>();
            dbElemanAl();
            objectToAList();
        }
        arrayAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,words);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(yeniList.size() == 0){
                    for(Word word: kelimeObjeleri){
                        if(word.wordNameE.equals(words.get(i))){
                            String yaz = "Kelime: "+word.wordNameE+"\nTurkce: "+word.wordNameT+"\nlevel: "+word.level;
                            textView.setText(yaz);
                            break;
                        }
                    }
                }else{
                    for(Word word: kelimeObjeleri){
                        System.out.println(yeniList.get(i)+"---"+word.wordNameE);
                        if(yeniList.get(i).equals(word.wordNameE)){
                            String yaz = "Kelime: "+word.wordNameE+"\nTurkce: "+word.wordNameT+"\nlevel: "+word.level;
                            textView.setText(yaz);
                            System.out.println("girdi");
                        }
                    }
                }

            }
        }
        );
    }
    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            textView.setText("");
            if(wordSearch.getText().toString().length() >= 2){
                kontrol();
                arrayAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,yeniList);
                listView.setAdapter(arrayAdapter);
            }else{
                arrayAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,words);
                listView.setAdapter(arrayAdapter);
                yeniList = new ArrayList<>();
                textView.setText("");
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

    };

    public void kontrol(){
        yeniList = new ArrayList<>();
        int boy = 0;
        int count = 0;
        String yazilan = wordSearch.getText().toString().toLowerCase();
        for(String kelime: words){
            if(kelime.length() <= yazilan.length()){
                boy = kelime.length();
            }else{
                boy = yazilan.length();
            }
           for(int i = 0 ;i<boy;i++){
               if(kelime.toLowerCase().charAt(i) == yazilan.charAt(i)){
                   count++;
               }
           }
           if(count == boy){
               yeniList.add(kelime);
               count = 0;
           }else{
               count = 0;
           }
        }
    }
    public void objectToAList(){
        for(Word word: kelimeObjeleri){
            words.add(word.wordNameE);
        }
    }
    public void dbElemanEkle(){
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS words (wordE VARCHAR, wordT VARCHAR, level VARCHAR)");
            String sqlStatement = "INSERT INTO words (wordE, wordT, level) VALUES (?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(sqlStatement);
            sqLiteStatement.bindString(1,eName);
            sqLiteStatement.bindString(2,tName);
            sqLiteStatement.bindString(3,level);
            sqLiteStatement.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void dbElemanAl(){
        db.execSQL("CREATE TABLE IF NOT EXISTS words (wordE VARCHAR, wordT VARCHAR, level VARCHAR)");
        Cursor cursor = db.rawQuery("SELECT * FROM words",null);
        int wordEIx = cursor.getColumnIndex("wordE");
        int wordTIx = cursor.getColumnIndex("wordT");
        int levelIx = cursor.getColumnIndex("level");
        kelimeObjeleri = new ArrayList<>();
        while(cursor.moveToNext()){
            kelimeObjeleri.add(new Word(cursor.getString(wordEIx),cursor.getString(wordTIx),cursor.getString(levelIx)));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.newWord){
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    public void lookFor(View view){

    }
}