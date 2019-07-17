package com.example.yiyou.data;

/**
 * Created by 龙世治 on 2019/3/16.
 */

public class Data {
    private static String UserPhoneNum;
    private static String TeamName = "none";
    private static String route = "none";

    public static String getRoute() {
        return route;
    }

    public static void setRoute(String route) {
        Data.route = route;
    }

    public static String getTeamName() {
        return TeamName;
    }

    public static void setTeamName(String teamName) {
        TeamName = teamName;
    }

    public static String getUserPhoneNum() {
        return UserPhoneNum;
    }

    public static void setUserPhoneNum(String userPhoneNum) {
        UserPhoneNum = userPhoneNum;
    }
}
