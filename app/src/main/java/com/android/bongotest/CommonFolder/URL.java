package com.android.bongotest.CommonFolder;

public class URL {
    public static final String HTTP = "https://";
    public static final String DOMAIN = "api.themoviedb.org/3/movie/";
    public static final String MAIN_URL = HTTP  + DOMAIN;
    public static final String Last_Part = "?api_key=c37d3b40004717511adb2c1fbb15eda4&language=en-US&page=";
    public static final String Last_Part2 = "?api_key=c37d3b40004717511adb2c1fbb15eda4&language=en-US";

    public static final String Top_rated_movie_api =MAIN_URL+Constant.top_rated+Last_Part;
//    public static final String Movie_details_api = MAIN_URL+Constant.id+"?api_key=c37d3b40004717511adb2c1fbb15eda4&language=en-US";

    public static final String image = HTTP+"image.tmdb.org/t/p/original/";
}
