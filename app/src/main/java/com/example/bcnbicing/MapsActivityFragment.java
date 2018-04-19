package com.example.bcnbicing;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

public class MapsActivityFragment extends Fragment {
    private MapView map;
    OverlayItem overItem;
    Drawable marker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        map = (MapView) view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setTilesScaledToDpi(true);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();


        Intent i = getActivity().getIntent();
        final Stations station = (Stations) i.getSerializableExtra("station");
        getActivity().setTitle(station.getStreetName());
        Log.w("Stations", String.valueOf(station.getLatitude()));
        double lat = Double.parseDouble(station.getLatitude());
        double lon = Double.parseDouble(station.getLongitude());
            GeoPoint startPoint = new GeoPoint(lat,lon);
            mapController.setZoom(15);
            mapController.setCenter(startPoint);
           // overItem = new OverlayItem("Favorite", "Position", startPoint);

            Marker marker = new Marker(map);
            marker.setPosition(startPoint);
            marker.setTitle(station.getStreetName()+"\nBicis disponibles: "+station.getBikes());
            map.getOverlays().add(marker);


            return view;
    }

}
