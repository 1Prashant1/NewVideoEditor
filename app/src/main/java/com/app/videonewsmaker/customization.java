package com.app.videonewsmaker;

import android.net.Uri;

public class customization {
    public Uri mainvideo;
    public String promovideo;
    public String cityname;
    public String reporter_name;
    public Uri logo;
    public Uri reporterphoto;
    public String br1;
    public String br2;
    public int brno;
    String headertxt,footertxt,bodytxt;
    private static final customization ourInstance = new customization();
    public static customization getInstance() {
        return ourInstance;
    }
    customization(){

    }

    public void setBodytxt(String bodytxt) {
        this.bodytxt = bodytxt;
    }

    public void setFootertxt(String footertxt) {
        this.footertxt = footertxt;
    }

    public void setHeadertxt(String headertxt) {
        this.headertxt = headertxt;
    }

    public void setMainvideo(Uri mainvideo) {
        this.mainvideo = mainvideo;
    }

    public void setPromovideo(String promovideo) {
        this.promovideo = promovideo;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public void setReporter_name(String reporter_name) {
        this.reporter_name = reporter_name;
    }

    public void setLogo(Uri logo) {
        this.logo = logo;
    }

    public void setReporterphoto(Uri reporterphoto) {
        this.reporterphoto = reporterphoto;
    }

    public void setBr1(String br1) {
        this.br1 = br1;
    }

    public void setBr2(String br2) {
        this.br2 = br2;
    }

    public void setBrno(int brno) {
        this.brno = brno;
    }

    public Uri getMainvideo() {
        return mainvideo;
    }

    public String getPromovideo() {
        return promovideo;
    }

    public int getBrno() {
        return brno;
    }

    public String getReporter_name() {
        return reporter_name;
    }

    public String getCityname() {
        return cityname;
    }

    public Uri getReporterphoto() {
        return reporterphoto;
    }

    public String getBr1() {
        return br1;
    }

    public Uri getLogo() {
        return logo;
    }

    public String getBr2() {
        return br2;
    }

    public String getBodytxt() {
        return bodytxt;
    }

    public String getFootertxt() {
        return footertxt;
    }

    public String getHeadertxt() {
        return headertxt;
    }
}
