package howest.desopdrachtmobileapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.StatedFragment;

import java.util.zip.Inflater;

public class DirectionFragment extends StatedFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final double[] schoollatlng = {50.824737,3.249512};
    private CheckBox school;
    View v;
    TextClicked mCallback;
    // State saven wanneer het fragment afgesloten wordt
    Bundle savedState;

    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        // For example:
        //outState.putString("text", tvSample.getText().toString());
        outState.putBoolean("school", school.isChecked());
    }

    /**
     * Restore Fragment's State here
     */
    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        // For example:
        //tvSample.setText(savedInstanceState.getString("text"));
        school.setChecked(savedInstanceState.getBoolean("school"));
    }

    public interface TextClicked{
        public void sendText(double[] text);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (TextClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.fragment_direction, container, false);
        initInstances(v);
        return v;
    }
    private void initInstances(View rootView) {
        // init instance with rootView.findViewById here
        school = (CheckBox) v.findViewById(R.id.checkBoxschool);
//        if (school.isChecked()) {
//            mCallback.sendText(schoollatlng);
//        }
        school.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateMapSender(buttonView, isChecked);
            }
        });
    }

    private void updateMapSender(CompoundButton buttonView, boolean isChecked) {
        if (isChecked == true) {
            mCallback.sendText(schoollatlng);
        }
    }
}
