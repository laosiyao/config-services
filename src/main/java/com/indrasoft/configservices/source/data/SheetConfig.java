package com.indrasoft.configservices.source.data;

import java.util.List;

/**
 * @Author laosiyao
 * @Date 2020/9/22 3:49 下午.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class SheetConfig {

    private String mvl;

    private String output;

    private List<String> profiles;

    public String getMvl() {
        return mvl;
    }

    public void setMvl(String mvl) {
        this.mvl = mvl;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
    }

    public SheetConfig() {
    }

    public SheetConfig(String mvl, String output, List<String> profiles) {
        this.mvl = mvl;
        this.output = output;
        this.profiles = profiles;
    }

    @Override
    public String toString() {
        return "FileSheetConfig{" +
                "mvl='" + mvl + '\'' +
                ", output='" + output + '\'' +
                ", profiles=" + profiles +
                '}';
    }

}
