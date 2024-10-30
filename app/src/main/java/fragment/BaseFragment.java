package fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    private AlertDialog mProgressDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        builder.setCancelable(false);
        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        builder.setView(progressBar);

        mProgressDialog = builder.create();
        mProgressDialog.show();
    }

    public void closeDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}

