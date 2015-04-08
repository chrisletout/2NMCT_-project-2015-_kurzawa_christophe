package howest.desopdrachtmobileapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tagmanager.Container;

/**
 * Created by chris on 30/03/15.
 */
public class MapsFragment extends android.support.v4.app.Fragment {
    View rootView;
    private GoogleMap map;
    private SupportMapFragment fragment;
    private double[] school;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        rootView = inflater.inflate(R.layout.fragment_maps, container, false);
//        return rootView;
        try {
            Bundle args = getArguments();
            school = args.getDoubleArray("schoollatlng");
        }catch (Exception ex){}

        return inflater.inflate(R.layout.fragment_maps, container, false);
//        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
//        mMap = mMapFragment.getMap();
//
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.map_container, mMapFragment).commit();
//        return view;

//        try {
//            rootView = inflater.inflate(R.layout.fragment_maps, container, false);
//
//
//        } catch (InflateException e) {
//        /* map is already there, just return view as it is */
//        }
//        return rootView;

    }
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0,0), 2));

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
            if(school!=null)
                map.addMarker(new MarkerOptions().position(new LatLng(school[0], school[1])));
            map.setMyLocationEnabled(true);
        }
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
