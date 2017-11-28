package com.ffo.ipiker.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Author: huchunhua
 * Time: 20:37
 * Package: com.ffo.ipiker.model
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class ReportInfo implements Serializable{

    private int type; //举报类型：1-图片举报，2-视频举报
    private String url_vid;//短视频信息url
    private String url_img;//图片信息url
    private Long time;//时间
    private String location;//地点
    private String describe;//详情描述

    public ReportInfo() {
    }

    public ReportInfo(int type, String url_vid, String url_img, Long time, String location,
                      String describe) {
        this.type = type;
        this.url_vid = url_vid;
        this.url_img = url_img;
        this.time = time;
        this.location = location;
        this.describe = describe;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl_vid() {
        return url_vid;
    }

    public void setUrl_vid(String url_vid) {
        this.url_vid = url_vid;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public String getTime() {
        return refFormatNowDate();
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * Long转Date时间格式
     *
     * @return
     */
    public String refFormatNowDate() {
        if (time == null) {
            return null;
        }
        SimpleDateFormat sdFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String retStrFormatNowDate = sdFormatter.format(time);
        return retStrFormatNowDate;
    }
}
