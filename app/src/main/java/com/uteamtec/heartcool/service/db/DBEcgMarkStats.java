package com.uteamtec.heartcool.service.db;

import com.litesuits.orm.db.annotation.Default;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.Table;
import com.uteamtec.heartcool.service.stats.EcgMarkReport;
import com.uteamtec.heartcool.service.type.EcgMark;

/**
 * EcgMarkStats统计数据集
 * Created by wd
 */
@Table("test_ecg_mark_stats")
public class DBEcgMarkStats extends DBModel {

    public DBEcgMarkStats(DBDetection detection) {
        if (detection != null) {
            setHR(detection.getHR());
            setHRHealth(detection.getHRRange());
            setBR(detection.getBR());
            setSQ(detection.getSQ());
            setConclusion(detection.getAbnormal());
        }
    }

    public void setEcgMarkReport(EcgMarkReport report) {
        if (report != null) {
            setHR(report.HR);
            setHRHealth(report.HRHealth);
            setBR(report.BR);
            setSQ(report.smzl);
            setConclusion(report.jcfk);
        }
    }

    public void addMark(DBEcgMark mark) {
        if (mark != null) {
            switch (mark.getMessageType()) {
                case EcgMark.MSG_TYPE_XLGK:
                    addXLGS();
                    break;
                case EcgMark.MSG_TYPE_XLGH:
                    addXLGH();
                    break;
                case EcgMark.MSG_TYPE_XLBQ:
                    addXLBQ();
                    break;
                case EcgMark.MSG_TYPE_SXZB:
                    addSXZB();
                    break;
                case EcgMark.MSG_TYPE_FXZB:
                    addFXZB();
                    break;
                case EcgMark.MSG_TYPE_SC:
                    addSC();
                    break;
                case EcgMark.MSG_TYPE_FC:
                    addFC();
                    break;
                default:
                    break;
            }
        }
    }

    @Default("未知")
    @NotNull
    private String SQ; // 信号质量

    @Default("0")
    @NotNull
    private int HR; // 心律

    @Default("0")
    @NotNull
    private int HRHealth; // 心律健康指数

    @Default("0")
    @NotNull
    private int BR; // 呼吸率

    @Default("0")
    @NotNull
    private long XLBQ; // 心律不齐

    @Default("0")
    @NotNull
    private long XLGS; // 心律过速

    @Default("0")
    @NotNull
    private long XLGH; // 心律过缓

    @Default("0")
    @NotNull
    private long SXZB; // 室性早搏

    @Default("0")
    @NotNull
    private long FXZB; // 房性早搏

    @Default("0")
    @NotNull
    private long SC; // 室颤

    @Default("0")
    @NotNull
    private long FC; // 房颤

    @Default("未知")
    @NotNull
    private String conclusion; // 监测结论

    public String getSQ() {
        return SQ;
    }

    public void setSQ(String SQ) {
        this.SQ = SQ;
    }

    public int getHR() {
        return HR;
    }

    public void setHR(int HR) {
        this.HR = HR;
    }

    public int getHRHealth() {
        return HRHealth;
    }

    public void setHRHealth(int HRHealth) {
        this.HRHealth = HRHealth;
    }

    public int getBR() {
        return BR;
    }

    public void setBR(int BR) {
        this.BR = BR;
    }

    public long getXLBQ() {
        return XLBQ;
    }

    public void setXLBQ(long XLBQ) {
        this.XLBQ = XLBQ;
    }

    public void addXLBQ() {
        this.XLBQ++;
    }

    public long getXLGS() {
        return XLGS;
    }

    public void setXLGS(long XLGS) {
        this.XLGS = XLGS;
    }

    public void addXLGS() {
        this.XLGS++;
    }

    public long getXLGH() {
        return XLGH;
    }

    public void setXLGH(long XLGH) {
        this.XLGH = XLGH;
    }

    public void addXLGH() {
        this.XLGH++;
    }

    public long getSXZB() {
        return SXZB;
    }

    public void setSXZB(long SXZB) {
        this.SXZB = SXZB;
    }

    public void addSXZB() {
        this.SXZB++;
    }

    public long getFXZB() {
        return FXZB;
    }

    public void setFXZB(long FXZB) {
        this.FXZB = FXZB;
    }

    public void addFXZB() {
        this.FXZB++;
    }

    public long getSC() {
        return SC;
    }

    public void setSC(long SC) {
        this.SC = SC;
    }

    public void addSC() {
        this.SC++;
    }

    public long getFC() {
        return FC;
    }

    public void setFC(long FC) {
        this.FC = FC;
    }

    public void addFC() {
        this.FC++;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DBEcgMarkStats [id=" + id + ", " +
                "SQ=" + SQ + ", HR=" + HR + ", HRHealth=" + HRHealth + ", BR=" + BR + ", " +
                "XLBQ=" + XLBQ + ", XLGS=" + XLGS + ", XLGH=" + XLGH + ", " +
                "SXZB=" + SXZB + ", FXZB=" + FXZB + ", SC=" + SC + ", FC=" + FC
        );
        sb.append("] \n");
        return sb.toString();
    }

}
