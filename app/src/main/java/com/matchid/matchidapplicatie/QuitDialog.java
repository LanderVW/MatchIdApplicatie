package com.matchid.matchidapplicatie;


import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by lander on 05/04/2017.
 */

public class QuitDialog extends DialogFragment implements View.OnClickListener{
    Button leave, stay;
    Communicator communicator;

    /**
     * *called once the class is associated with its activity.

     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    /**
     * declaration of all btns
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return  View
     */
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quit_dialog,null);
        leave = (Button) view.findViewById(R.id.leave );
        stay = (Button) view.findViewById(R.id.stay);
        leave.setOnClickListener(this);
        stay.setOnClickListener(this);
        setCancelable(false);

        return view;

    }

    /**
     * bepaald wat gebeurd na drukken op een knop
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.leave){
            dismiss();
            communicator.onDialogMessage("leave");
            getActivity().finish();
            System.exit(0);
        }else{
            communicator.onDialogMessage("stay");
            dismiss();
        }
    }

    /**
     * interface om message weer te geven
     */
    interface Communicator{
        public void onDialogMessage(String message);
    }
}

