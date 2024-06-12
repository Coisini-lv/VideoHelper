/**
  * Copyright 2024 bejson.com 
  */
package com.lsh.videohelper.entity.dto;

/**
 * Auto-generated: 2024-06-12 7:18:54
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BilibiliVideoDataDto {

    private int code;
    private String message;
    private int ttl;
    private Data data;
    private String session;
    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setMessage(String message) {
         this.message = message;
     }
     public String getMessage() {
         return message;
     }

    public void setTtl(int ttl) {
         this.ttl = ttl;
     }
     public int getTtl() {
         return ttl;
     }

    public void setData(Data data) {
         this.data = data;
     }
     public Data getData() {
         return data;
     }

    public void setSession(String session) {
         this.session = session;
     }
     public String getSession() {
         return session;
     }

}