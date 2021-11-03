package com.example.a2.ThanhToan;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.a2.Account.Account;
import com.example.a2.Cart.Cart;
import com.example.a2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartDetailActivity extends AppCompatActivity {
    RecyclerView rcvCartDetail;
   // String accountID = "abc05684428156";
    CartDetailAdapter cardAdapter;
    Button btnCart;
    ArrayList<CartDetail> list;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference detaiRef = db.getReference("Cart_Detail");
    DatabaseReference cartRef = db.getReference("Cart");
    DatabaseReference accountRef=db.getReference("Account");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_detail);
        this.getSupportActionBar().setTitle("Chi tiet gio hang");
        rcvCartDetail=findViewById(R.id.lvCartDetail);


//        //lay du lieu tu firebase
//        FirebaseRecyclerOptions<ThanhToan> options =
//                new FirebaseRecyclerOptions.Builder<ThanhToan>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Cart"),ThanhToan.class)
//                        .build();
//
//        Log.d("Options"," data : "+options);
        list = new ArrayList<>();
        cardAdapter=new CartDetailAdapter( this,list);
        data();
        rcvCartDetail.setAdapter(cardAdapter);
        rcvCartDetail.setLayoutManager(new LinearLayoutManager(this));

    }

    private void data() {
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
