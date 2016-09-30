package com.sqlite.realm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sqlite.realm.db.SQLiteDB;

import java.util.List;

public class SqliteActivity extends AppCompatActivity {

    private SQLiteDB sqliteDB;
    private EditText etName;
    private Button btnSave;
    private ListView lvName;
    private List<String> list_name;
    private ArrayAdapter listNameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        etName = (EditText) findViewById(R.id.et_name);
        btnSave = (Button) findViewById(R.id.btn_save);
        lvName = (ListView) findViewById(R.id.lv_name);
        sqliteDB = new SQLiteDB(getApplicationContext());

        reloadListView();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                String name = etName.getText().toString();
                sqliteDB.insert(name);
                etName.setText("");
                reloadListView();
            }
        });

        lvName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String name = String.valueOf(parent.getItemAtPosition(position));
                final AlertDialog sellectDialog = new AlertDialog.Builder(SqliteActivity.this).create();
                sellectDialog.setTitle(name);
                sellectDialog.setButton(DialogInterface.BUTTON_POSITIVE, "DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(name);
                        sellectDialog.dismiss();
                    }
                });
                sellectDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editData(name);
                        sellectDialog.dismiss();
                    }
                });
                sellectDialog.show();
            }
        });
    }

    private void reloadListView() {
        list_name = sqliteDB.selectAll();
        listNameAdapter = new ArrayAdapter<String>(this, R.layout.text_list, list_name);
        lvName.setAdapter(listNameAdapter);
    }

    private void deleteData(final String name) {
        final AlertDialog deleteDialog = new AlertDialog.Builder(SqliteActivity.this).create();
        deleteDialog.setTitle("DELETE DATA");
        deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sqliteDB.delete(name);
                deleteDialog.dismiss();
                reloadListView();
            }
        });
        deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
    }

    private void editData(final String name) {
        AlertDialog editDialog = new AlertDialog.Builder(SqliteActivity.this).create();
        editDialog.setTitle("EDIT DATA");
        LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View editDialogView = li.inflate(R.layout.edit, null);
        editDialog.setView(editDialogView);
        final EditText etEditData = (EditText) editDialogView.findViewById(R.id.etEditName);
        editDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sqliteDB.update(name, etEditData.getText().toString());
                reloadListView();
            }
        });
        editDialog.show();
    }
}
