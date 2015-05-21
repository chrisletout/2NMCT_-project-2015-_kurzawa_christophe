package howest.desopdrachtmobileapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import howest.desopdrachtmobileapp.Loader.Contract;
import howest.desopdrachtmobileapp.Loader.PlacesLoader;

/**
 * Created by chris on 30/03/15.
 */
public class MainFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {
    SimpleCursorAdapter mAdapter;
    ListClicked mCallback;
    String[] types;
    private String grid_currentQuery = null;
    Boolean cursorloaded = false;
    Cursor firstcursor;



    public interface ListClicked{
        public void sendLocation(String text, float lat, float longetitude);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ListClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursorloaded == false) {
            mAdapter.swapCursor(cursor);
            firstcursor = mAdapter.getCursor();
            cursorloaded = true;
        }
        else
            mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = (Cursor)mAdapter.getItem(position);
        c.moveToPosition(position);
        String name = c.getString(c.getColumnIndex(Contract.placesColumns.COLUMN_NAAM));
        float lat = c.getFloat(c.getColumnIndex(Contract.placesColumns.COLUMN_LAT));
        float longetitude = c.getFloat(c.getColumnIndex(Contract.placesColumns.COLUMN_LONG));
        mCallback.sendLocation(name, lat, longetitude);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //get communicatie
        try {
            Bundle args = getArguments();
            types = args.getStringArray("types");
        }catch (Exception ex){}

        //end communicatie

        String[] columns = new String[] {
                Contract.placesColumns.COLUMN_NAAM,
                Contract.placesColumns.COLUMN_STRAAT,
        };
        int[] viewIds = new int[]{R.id.txtnaam, R.id.txtstraat};
        mAdapter = new PlacesAdapter(getActivity(),R.layout.row, null,columns, viewIds, 0);

        setListAdapter(mAdapter);
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }


    public class PlacesAdapter extends SimpleCursorAdapter{

        public PlacesAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
            super(context, layout, c, from, to);
        }

        public PlacesAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);
            int naamIndex = cursor.getColumnIndex(Contract.placesColumns.COLUMN_NAAM);
            int straatIndex = cursor.getColumnIndex(Contract.placesColumns.COLUMN_STRAAT);
            int latIndex = cursor.getColumnIndex(Contract.placesColumns.COLUMN_LAT);
            int longetitudeIndex = cursor.getColumnIndex(Contract.placesColumns.COLUMN_LONG);
            int imageIndex = cursor.getColumnIndex(Contract.placesColumns.COLUMN_TYPE);

            TextView naam = (TextView) view.findViewById(R.id.txtnaam);
            TextView straat = (TextView) view.findViewById(R.id.txtstraat);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            naam.setText(cursor.getString(naamIndex));
            straat.setText(cursor.getString(straatIndex));
//            image.
            Ion.with(context)
                    .load(cursor.getString(imageIndex))
                    .withBitmap()
                    .intoImageView(image);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menulist, menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
//        searchView.setOnQueryTextListener(queryListener);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
//                getActivity().getActionBar().setSubtitle("List");
            grid_currentQuery = null;
        } else {
//                getActivity().getActionBar().setSubtitle("List - Searching for: " + newText);
            grid_currentQuery = s;

        }
        getLoaderManager().restartLoader(0, null, this);
        return false;
    }
//    final private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
//
//        @Override
//        public boolean onQueryTextChange(String newText) {
//            if (TextUtils.isEmpty(newText)) {
////                getActivity().getActionBar().setSubtitle("List");
//                grid_currentQuery = null;
//            } else {
////                getActivity().getActionBar().setSubtitle("List - Searching for: " + newText);
//                grid_currentQuery = newText;
//
//            }
//            getLoaderManager().restartLoader(0, null, MainFragment.this);
//            return false;
//        }
//
//        @Override
//        public boolean onQueryTextSubmit(String query) {
//            Toast.makeText(getActivity(), "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.
        //create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        if(grid_currentQuery==null || grid_currentQuery=="")
            return new PlacesLoader(getActivity(), types);
        else
            return new PlacesLoader(getActivity(), grid_currentQuery,firstcursor);
    }

}
