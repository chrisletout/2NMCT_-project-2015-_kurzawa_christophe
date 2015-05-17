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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import howest.desopdrachtmobileapp.Loader.Contract;
import howest.desopdrachtmobileapp.Loader.PlacesLoader;

/**
 * Created by chris on 30/03/15.
 */
public class MainFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    SimpleCursorAdapter mAdapter;
    String[] types;

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.
        //create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new PlacesLoader(getActivity(), types);
    }

}
