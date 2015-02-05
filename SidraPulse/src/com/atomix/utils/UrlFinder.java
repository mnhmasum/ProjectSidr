package com.atomix.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlFinder {
	public int getNoOfUrl(String text) {
		
		return 0;
	}
	
	public static Matcher getMatcher(String msg) {
		String urlPattern = "((https?|ftp|gopher|telnet|file):(|(//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(msg);
		return m;
	}
	
	public static boolean isVideo(String checkUrl) {
		if (checkUrl.contains("youtube.com/watch?v") || checkUrl.contains("vimeo.com/")) {
			return true;
		}
		return false;
	}
	
	public static String getVideoProvider(String checkUrl) {
		
	    if (checkUrl.contains("youtube.com/watch?v")) {
			return "http://www.youtube.com/oembed?url="+checkUrl+"&format=json";
		
		} else if (checkUrl.contains("vimeo.com/")) {
			return "https://vimeo.com/api/oembed.json?url=" + checkUrl;
		}
		
		return "";
	}
}
