package com.uteamtec.heartcool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.uteamtec.heartcool.R;
import com.uteamtec.heartcool.service.db.DBDetection;
import com.uteamtec.heartcool.service.db.DBEcgMark;
import com.uteamtec.heartcool.service.net.AppNetTcpComm;
import com.uteamtec.heartcool.service.net.AppNetTcpCommListener;
import com.uteamtec.heartcool.service.share.ShareSDKUtils;
import com.uteamtec.heartcool.service.type.EcgMark;
import com.uteamtec.heartcool.service.type.EcgMarks;
import com.uteamtec.heartcool.service.type.GlobalVar;
import com.uteamtec.heartcool.service.type.MobclickEvent;
import com.uteamtec.heartcool.utils.L;

import java.util.List;
import java.util.Locale;

/**
 * Created by wd
 */
public class HistoryDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private DBDetection detection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            goBack();
            return;
        }
        detection = (DBDetection) intent.getSerializableExtra(DBDetection.class.getSimpleName());
        if (detection == null) {
            goBack();
            return;
        }
        setContentView(R.layout.history_detail);
        MobclickEvent.onEvent(this, MobclickEvent.EventId_DetectionHistory);
    }

    @Override
    protected void initViews() {
        findViewById(R.id.history_detail_iv_back).setOnClickListener(this);
        findViewById(R.id.history_detail_iv_share).setOnClickListener(this);

        TextView.class.cast(findViewById(R.id.history_detail_tv_detection_date)).
                setText(detection.getDate() + " " +
                        detection.getStartTimeStr() + " - " + detection.getStopTimeStr());
        TextView.class.cast(findViewById(R.id.history_detail_tv_detection_time)).
                setText(detection.getDuration());

        if (detection.getMarks().isEmpty()) {
            AppNetTcpComm.getEcgMark().queryAppMarkCounts(
                    GlobalVar.getUser().getIdString()
                    , detection.getStartTime(), detection.getStopTime(),
                    new AppNetTcpCommListener<List<EcgMarks>>() {
                        @Override
                        public void onResponse(boolean success, List<EcgMarks> response) {
                            L.e("queryAppMarkCounts -> success: " + success);
                            if (success && response != null) {
                                for (EcgMarks ms : response) {
                                    if (ms != null) {
                                        L.e("queryAppMarkCounts -> EcgMarks: " + ms.toString());
                                        for (EcgMark m : ms.getMarks()) {
                                            detection.addMark(new DBEcgMark(m));
                                        }
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showEcgMarks();
                                    }
                                });
                            } else {
                                Toast.makeText(HistoryDetailActivity.this,
                                        R.string.http_conn_net, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            showEcgMarks();
        }
    }

    @Override
    protected boolean enableBackPressedFinish() {
        return false;
    }

    @Override
    protected boolean enableServiceConnection() {
        return false;
    }

    @Override
    public void onServiceConnected() {
    }

    @Override
    public void onServiceDisconnected() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_detail_iv_back:
                goBack();
                break;
            case R.id.history_detail_iv_share:
                ShareSDKUtils.shareContent(this,
                        "平均心律:" +
                                TextView.class.cast(findViewById(R.id.history_detail_tv_heart_rate)).getText() +
                                "\n正常心律范围" +
                                TextView.class.cast(findViewById(R.id.history_detail_tv_heart_health)).getText() +
                                "%");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        this.finish();
    }

    private void showEcgMarks() {
        TextView.class.cast(findViewById(R.id.history_detail_tv_detection_signal)).
                setText(detection.getMarkStats().getSQ());
        TextView.class.cast(findViewById(R.id.history_detail_tv_heart_rate)).
                setText(String.valueOf(detection.getMarkStats().getHR()));
        TextView.class.cast(findViewById(R.id.history_detail_tv_respiratory_rate)).
                setText(String.valueOf(detection.getMarkStats().getBR()));
        TextView.class.cast(findViewById(R.id.history_detail_tv_heart_health)).
                setText(String.valueOf(detection.getMarkStats().getHRHealth()));

        TextView.class.cast(findViewById(R.id.history_detail_tv_arrhythmia)).
                setText(String.format(Locale.getDefault(), "%s 次", detection.getMarkStats().getXLBQ()));
        TextView.class.cast(findViewById(R.id.history_detail_tv_heart_rate_fast)).
                setText(String.format(Locale.getDefault(), "%s 次", detection.getMarkStats().getXLGS()));
        TextView.class.cast(findViewById(R.id.history_detail_tv_heart_rate_slow)).
                setText(String.format(Locale.getDefault(), "%s 次", detection.getMarkStats().getXLGH()));
        TextView.class.cast(findViewById(R.id.history_detail_tv_ventricular_premature_beat)).
                setText(String.format(Locale.getDefault(), "%s 次", detection.getMarkStats().getSXZB()));
        TextView.class.cast(findViewById(R.id.history_detail_tv_room_sex_premature_beat)).
                setText(String.format(Locale.getDefault(), "%s 次", detection.getMarkStats().getFXZB()));
        TextView.class.cast(findViewById(R.id.history_detail_tv_ventricular_fibrillation)).
                setText(String.format(Locale.getDefault(), "%s 次", detection.getMarkStats().getSC()));
        TextView.class.cast(findViewById(R.id.history_detail_tv_atrial_fibrillation)).
                setText(String.format(Locale.getDefault(), "%s 次", detection.getMarkStats().getFC()));

        TextView.class.cast(findViewById(R.id.history_detail_tv_detection_conclusion)).
                setText(detection.getMarkStats().getConclusion());
    }

}
