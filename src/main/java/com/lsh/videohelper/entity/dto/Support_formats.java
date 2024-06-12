/**
  * Copyright 2024 bejson.com 
  */
package com.lsh.videohelper.entity.dto;
import java.util.List;

/**
 * Auto-generated: 2024-06-12 7:18:54
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Support_formats {

    private int quality;
    private String format;
    private String new_description;
    private String display_desc;
    private String superscript;
    private List<String> codecs;
    public void setQuality(int quality) {
         this.quality = quality;
     }
     public int getQuality() {
         return quality;
     }

    public void setFormat(String format) {
         this.format = format;
     }
     public String getFormat() {
         return format;
     }

    public void setNew_description(String new_description) {
         this.new_description = new_description;
     }
     public String getNew_description() {
         return new_description;
     }

    public void setDisplay_desc(String display_desc) {
         this.display_desc = display_desc;
     }
     public String getDisplay_desc() {
         return display_desc;
     }

    public void setSuperscript(String superscript) {
         this.superscript = superscript;
     }
     public String getSuperscript() {
         return superscript;
     }

    public void setCodecs(List<String> codecs) {
         this.codecs = codecs;
     }
     public List<String> getCodecs() {
         return codecs;
     }

}