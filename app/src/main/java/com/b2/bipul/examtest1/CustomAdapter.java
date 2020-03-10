
package com.b2.bipul.examtest1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    private List<UserData> lists;
    private List<String> key;
    CustomAdapter(Context context, List<UserData> lists, List<String> key){
        this.context = context;
        this.lists = lists;
        this.key = key;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(lists.get(position).getName());
        holder.email.setText(lists.get(position).getEmail());
        holder.address.setText(lists.get(position).getAddress());

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(context).create();
                View view = LayoutInflater.from(context).inflate(R.layout.update_data_view, null, false);
                final EditText name_field = view.findViewById(R.id.name_field_id);
                final EditText email_field = view.findViewById(R.id.email_field_id);
                final EditText address_field = view.findViewById(R.id.address_field_id);

                name_field.setText(lists.get(position).getName());
                email_field.setText(lists.get(position).getEmail());
                address_field.setText(lists.get(position).getAddress());
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
                            Toast.makeText(context, "Please Fill up all fields.", Toast.LENGTH_SHORT).show();
                        }else{
                            update(nVal, eVal, aVal, key.get(position));
                            dialog.dismiss();
                        }
                    }
                });
                dialog.setCancelable(false);
                dialog.setView(view);
                dialog.show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete Confirmation");
                dialog.setMessage("Are you Sure for delete?");
                dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(key.get(position));
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }

    private void delete(String key){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("customers").child(key);
        myRef.removeValue();
    }

    private void update(String name, String email, String address, String key){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("customers").child(key);
        myRef.child("name").setValue(name);
        myRef.child("email").setValue(email);
        myRef.child("address").setValue(address);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name, email, address;
        private Button update, delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_view_id);
            email = itemView.findViewById(R.id.email_view_id);
            address = itemView.findViewById(R.id.address_view_id);
            update = itemView.findViewById(R.id.update_btn_id);
            delete = itemView.findViewById(R.id.delte_btn_id);
        }
    }
}
