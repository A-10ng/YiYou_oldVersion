package com.example.yiyou.db;

import org.litepal.crud.LitePalSupport;

/**
 * Created by 龙世治 on 2019/3/24.
 */

public class Route extends LitePalSupport {
    private String guidePhoneNum;
    private String teamName;
    private String route;

    public String getGuidePhoneNum() {
        return guidePhoneNum;
    }

    public void setGuidePhoneNum(String guidePhoneNum) {
        this.guidePhoneNum = guidePhoneNum;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
