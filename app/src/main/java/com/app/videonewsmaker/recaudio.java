package com.app.videonewsmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;


public class recaudio extends AppCompatActivity {
    Button process;
    VideoView videoView;
    Button record,play,stop;
    String outputFile;
    String videopath;
    MediaController mediaC;
    MediaRecorder myAudioRecorder;
    int color;
    Bundle bundle;
    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context=this;
        outputFile = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/recording.mp4";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recaudio);
        videoView=findViewById(R.id.videoviewer);
        mediaC=new MediaController(this);
        process=findViewById(R.id.process);
        record=findViewById(R.id.rec);
        play=findViewById(R.id.play);
        stop=findViewById(R.id.stop);
        play.setEnabled(false);
        stop.setEnabled(false);
        play.setBackgroundResource(R.mipmap.playdisabled);
        stop.setBackgroundResource(R.mipmap.stopdisabled);
        bundle=getIntent().getExtras();
        assert bundle != null;
        videopath=bundle.getString("videopath");
        videoView.setVideoURI(Uri.parse(videopath));
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {
               stopaudiorecording();

            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f, 0f);
            }
        });
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
        process.setVisibility(View.GONE);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    // make something
                }
                record.setEnabled(false);
                stop.setEnabled(true);
                record.setBackgroundResource(R.mipmap.recorddisabled);
                stop.setBackgroundResource(R.mipmap.stop);
                videoView.start();
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopaudiorecording();
                videoView.stopPlayback();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    // make something
                }

            }
        });
        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(recaudio.this,activity_step3.class);
                intent.putExtra("newaudiovideo",outputFile);
                intent.putExtra("outputfileafterrec",videopath);
                intent.putExtra("logo",(Uri) bundle.get("logo"));
                intent.putExtra("city_name",(String) bundle.get("city_name"));
                intent.putExtra("reporter_name",(String) bundle.get("reporter_name"));
                intent.putExtra("reporterphoto",(String) bundle.get("reporterphoto"));
                intent.putExtra("breakingnews1",(String) bundle.get("breakingnews1"));
                intent.putExtra("breakingnews2",(String) bundle.get("breakingnews2"));
                intent.putExtra("brstyle", (int) bundle.get("brstyle"));
                intent.putExtra("vidheight", (int) bundle.get("vidheight"));
                intent.putExtra( "vidwidth",(int) bundle.get("vidwidth"));
                intent.putExtra( "vidrotation",(int) bundle.get("vidrotation"));
                intent.putExtra("remaudiomode",bundle.getInt("remaudiomode"));
                intent.putExtra("remaudiotime",bundle.getInt("remaudiotime"));
                intent.putExtra("promobtnmode",bundle.getInt("promobtnmode"));
                intent.putExtra("editedvideo", bundle.getString("editedvideo"));
                intent.putExtra( "mutedvideo",bundle.getString("mutedvideo"));
                intent.putExtra( "unmutedvideo",bundle.getString("unmutedvideo"));
                intent.putExtra("promovideopath", bundle.getString("promovideopath"));
                intent.putExtra( "videowithpromo",bundle.getString("videowithpromo"));
                intent.putExtra("recaudiomode", 1);
                startActivity(intent);
            }
        });
    }
    void stopaudiorecording(){
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        record.setEnabled(false);
        stop.setEnabled(false);
        play.setEnabled(true);
        process.setVisibility(View.VISIBLE);
        play.setBackgroundResource(R.mipmap.play);
        stop.setBackgroundResource(R.mipmap.stopdisabled);
        record.setBackgroundResource(R.mipmap.recorddisabled);
        Toast.makeText(getApplicationContext(), "Audio Recorded successfully", Toast.LENGTH_LONG).show();
    }


}
