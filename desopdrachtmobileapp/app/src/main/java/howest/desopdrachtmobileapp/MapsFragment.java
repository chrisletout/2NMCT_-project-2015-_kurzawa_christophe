package howest.desopdrachtmobileapp;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tagmanager.Container;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chris on 30/03/15.
 */
public class MapsFragment extends android.support.v4.app.Fragment {
    View rootView;
    private GoogleMap map;
    private SupportMapFragment fragment;
    private String[] types;
    ArrayList<com.google.gson.JsonObject> results = new ArrayList<>();
    public Bitmap img = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        rootView = inflater.inflate(R.layout.fragment_maps, container, false);
//        return rootView;

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, Menu.FIRST, Menu.NONE, "Toon alle geselecteerde plaatsen");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getContentAndAddMarker();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menulist, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 2));

        // Other supported types include: MAP_TYPE_NORMAL,
        // MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID and MAP_TYPE_NONE
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = fragment.getMap();
            if(getArguments().getString("Location") == null)
                getContentAndAddMarker();
            else{
                map.clear();
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(getArguments().getFloat("lat"), getArguments().getFloat("long")))
                        .title(getArguments().getString("Location")));

            }
//                map.addMarker(new MarkerOptions().position(new LatLng(school[0], school[1])));
//            map.setMyLocationEnabled(true);
        }
    }
    public void updateLoaction(String text, float lat, float longetitude){
        map.clear();
        map.addMarker(new MarkerOptions()
                .position(new LatLng(lat, longetitude))
                .title(text));
    }

    public void getContentAndAddMarker() {
        types = MainActivity.getmStringArray();
        map.clear();
        for (String var : types)
            Ion.with(getActivity())
                    .load("https://maps.googleapis.com/maps/api/place/textsearch/json?query="+var+"+in+kortrijk&key=AIzaSyA8EFLUvaxaQccnvKQxA1W0uhep9GzGfrU")
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            if(e == null){
                            JsonArray json = result.getAsJsonArray("results");
                            for(int i = 0; i<json.size();i++){
                                JsonElement results = json.get(i);
                                JsonObject res = results.getAsJsonObject();
                                JsonElement adres = res.get("formatted_address");
                                final JsonElement name = res.get("name");
                                JsonElement url = res.get("icon");
                                JsonObject geometry = res.getAsJsonObject("geometry");
                                JsonObject location = geometry.getAsJsonObject("location");
                                final JsonElement lat = location.get("lat");
                                final JsonElement lng = location.get("lng");

                                Ion.with(getActivity())
                                        .load(url.getAsString())
                                        .withBitmap()
                                        .asBitmap()
                                        .setCallback(new FutureCallback<Bitmap>() {
                                            @Override
                                            public void onCompleted(Exception e, Bitmap result) {
                                                img = result;
                                                map.addMarker(new MarkerOptions()
                                                        .position(new LatLng(lat.getAsDouble(), lng.getAsDouble()))
                                                        .title(name.getAsString())
                                                        .icon(BitmapDescriptorFactory.fromBitmap(img)));
                                            }
                                        });
                            }}
                        }
                    });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();
        }
    }
}
