package edu.rit.soundtestmobile;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class SoundActivity extends Activity {
    private TextView frequencyLabel = null;
    private SeekBar frequencyControl = null;

    private TextView delayLabel = null;
    private SeekBar delayControl = null;

    private Button playControl = null;

    private SoundService mService;
    private boolean mBound = false;

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            SoundService.LocalBinder binder = (SoundService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            frequencyLabel.setText("Frequency: " + mService.getCurrentFrequency());
            frequencyControl.setProgress(mService.getCurrentFrequency());

            delayLabel.setText("Delay: " + mService.getCurrentDelay());
            delayControl.setProgress(mService.getCurrentDelay());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        frequencyLabel = (TextView) findViewById(R.id.frequency_text);
        frequencyControl = (SeekBar) findViewById(R.id.frequency_bar);
        frequencyControl.setMax(mService.MAX_FREQUENCY_VALUE);

        frequencyControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mService.updateFrequency(progress - mService.getCurrentFrequency());
                frequencyLabel.setText("Frequency: " + mService.getCurrentFrequency());
                frequencyControl.setProgress(mService.getCurrentFrequency());
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(SoundActivity.this, "Frequency: " + mService.getCurrentFrequency(), Toast.LENGTH_SHORT)
//                        .show();
            }
        });

        delayLabel = (TextView) findViewById(R.id.delay_text);
        delayControl = (SeekBar) findViewById(R.id.delay_bar);
        delayControl.setMax(mService.MAX_DELAY_VALUE);

        delayControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mService.updateDelay(progress - mService.getCurrentDelay());
                delayLabel.setText("Delay: " + mService.getCurrentDelay());
                delayControl.setProgress(mService.getCurrentDelay());
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(SoundActivity.this, "Delay: " + mService.getCurrentDelay(), Toast.LENGTH_SHORT)
//                        .show();
            }
        });

        playControl = (Button) findViewById(R.id.play_button);
        playControl.setText("Play |> ");
        playControl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mService.isPaused()) {
                    mService.resumeMusic();
                    playControl.setText("Pause || ");
                } else {
                    mService.pauseMusic();
                    playControl.setText("Play |> ");
                }
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //Bind to our service so we can manipulate theMediaPlayer if needed
        Intent intent = new Intent(this, SoundService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
