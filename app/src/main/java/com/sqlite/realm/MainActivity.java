package com.sqlite.realm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnSqlite, btnRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSqlite = (Button) findViewById(R.id.btn_sqlite);
        btnRealm = (Button) findViewById(R.id.btn_realm);

        btnSqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SqliteActivity.class);
                startActivity(i);
            }
        });

        btnRealm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RealmActivity.class);
                startActivity(i);
            }
        });

    }
}
