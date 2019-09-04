package com.example.newtask.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newtask.R;
import com.example.newtask.adapter.CustomAdapter;
import com.example.newtask.model.Coordinate;
import com.example.newtask.model.Taxi;
import com.example.newtask.viewmodel.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int REQUEST_CODE = 101;
    private GoogleMap gMap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private BottomSheetBehavior mBottomSheetBehavior;
    private LinearLayout linearLayout;
    private MainViewModel mainViewModel;
    private List<Taxi> listAll = new ArrayList<>();
    private List<Taxi> listTaxi = new ArrayList<>();
    private List<Taxi> listPolling = new ArrayList<>();
    private List<Taxi> list = new ArrayList<>();
    RadioGroup radioGroup;
    Button btn;
    TextView txt;
    ImageView img;
    Marker marker;
    RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    MapView mapView;
    LatLng temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.map);
        mapView.onCreate(mapViewBundle);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        radioGroup = findViewById(R.id.radio_group);
        recyclerView = findViewById(R.id.cars_view);
        linearLayout = findViewById(R.id.linear1);
        btn = findViewById(R.id.cancel_button);
        txt = findViewById(R.id.text_id);
        img = findViewById(R.id.img_type);
        final View bottonSheet = findViewById(R.id.bottom_sheet);
        recyclerView.setLayoutManager(layoutManager);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottonSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        linearLayout.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        customAdapter = new CustomAdapter(this, list);
        recyclerView.setAdapter(customAdapter);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getCars().observe(this, new Observer<List<Taxi>>() {
            @Override
            public void onChanged(List<Taxi> taxis) {
                list.addAll(taxis);
                listAll.addAll(list);
                customAdapter.notifyDataSetChanged();
                mapView.getMapAsync(MainActivity.this);
                for (int i = 0; i < 30; i++) {
                    if (taxis.get(i).getFleetType().equals("TAXI")) {
                        listTaxi.add(taxis.get(i));
                    } else if (taxis.get(i).getFleetType().equals("POOLING")) {
                        listPolling.add(taxis.get(i));
                    }
                }
            }
        });
        customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                txt.setText(Integer.toString(listAll.get(position).getId()));
                if (listAll.get(position).getFleetType().equals("TAXI")) {
                    img.setImageResource(R.drawable.car_taxi);
                } else {
                    img.setImageResource(R.drawable.pooliing);
                }
                Coordinate coordinate = listAll.get(position).getCoordinate();
                LatLng lan = new LatLng(coordinate.getLatitude(),coordinate.getLongitude());
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lan, 15.1f));
                btn.setVisibility(View.VISIBLE);

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                txt.setText("No Vehicle Selected");
                img.setImageResource(R.drawable.taxi);
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(temp, 10f));
                btn.setVisibility(View.GONE);
            }
        });

    }


    public void createMethod(View view) {
        boolean check = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_button_all:
                if (check) {
                    list.clear();
                    list.addAll(listAll);
                    recyclerView.setAdapter(customAdapter);
                    Toast.makeText(this, "all", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.radio_button_pooling:
                if (check) {
                    list.clear();
                    list.addAll(listPolling);
                    recyclerView.setAdapter(customAdapter);
                    Toast.makeText(this, "Pooling", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.radio_button_taxi:
                if (check) {
                    list.clear();
                    list.addAll(listTaxi);
                    recyclerView.setAdapter(customAdapter);
                    Toast.makeText(this, "Taxi", Toast.LENGTH_SHORT).show();
                }
                break;


        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        BitmapDrawable bitmapdraw;
        Bitmap b;
        Bitmap iconCar = null;
        for (int i = 0; i < 30; i++) {
            MarkerOptions markerOptions;
            Coordinate coordinate;
            if (listAll.get(i).getFleetType().equals("TAXI")) {
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.map_taxi);
                b = bitmapdraw.getBitmap();
                iconCar = Bitmap.createScaledBitmap(b, 150, 150, false);

                coordinate = listAll.get(i).getCoordinate();
                double heading = listAll.get(i).getHeading();
                markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconCar)).title(listAll.get(i).getFleetType()).snippet(Integer.toString(listAll.get(i).getId())).rotation(((float) heading));

            } else {
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.map_car);
                b = bitmapdraw.getBitmap();
                iconCar = Bitmap.createScaledBitmap(b, 150, 150, false);
                coordinate = listAll.get(i).getCoordinate();
                double heading = listAll.get(i).getHeading();
                markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconCar)).title(listAll.get(i).getFleetType()).snippet(Integer.toString(listAll.get(i).getId())).rotation(((float) heading));
            }

            final LatLng latLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
            temp = latLng;
            marker = gMap.addMarker(markerOptions.position(latLng).flat(true));
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));

            gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15.1f));
                    btn.setVisibility(View.VISIBLE);
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    txt.setText(marker.getSnippet());
                    if(marker.getTitle().equals("TAXI"))
                    {
                        img.setImageResource(R.drawable.car_taxi);
                    }
                    else
                    {
                        img.setImageResource(R.drawable.pooliing);
                    }

                    return true;
                }
            });
        }
    }
}