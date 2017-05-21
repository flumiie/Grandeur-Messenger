package xyz.grand.grandeur.settings;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import xyz.grand.grandeur.R;

public class PinLockSetupActivity extends AppCompatActivity
{
    public static final String TAG = "";
    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_lock_setup);
        toolbar = (Toolbar) findViewById(R.id.toolbar_pin_lock_setup);

        Toast.makeText(getApplicationContext(), "PIN Lock is currently not working just yet", Toast.LENGTH_LONG).show();

        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);

        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
    }

    private PinLockListener mPinLockListener = new PinLockListener()
    {
        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "Pin complete: " + pin);
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };
}