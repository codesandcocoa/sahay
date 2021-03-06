package amhacks.gratitude.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.HashMap;

import amhacks.gratitude.Model.Requests;
import amhacks.gratitude.ViewHolder.RequestsViewHolder;
import amhacks.gratitude.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {
    private RelativeLayout helpSeekerSwitch, helperSwitch;
    private LinearLayout helperLayout, helpSeekerLayout, profileLayout;
    private LinearLayout billsFormLayout, groceryFormLayout, emergencyFormLayout, orderFormLayout;
    private LinearLayout billsLayout, groceryLayout, emergencyLayout, orderLayout;
    private TextView helpSeekerTxt, helperTxt, usernameTxt, billsTimeTxt,emergencyTimeTxt, groceryTimeTxt,orderTimeTxt;
    private String user_type, currentUserID, fullname, dest_time, address, latlon_target;
    private FirebaseAuth mAuth;
    private CircleImageView profileView;
    private FirebaseFirestore firestore;
    private TimePicker billsTimePicker;
    private Button postBills;
    private RecyclerView requestsView;
    private EditText DescET;
    private FirestoreRecyclerAdapter adapter;

    private TimePicker groceryTimePicker, foodTimePicker, emergencyTimePicker;
    private Button postGrocery, postEmergency, postOrder;

    private EditText groceryET, emergencyET, orderET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid().toString();

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Users").document(currentUserID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null)
                {
                    fullname = value.get("fullname").toString();
                    address = value.get("address").toString();
                    usernameTxt.setText(fullname);
                    Glide.with(getApplicationContext()).load(value.get("profile_picture").toString()).into(profileView);
                }
            }
        });
        helperSwitch = (RelativeLayout) findViewById(R.id.helper_switch);
        helpSeekerSwitch = (RelativeLayout) findViewById(R.id.help_seeker_switch);
        helperTxt = (TextView) findViewById(R.id.helper_txt);
        helpSeekerTxt = (TextView) findViewById(R.id.help_seeker_txt);
        helperLayout = (LinearLayout) findViewById(R.id.helper_body);
        profileView = (CircleImageView) findViewById(R.id.profile_image_dashboard);
        helpSeekerLayout = (LinearLayout) findViewById(R.id.help_seeker_body);
        profileLayout = (LinearLayout) findViewById(R.id.profile_llt);
        usernameTxt = (TextView) findViewById(R.id.username_txt);
        billsTimePicker = (TimePicker) findViewById(R.id.time_picker_bills);
        groceryTimePicker = (TimePicker) findViewById(R.id.time_picker_grocery);
        emergencyTimePicker = (TimePicker) findViewById(R.id.time_picker_emergency);
        foodTimePicker = (TimePicker) findViewById(R.id.time_picker_order);

        billsTimeTxt = (TextView) findViewById(R.id.time_txt_bills);
        groceryTimeTxt = (TextView) findViewById(R.id.time_txt_grocery);
        emergencyTimeTxt = (TextView) findViewById(R.id.time_txt_emergency);
        orderTimeTxt = (TextView) findViewById(R.id.time_txt_order);

        postBills = (Button) findViewById(R.id.post_request_bills);
        postEmergency = (Button) findViewById(R.id.post_request_emergency);
        postGrocery = (Button) findViewById(R.id.post_request_grocery);
        postOrder = (Button) findViewById(R.id.post_request_order);

        DescET = (EditText) findViewById(R.id.desc_bills);
        requestsView = (RecyclerView)  findViewById(R.id.requests_recycler_view);
        groceryET = (EditText) findViewById(R.id.desc_grocery);
        orderET = (EditText) findViewById(R.id.desc_order);
        emergencyET = (EditText) findViewById(R.id.desc_emergency);
        billsFormLayout = (LinearLayout) findViewById(R.id.bills_form_llt);

        groceryFormLayout = (LinearLayout)findViewById(R.id.grocery_form_llt);
        emergencyFormLayout = (LinearLayout)findViewById(R.id.emergency_form_llt);
        orderFormLayout = (LinearLayout)findViewById(R.id.order_form_llt);


        billsTimeTxt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                billsTimePicker.setVisibility(View.VISIBLE);
                groceryTimePicker.setVisibility(View.GONE);
                emergencyTimePicker.setVisibility(View.GONE);
                foodTimePicker.setVisibility(View.GONE);

            }
        });
        groceryTimeTxt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                billsTimePicker.setVisibility(View.VISIBLE);
                groceryTimePicker.setVisibility(View.GONE);
                emergencyTimePicker.setVisibility(View.GONE);
                foodTimePicker.setVisibility(View.GONE);

            }
        });
        emergencyTimeTxt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                billsTimePicker.setVisibility(View.GONE);
                groceryTimePicker.setVisibility(View.GONE);
                emergencyTimePicker.setVisibility(View.VISIBLE);
                foodTimePicker.setVisibility(View.GONE);
            }
        });
        orderTimeTxt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                billsTimePicker.setVisibility(View.GONE);
                groceryTimePicker.setVisibility(View.GONE);
                emergencyTimePicker.setVisibility(View.GONE);
                foodTimePicker.setVisibility(View.VISIBLE);
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(Dashboard.this, ProfileViewActivity.class);
                startActivity(profileIntent);
            }
        });


        billsLayout = (LinearLayout)findViewById(R.id.bills_llt);
        groceryLayout = (LinearLayout)findViewById(R.id.grocery_llt);
        emergencyLayout = (LinearLayout)findViewById(R.id.emergency_llt);
        orderLayout = (LinearLayout)findViewById(R.id.food_llt);



        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billsFormLayout.setVisibility(View.VISIBLE);
                groceryFormLayout.setVisibility(View.GONE);
                emergencyFormLayout.setVisibility(View.GONE);
                orderFormLayout.setVisibility(View.GONE);
            }
        });
        orderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billsFormLayout.setVisibility(View.GONE);
                groceryFormLayout.setVisibility(View.GONE);
                emergencyFormLayout.setVisibility(View.GONE);
                orderFormLayout.setVisibility(View.VISIBLE);
            }
        });
        groceryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billsFormLayout.setVisibility(View.GONE);
                groceryFormLayout.setVisibility(View.VISIBLE);
                emergencyFormLayout.setVisibility(View.GONE);
                orderFormLayout.setVisibility(View.GONE);
            }
        });
        emergencyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billsFormLayout.setVisibility(View.GONE);
                groceryFormLayout.setVisibility(View.GONE);
                emergencyFormLayout.setVisibility(View.VISIBLE);
                orderFormLayout.setVisibility(View.GONE);
            }
        });

        postBills.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String desc = DescET.getText().toString();
                dest_time = billsTimePicker.getHour() + ":" + billsTimePicker.getMinute();

                if (TextUtils.isEmpty(desc) || TextUtils.isEmpty(dest_time))
                {
                    Toast.makeText(Dashboard.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("time",dest_time);
                    hashMap.put("desc",desc);
                    hashMap.put("status", "pending");
                    hashMap.put("poster", currentUserID);
                    hashMap.put("type","BILL");
                    hashMap.put("poster_location",address);

                    firestore.collection("Posts").document().set(hashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.P)
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Dashboard.this, "Posted successfully", Toast.LENGTH_SHORT).show();
                                        billsTimePicker.resetPivot();
                                        DescET.setText(null);
                                        billsFormLayout.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        String err = task.getException().getMessage();
                                        Toast.makeText(Dashboard.this, err, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }





            }
        });
        postGrocery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String desc = groceryET.getText().toString();
                dest_time = groceryTimePicker.getHour() + ":" + groceryTimePicker.getMinute();

                if (TextUtils.isEmpty(desc) || TextUtils.isEmpty(dest_time))
                {
                    Toast.makeText(Dashboard.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("time",dest_time);
                    hashMap.put("desc",desc);
                    hashMap.put("status", "pending");
                    hashMap.put("poster", currentUserID);
                    hashMap.put("type","GROCERY");
                    hashMap.put("poster_location",address);

                    firestore.collection("Posts").document().set(hashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.P)
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Dashboard.this, "Posted successfully", Toast.LENGTH_SHORT).show();
                                        groceryTimePicker.resetPivot();
                                        groceryET.setText(null);
                                        groceryFormLayout.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        String err = task.getException().getMessage();
                                        Toast.makeText(Dashboard.this, err, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }





            }
        });
        postOrder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String desc = orderET.getText().toString();
                dest_time = foodTimePicker.getHour() + ":" + foodTimePicker.getMinute();

                if (TextUtils.isEmpty(desc) || TextUtils.isEmpty(dest_time))
                {
                    Toast.makeText(Dashboard.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("time",dest_time);
                    hashMap.put("desc",desc);
                    hashMap.put("status", "pending");
                    hashMap.put("poster", currentUserID);
                    hashMap.put("type","FOOD");
                    hashMap.put("poster_location",address);

                    firestore.collection("Posts").document().set(hashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.P)
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Dashboard.this, "Posted successfully", Toast.LENGTH_SHORT).show();
                                        foodTimePicker.resetPivot();
                                        orderET.setText(null);
                                        orderFormLayout.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        String err = task.getException().getMessage();
                                        Toast.makeText(Dashboard.this, err, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }





            }
        });
        postEmergency.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String desc = emergencyET.getText().toString();
                dest_time = emergencyTimePicker.getHour() + ":" + emergencyTimePicker.getMinute();

                if (TextUtils.isEmpty(desc) || TextUtils.isEmpty(dest_time))
                {
                    Toast.makeText(Dashboard.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("time",dest_time);
                    hashMap.put("desc",desc);
                    hashMap.put("status", "pending");
                    hashMap.put("poster", currentUserID);
                    hashMap.put("type","EMERGENCY");
                    hashMap.put("poster_location",address);

                    firestore.collection("Posts").document().set(hashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.P)
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Dashboard.this, "Posted successfully", Toast.LENGTH_SHORT).show();
                                        emergencyTimePicker.resetPivot();
                                        emergencyET.setText(null);
                                        emergencyFormLayout.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        String err = task.getException().getMessage();
                                        Toast.makeText(Dashboard.this, err, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }





            }
        });




        helpSeekerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helperSwitch.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                helperTxt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                helpSeekerSwitch.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                helpSeekerTxt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darker_gray));

                helpSeekerLayout.setVisibility(View.VISIBLE);
                helperLayout.setVisibility(View.GONE);

                user_type = "helper";

            }
        });

        helperSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpSeekerSwitch.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                helpSeekerTxt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                helperSwitch.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                helperTxt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darker_gray));

                helperLayout.setVisibility(View.VISIBLE);
                helpSeekerLayout.setVisibility(View.GONE);

                user_type = "help_seeker";

            }
        });


        Query query = firestore.collection("Posts");

        FirestoreRecyclerOptions<Requests> firestoreRecyclerOptions
                = new FirestoreRecyclerOptions.Builder<Requests>()
                .setQuery(query, Requests.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Requests, RequestsViewHolder>(firestoreRecyclerOptions)
        {

            @NonNull
            @Override
            public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requests_layout, parent, false);

                view.findViewById(R.id.requests_help_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("https")
                                    .authority("www.google.com")
                                    .appendPath("maps")
                                    .appendPath("dir")
                                    .appendPath("")
                                    .appendQueryParameter("api", "1")
                                    .appendQueryParameter("destination", latlon_target);
                            String url = builder.build().toString();
                            Log.d("Directions", url);
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);

                        
                    }
                });

                return new RequestsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RequestsViewHolder holder, int position, @NonNull Requests model) {

                holder.setDesc(model.getDesc());
                holder.setTime(model.getTime());
                holder.setPoster_location(model.getPoster_location());
                holder.setType(model.getType());
                latlon_target = model.getLatlon();

                String poster_id = model.getPoster();

                firestore.collection("Users").document(poster_id)
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                if (value!=null)
                                {
                                    String name = value.get("fullname").toString();
                                    String imageURL = value.get("profile_picture").toString();
                                    holder.setImage(imageURL);
                                    holder.setName(name);

                                }
                            }
                        });

            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        requestsView.setLayoutManager(linearLayoutManager);

        requestsView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

