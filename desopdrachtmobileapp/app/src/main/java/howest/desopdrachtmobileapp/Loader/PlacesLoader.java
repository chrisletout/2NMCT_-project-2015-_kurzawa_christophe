package howest.desopdrachtmobileapp.Loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.JsonReader;
import android.util.JsonToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by chris on 12/05/15.
 */
public class PlacesLoader extends AsyncTaskLoader<Cursor> {
    private Cursor mCursor;
    private final String[] mColumnNames = new String[]{
            BaseColumns._ID,
            Contract.placesColumns.COLUMN_NAAM,
            Contract.placesColumns.COLUMN_STRAAT,
            Contract.placesColumns.COLUMN_LAT,
            Contract.placesColumns.COLUMN_LONG,
            Contract.placesColumns.COLUMN_TYPE
    };
    String[] types;
    String text = null;
    private static Object lock = new Object();

    public PlacesLoader(Context context) {
        super(context);
//        this.mCursor = mCursor;
    }
    public PlacesLoader(Context context,String text, Cursor mCursor) {
        super(context);
        this.text = text;
        this.mCursor = mCursor;
//        this.mCursor = mCursor;
    }
    public PlacesLoader(Context context, String[] types) {
        super(context);
        this.types = types;
//        this.mCursor = mCursor;
    }

    @Override
    protected void onStartLoading() {
        if(mCursor != null){
//            deliverResult(mCursor);
            forceLoad();
        }
        if(takeContentChanged() || mCursor == null)
            forceLoad();
    }
    @Override
    public Cursor loadInBackground() {
        if(mCursor == null) {
            if (types == null)
                loadCursor();
            else
                loadCursorTypes();
        }else {
            return filterCursorOnName(text);
        }


        return mCursor;
    }

    private void loadCursorTypes() {
        synchronized (lock){
            if(mCursor != null) return;

            MatrixCursor cursor = new MatrixCursor(mColumnNames);
            InputStream input = null;
            JsonReader reader = null;

            for (String var : types) {
                JSONArray json = null;
                String str = "";
                HttpResponse response;
                HttpClient myClient = new DefaultHttpClient();
                HttpGet myConnection = new HttpGet("https://maps.googleapis.com/maps/api/place/textsearch/json?query="+var+"+in+kortrijk&key=AIzaSyA8EFLUvaxaQccnvKQxA1W0uhep9GzGfrU");
                int id = 1;
                try {
                    response = myClient.execute(myConnection);
                    str = EntityUtils.toString(response.getEntity(), "UTF-8");

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject jArray = new JSONObject(str);
                    json = jArray.getJSONArray("results");

                    while (id - 1 < json.length()) {

                        JSONObject results = json.getJSONObject(id - 1);
                        JSONObject geometry = results.getJSONObject("geometry");
                        JSONObject coordinates = geometry.getJSONObject("location");


                        MatrixCursor.RowBuilder row = cursor.newRow();
                        row.add(id);
                        row.add(results.getString("name"));
                        row.add(results.getString("formatted_address"));
                        row.add(coordinates.getDouble("lat"));
                        row.add(coordinates.getDouble("lng"));
                        row.add(results.getString("icon"));

                        id++;
                    }
//                    mCursor = cursor;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mCursor = cursor;
        }
    }

    private Cursor filterCursorOnName(String name){
        final String[] mColumnNames = new String[]{
                BaseColumns._ID,
                Contract.placesColumns.COLUMN_NAAM,
                Contract.placesColumns.COLUMN_STRAAT,
                Contract.placesColumns.COLUMN_LAT,
                Contract.placesColumns.COLUMN_LONG,
                Contract.placesColumns.COLUMN_TYPE
        };
        MatrixCursor newCursor = new MatrixCursor(mColumnNames);
        int colnr1 = mCursor.getColumnIndex(Contract.placesColumns.COLUMN_NAAM);
        int colnr2 = mCursor.getColumnIndex(Contract.placesColumns.COLUMN_STRAAT);
        int colnr3 = mCursor.getColumnIndex(Contract.placesColumns.COLUMN_LAT);
        int colnr4 = mCursor.getColumnIndex(Contract.placesColumns.COLUMN_LONG);
        int colnr5 = mCursor.getColumnIndex(Contract.placesColumns.COLUMN_TYPE);

        int id = 1;
        if (mCursor.moveToFirst())
            do{
                if(mCursor.getString(colnr1).toLowerCase().contains(name.toLowerCase().trim())){
                    MatrixCursor.RowBuilder row = newCursor.newRow();
                    row.add(id);
                    row.add(mCursor.getString(colnr1));
                    row.add(mCursor.getString(colnr2));
                    row.add(mCursor.getString(colnr3));
                    row.add(mCursor.getString(colnr4));
                    row.add(mCursor.getString(colnr5));

                    id++;
                }
            }while (mCursor.moveToNext());
        return newCursor;
    }

    public void loadCursor(){
        synchronized (lock){
            if(mCursor != null) return;

            MatrixCursor cursor = new MatrixCursor(mColumnNames);
            InputStream input = null;
            JsonReader reader = null;


            JSONArray json = null;
            String str = "";
            HttpResponse response;
            HttpClient myClient = new DefaultHttpClient();
            HttpGet myConnection = new HttpGet("https://maps.googleapis.com/maps/api/place/textsearch/json?query=school+in+kortrijk&key=AIzaSyBq23lNzzJYFV9rAgZiLRmOj0XUl_tQCAw");
            int id =1;
            try {
                response = myClient.execute(myConnection);
                str = EntityUtils.toString(response.getEntity(), "UTF-8");

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try{
                JSONObject jArray = new JSONObject(str);
                json = jArray.getJSONArray("results");

                while (id-1 < json.length()) {

                    JSONObject results = json.getJSONObject(id-1);
                    JSONObject geometry = results.getJSONObject("geometry");
                    JSONObject coordinates = geometry.getJSONObject("location");


                    MatrixCursor.RowBuilder row = cursor.newRow();
                    row.add(id);
                    row.add(results.getString("name"));
                    row.add(results.getString("formatted_address"));
                    row.add(coordinates.getDouble("lat"));
                    row.add(coordinates.getDouble("lng"));
                    row.add(results.getString("icon"));

                    id++;
                }
                mCursor = cursor;
            } catch ( JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
