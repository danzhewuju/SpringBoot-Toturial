package com.satan.utils;

public class UrlUtils {
  public static String urlConnector(String... urls) {
    StringBuilder sb = new StringBuilder();
    for (String url : urls) {
      sb.append(url).append("/");
    }
    return sb.toString();
  }

  public static String urlConnector(String ip, String port, String... urls) {
    StringBuilder sb = new StringBuilder();
    sb.append("http://").append(ip).append(":").append(port).append("/");
    for (String url : urls) {
      sb.append(url).append("/");
    }
    return sb.toString();
  }
}
