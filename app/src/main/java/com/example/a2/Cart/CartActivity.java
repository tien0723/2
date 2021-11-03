package com.example.a2.Cart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2.Account.Account;
import com.example.a2.R;

import com.example.a2.ThanhToan.CartDetail;
import com.example.a2.ThanhToan.CartDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    RecyclerView rcvCart;
    CartAdapter cardAdapter;
    Button btnCart;
    ArrayList<CartDetail> list;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference detaiRef = db.getReference("Cart_Detail");
    DatabaseReference cartRef = db.getReference("Cart");
    DatabaseReference accountRef = db.getReference("Account");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        rcvCart = findViewById(R.id.lvCart);
        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        btnCart = findViewById(R.id.btnCartSubmit);
        //do du lieu
        list = new ArrayList<>();
        cardAdapter = new CartAdapter(this, list);
        data();
        rcvCart.setAdapter(cardAdapter);
        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, CartDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    public void data() {
        accountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Account account = dataSnapshot.getValue(Account.class);
                    account.setAcountID(dataSnapshot.getKey());
                    accountRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                Account account=dataSnapshot.getValue(Account.class);
                                account.setAcountID(dataSnapshot.getKey());
                                cartRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot node : snapshot.getChildren()) {
                                            Cart cart = node.getValue(Cart.class);
                                            cart.setCartID(node.getKey());
                                            if (cart.getAccountID().equals(account.getAcountID())) {
                                                Log.d("bbbb", "onDataChange: "+account.getAcountID());
                                                detaiRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        list.clear();
                                                        for (DataSnapshot node1 : snapshot.getChildren()) {
                                                            CartDetail detail = node1.getValue(CartDetail.class);
                                                            detail.setKey(node1.getKey());
                                                            if (cart.getCartID().equals(detail.getCartID())) {
                                                                list.add(detail);
                                                                Log.d("TAG", "onDataChange: "+detail.getPrice());
                                                                btnCart.setText(cart.getTotal()+" - Thanh toan");
                                                            }
                                                        }

                                                        cardAdapter.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itCartDelete:
                AlertDialog.Builder b = new AlertDialog.Builder(CartActivity.this);
                b.setTitle("Thong bao");
                b.setMessage("Ban co muon xoa toan bo san pham nay khong?");
                b.setPositiveButton("Xac nhan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Cart_Detail")
                                .removeValue();

                    }
                });
                b.setNegativeButton("Huy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog al = b.create();
                al.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
