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

import java.util.zip.Inflater;

public class DirectionFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final double latitude = 50.824737;
    public final double longitude = 3.249512;
    public final double[] schoollatlng = {50.824737,3.249512};
    TextClicked mCallback;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_direction, container, false);
        CheckBox school = (CheckBox) v.findViewById(R.id.checkBoxschool);
//        if (school.isChecked()) {
//            mCallback.sendText(schoollatlng);
//        }
        school.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateMapSender(buttonView, isChecked);
            }
        });
        return v;
    }

    private void updateMapSender(CompoundButton buttonView, boolean isChecked) {
        if (isChecked == true) {
            mCallback.sendText(schoollatlng);
        }
    }
}
