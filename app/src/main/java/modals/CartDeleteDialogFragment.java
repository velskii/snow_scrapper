package modals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.DialogFragment;

import com.example.snow_scrapper.R;


public class CartDeleteDialogFragment extends DialogFragment {

    public static CartDeleteDialogFragment newInstance(String title) {
        CartDeleteDialogFragment frag = new CartDeleteDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_one)
                .setTitle(title)
                .setPositiveButton( "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Log.d("modal", "click yes");
//                                ((FragmentAlertDialog)getActivity()).doPositiveClick();
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Log.d("modal", "click cancel");
//                                ((FragmentAlertDialog)getActivity()).doNegativeClick();
                            }
                        }
                )
                .create();
    }


}
