package com.b2.bipul.examtest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<UserData> lists;
    private CustomAdapter adapter;
    private List<String> key;
    private Button insert_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insert_btn = findViewById(R.id.insert_btn);
        recyclerView = findViewById(R.id.recycle_view_id);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager lm = new LinearLayoutManager(MainActivity.this);
        lm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(lm);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("customers");

        lists = new ArrayList<>();
        key = new ArrayList<>();

        adapter = new CustomAdapter(MainActivity.this, lists, key);
        recyclerView.setAdapter(adapter);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lists.clear();
                key.clear();

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    lists.add(dataSnapshot1.getValue(UserData.class));
                    key.add(dataSnapshot1.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.update_data_view, null, false);
                final EditText name_field = view.findViewById(R.id.name_field_id);
                final EditText email_field = view.findViewById(R.id.email_field_id);
                final EditText address_field = view.findViewById(R.id.address_field_id);
                final TextView title = view.findViewById(R.id.title_id);
                title.setText("Insert Customer Info");
                Button cancel_btn = view.findViewById(R.id.cancel_btn_id);
                Button submit_btn = view.findViewById(R.id.submit_btn_id);

                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                submit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nVal = name_field.getText().toString();
                        String eVal = email_field.getText().toString();
                        String aVal = address_field.getText().toString();
                        if(nVal.isEmpty()||eVal.isEmpty()||aVal.isEmpty()){
                            Toast.makeText(MainActivity.this, "Please Fill up all fields.", Toast.LENGTH_SHORT).show();
                        }else{
                            UserData obj = new UserData();
                            obj.setName(nVal);
                            obj.setEmail(eVal);
                            obj.setAddress(aVal);
                            myRef.push().setValue(obj);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.setCancelable(false);
                dialog.setView(view);
                dialog.show();
            }
        });
    }
}