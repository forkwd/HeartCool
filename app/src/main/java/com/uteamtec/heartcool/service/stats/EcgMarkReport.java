package com.uteamtec.heartcool.service.stats;

import com.uteamtec.heartcool.AeroCardioApp;
import com.uteamtec.heartcool.R;
import com.uteamtec.heartcool.service.utils.DateFormats;

import java.util.Date;

/**
 * Created by wd
 */
public final class EcgMarkReport {

    // 时间指标
    public String jlsj; // 记录时间
    public String jcsc; // 监测时长
    // 常规
    public int HR; // 平均心律
    public String pjxl; // 平均心律
    public int HRHealth; // 心律范围
    public String xlfw; // 心律范围
    public int BR; // 平均呼吸
    public String pjhx; // 平均呼吸
    public String smzl; // 睡眠质量
    // 异常指标
    public String jcfk; // 心律监测反馈
    public String yczb; // 心律异常指标

    private int markSize = 0;

    public EcgMarkReport(EcgMarkAnalyzer analyzer) {
        if (analyzer != null) {
            this.jlsj = DateFormats.YYYY_MM_DD_HH_MM_CN.format(new Date());
            this.jcsc = analyzer.getSecondsFormat();

            this.HR = analyzer.getAverageHR();
            this.pjxl = String.valueOf(this.HR);
            this.HRHealth = analyzer.getHealthHR();
            this.xlfw = String.valueOf(this.HRHealth);
            this.BR = analyzer.getAverageBR();
            this.pjhx = String.valueOf(this.BR);
            if (this.HR <= 100 && this.HR >= 50) {
                this.smzl = AeroCardioApp.getApplication().getString(R.string.sleepQuality);
            } else {
                this.smzl = AeroCardioApp.getApplication().getString(R.string.no);
            }

            if (this.HR <= 0) {
                this.jcfk = AeroCardioApp.getApplication().getString(R.string.arrest);// 心脏骤停
                this.yczb = AeroCardioApp.getApplication().getString(R.string.heartExp);// 心律异常
            } else if (this.HR <= 100 && this.HR >= 84 || this.HR <= 48) {
                this.jcfk = AeroCardioApp.getApplication().getString(R.string.scope);// 部分指标不在正常范围内
                this.yczb = AeroCardioApp.getApplication().getString(R.string.heartExp);// 心律异常
            } else if (this.HR > 100) {
                this.jcfk = AeroCardioApp.getApplication().getString(R.string.fibrillation);// 房颤
                this.yczb = AeroCardioApp.getApplication().getString(R.string.heartExp);// 心律异常
            } else {
                this.jcfk = AeroCardioApp.getApplication().getString(R.string.indicators);// 各项指标均在正常范围内
                this.yczb = AeroCardioApp.getApplication().getString(R.string.no);// 无
            }
        }
    }

    public int getMarkSize() {
        return markSize;
    }

    public void setMarkSize(int markSize) {
        this.markSize = markSize;
    }

}
