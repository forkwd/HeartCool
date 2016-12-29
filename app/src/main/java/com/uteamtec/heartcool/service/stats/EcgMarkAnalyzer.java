package com.uteamtec.heartcool.service.stats;

import com.uteamtec.heartcool.service.listener.ListenerMgr;
import com.uteamtec.heartcool.service.type.EcgMark;
import com.uteamtec.heartcool.service.type.GlobalVar;
import com.uteamtec.heartcool.service.type.UserDevice;
import com.uteamtec.heartcool.utils.L;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * EcgMark解析器
 * Created by wd
 */
public final class EcgMarkAnalyzer {

    private Timer timer = null;
    private TimerTask timerTask = null;
    private volatile long seconds = 0;

    public long getSeconds() {
        return seconds;
    }

    public String getSecondsFormat() {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d",
                seconds / 3600, seconds % 3600 / 60, seconds % 60);
    }

    private long HRTotal = 0;
    private long HRCount = 0;
    private long HRHealthCount = 0;

    private long BRTotal = 0;
    private long BRCount = 0;
    private long BRHealthCount = 0;

    public EcgMarkAnalyzer() {
    }

    public synchronized void startRecord() {
        if (timer != null || timerTask != null) {
            return;
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                seconds++;
                if (ListenerMgr.hasDetectionListener()) {
                    ListenerMgr.getDetectionListener().onTimerTick(getSecondsFormat());
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    public synchronized void stopRecord() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public synchronized void clear() {
        seconds = 0;

        HRTotal = 0;
        HRCount = 0;
        HRHealthCount = 0;

        BRTotal = 0;
        BRCount = 0;
        BRHealthCount = 0;
    }

    public synchronized void recordMark(EcgMark m) {
        if (m == null) {
            return;
        }
        switch (m.getTypeGroup()) {
            case EcgMark.TYPE_GROUP_STATUS:
                displayMark(m);
                break;
            case EcgMark.TYPE_GROUP_PHYSIO:
                final int VALUE = m.getValue();
                if (VALUE < 0) {
                    return;
                }
                switch (m.getType()) {
                    case EcgMark.PHYSIO_HR:
                        HRTotal += VALUE;
                        HRCount++;
                        if (VALUE >= 60 && VALUE <= 80) {
                            HRHealthCount++;
                        }
                        if (ListenerMgr.hasEcgMarkListener()) {
                            ListenerMgr.getEcgMarkListener().onMarkHR(VALUE,
                                    getAverageHR(), getHealthHR());
                        }
                        break;
                    case EcgMark.PHYSIO_BR:
                        BRTotal += VALUE;
                        BRCount++;
                        BRHealthCount++;
                        if (ListenerMgr.hasEcgMarkListener()) {
                            ListenerMgr.getEcgMarkListener().onMarkBR(VALUE);
                        }
                        break;
                    case EcgMark.PHYSIO_NOISE:
                        if (ListenerMgr.hasEcgMarkListener()) {
                            ListenerMgr.getEcgMarkListener().onMarkNoise("噪声");
                        }
                        break;
                }
                break;
        }
    }

    public void displayMark(EcgMark m) {
        if (m == null) {
            return;
        }
        L.e("EcgMarkAnalyzer.displayMark: " + m.toString());
        final int VALUE = m.getValue();
        switch (m.getTypeGroup()) {
            case EcgMark.TYPE_GROUP_STATUS:
                switch (m.getType()) {
                    case EcgMark.STATUS_LEADOFF:
                        String msg = "导联脱落";
                        if (GlobalVar.getUser().getUserDevice().getModel() == UserDevice.MODEL_20_1) {
                            msg = "导联脱落";
                        } else if (GlobalVar.getUser().getUserDevice().getModel() == UserDevice.MODEL_20_3) {
                            boolean chn1Off = ((VALUE & 0x01) == 1);
                            boolean chn2Off = ((VALUE & 0x02) == 2);
                            boolean chn3Off = ((VALUE & 0x04) == 4);
                            String ch1 = "1";
                            String ch2 = "2";
                            String ch3 = "3";
                            String combo = (chn1Off ? ch1 : " ") + (chn2Off ? ch2 : " ") + (chn3Off ? ch3 : " ");
                            msg = "导联" + combo + "脱落";
                        }
                        if (ListenerMgr.hasEcgMarkListener()) {
                            ListenerMgr.getEcgMarkListener().onMarkLeadOff(msg);
                        }
                        break;
                    case EcgMark.STATUS_LOWPOWER:
                        if (ListenerMgr.hasEcgMarkListener()) {
                            ListenerMgr.getEcgMarkListener().onMarkLowPower("电量不足");
                        }
                        break;
                    case EcgMark.STATUS_SHORT:
                        if (ListenerMgr.hasEcgMarkListener()) {
                            ListenerMgr.getEcgMarkListener().onMarkShort("短路");
                        }
                        break;
                    case EcgMark.STATUS_UNPLUG:
                        if (ListenerMgr.hasEcgMarkListener()) {
                            ListenerMgr.getEcgMarkListener().onMarkUnplug("拔下");
                        }
                        break;
                }
                break;
            case EcgMark.TYPE_GROUP_PHYSIO:
                if (VALUE < 0) {
                    return;
                }
                switch (m.getType()) {
                    case EcgMark.PHYSIO_HR:
                        if (ListenerMgr.hasEcgMarkListener()) {
                            ListenerMgr.getEcgMarkListener().onMarkHR(VALUE, -1, -1);
                        }
                        break;
                    case EcgMark.PHYSIO_BR:
                        if (ListenerMgr.hasEcgMarkListener()) {
                            ListenerMgr.getEcgMarkListener().onMarkBR(VALUE);
                        }
                        break;
                    case EcgMark.PHYSIO_NOISE:
                        if (ListenerMgr.hasEcgMarkListener()) {
                            ListenerMgr.getEcgMarkListener().onMarkNoise("噪声");
                        }
                        break;
                }
                break;
        }
    }

    public int getAverageHR() {
        if (HRCount <= 0) {
            return 0;
        }
        return (int) (HRTotal / HRCount);
    }

    public int getHealthHR() {
        if (HRCount <= 0) {
            return 0;
        }
        return (int) (HRHealthCount * 100 / HRCount);
    }

    public int getAverageBR() {
        if (BRCount <= 0) {
            return 0;
        }
        return (int) (BRTotal / BRCount);
    }

    public EcgMarkReport getReport() {
        return new EcgMarkReport(this);
    }

}
