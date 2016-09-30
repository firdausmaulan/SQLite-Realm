package com.sqlite.realm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sqlite.realm.db.RealmDB;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmActivity extends AppCompatActivity {

    private EditText etName;
    private Button btnSave;
    private ListView lvName;
    private List<String> list_name = new ArrayList<String>();
    private ArrayAdapter listNameAdapter;
    private RealmConfiguration realmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        etName = (EditText) findViewById(R.id.et_name);
        btnSave = (Button) findViewById(R.id.btn_save);
        lvName = (ListView) findViewById(R.id.lv_name);

        realmConfig = new RealmConfiguration.Builder(this)
                .name("local.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        Realm myRealm = Realm.getDefaultInstance();

        reloadListView();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                insertAction(name, (list_name.size() + 1));
                etName.setText("");
                reloadListView();
            }
        });

        lvName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String name = String.valueOf(parent.getItemAtPosition(position));
                final AlertDialog sellectDialog = new AlertDialog.Builder(RealmActivity.this).create();
                sellectDialog.setTitle(name);
                sellectDialog.setButton(DialogInterface.BUTTON_POSITIVE, "DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(position);
                        sellectDialog.dismiss();
                    }
                });
                sellectDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editData(position, name);
                        sellectDialog.dismiss();
                    }
                });
                sellectDialog.show();
            }
        });
    }

    private void reloadListView() {
        if (list_name != null || list_name.size() != 0) {
            list_name.clear();
        }
        Realm myRealm = Realm.getDefaultInstance();
        RealmResults<RealmDB> results = myRealm.where(RealmDB.class).findAllSorted("id");
        if (results != null && results.size() != 0) {
            for (RealmDB obj : results) {
                Log.i("id", obj.getId() + "");
                Log.i("name", obj.getName());
                list_name.add(obj.getName().toString());
            }
            Log.i("listnamesize", list_name.size() + "");
            listNameAdapter = new ArrayAdapter<String>(this, R.layout.text_list, list_name);
            lvName.setAdapter(listNameAdapter);
        }
    }

    private void deleteData(final int position) {
        final AlertDialog deleteDialog = new AlertDialog.Builder(RealmActivity.this).create();
        deleteDialog.setTitle("DELETE DATA");
        deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                actionDelete(position);
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

    private void editData(final int position, final String name) {
        AlertDialog editDialog = new AlertDialog.Builder(RealmActivity.this).create();
        editDialog.setTitle("EDIT DATA");
        LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View editDialogView = li.inflate(R.layout.edit, null);
        editDialog.setView(editDialogView);
        final EditText etEditData = (EditText) editDialogView.findViewById(R.id.etEditName);
        editDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                actionDelete(position);
                insertAction(etEditData.getText().toString(), position);
                reloadListView();
            }
        });
        editDialog.show();
    }

    private void insertAction(final String name, final int position) {
        Realm myRealm = Realm.getDefaultInstance();
        myRealm.beginTransaction();
        // Create an object
        RealmDB realmDB = myRealm.createObject(RealmDB.class);
        // Set its fields
        Log.i("realmId", position + "");
        realmDB.setId(position);
        realmDB.setName(name);
        myRealm.commitTransaction();
    }

    private void actionDelete(final int position) {
        Realm myRealm = Realm.getDefaultInstance();
        myRealm.beginTransaction();
        RealmQuery query = myRealm.where(RealmDB.class);
        RealmResults results = query.findAll();
        results.remove(position);
        myRealm.commitTransaction();
    }
}