package com.example.bcnbicing;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    FirebaseListAdapter<Stations> adapter;
    FirebaseListOptions<Stations> options;
    DatabaseReference query;
    ImageView icon;
    private static final int RC_SIGN_IN = 123;

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, true);


        Log.w("Icon", String.valueOf(icon));
        ListView lvStations = view.findViewById(R.id.lvStations);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null){
            DoLogin(); }

        query = FirebaseDatabase.getInstance()
                .getReference()
                .child("stations");

        options = new FirebaseListOptions.Builder<Stations>()
                .setQuery(query,Stations.class)
                .setLayout(R.layout.row_stations)
                .build();

        adapter = new FirebaseListAdapter<Stations>(options){
            @Override
            protected void populateView(View v, Stations model, int position) {
                icon = v.findViewById(R.id.ivIcon);
                TextView tvName = v.findViewById(R.id.tvName);
                tvName.setText(model.getStreetName());
                TextView tvType = v.findViewById(R.id.tvtype);
                tvType.setText(model.getType());
                //p(%) = (P / T) x 100 0 % - 25% - 50% - 75% - 100%
                double b = Integer.parseInt(model.getBikes());
                double s = Integer.parseInt(model.getSlots());
                double total = b + s;
                Log.w("Bikes", String.valueOf(b));
                Log.w("Slots", String.valueOf(s));
                Log.w("total", String.valueOf(total));

                double p1 = b / total ;
                double p2 = p1 * 100;
                Log.w("P1", String.valueOf(p2));
                Log.w("P2", String.valueOf(p2));

                if (check(0, (int) p2,24)){
                    icon.setImageResource(R.drawable.parking);
                }
                else if (check(25, (int) p2,49)){
                    icon.setImageResource(R.drawable.pink);
                }
                else if (check(50, (int) p2,74)){
                    icon.setImageResource(R.drawable.orange);
                }
                else if (check(75, (int) p2,99)){
                    icon.setImageResource(R.drawable.yellow);
                }
                else if (p2 == 100){
                    icon.setImageResource(R.drawable.green);
                }

            }
        };

        lvStations.setAdapter(adapter);

        lvStations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Stations station = (Stations) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("station", station);
                startActivity(intent);
            }
        });

        return view;
    }

    private void DoLogin() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }

    public static boolean check(int a, int b, int c) {
        return a<=b && b<=c;
    }
}
