package howest.desopdrachtmobileapp;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import howest.desopdrachtmobileapp.Loader.Contract;
import howest.desopdrachtmobileapp.Loader.PlacesLoader;

/**
 * Created by chris on 30/03/15.
 */
public class MainFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    View rootView;
    SimpleCursorAdapter mAdapter;

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor = cursor;
        Cursor c = cursor;
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

            TextView naam = (TextView) view.findViewById(R.id.txtnaam);
            TextView straat = (TextView) view.findViewById(R.id.txtstraat);
            naam.setText(cursor.getString(naamIndex));
            straat.setText(cursor.getString(straatIndex));
        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.
        //create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new PlacesLoader(getActivity());
    }

}
