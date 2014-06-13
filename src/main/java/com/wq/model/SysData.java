package com.wq.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 13-5-17
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public class SysData implements Serializable {

    private Set<String> fileList;//映射文件列表
    private int speed;//默认鼠标滚动速度  默认35
    private String savePath;//默认文件保存路径
    private Set<String> typeList;//支持文件类型
    private boolean isExpland;//是否打开目录   默认false
    private String password;//密码
    private boolean isFirstLogin;//是否首次登陆
    private String lastOpenPath;//上次打开的映射路径
    private boolean ignoreEmptyFolder;//忽略空白文件夹
    private boolean autoFit;//图片自适应模式
    private boolean isHMode;//水平浏览模式
    //抓取
    private int fetchMinWidth;
    private int fetchMinHeight;
    private int fetchDeep;
    private boolean fetchToMana;
    //代理设置
    private boolean isUseProxy;
    private String proxyHost;
    private int proxyPort;
    private String proxyUserName;
    private String proxyUserPassword;

    public boolean isHMode() {
        return isHMode;
    }

    public void setHMode(boolean HMode) {
        isHMode = HMode;
    }

    public boolean isUseProxy() {
        return isUseProxy;
    }

    public void setUseProxy(boolean useProxy) {
        isUseProxy = useProxy;
    }

    public int getFetchMinWidth() {
        return fetchMinWidth;
    }

    public void setFetchMinWidth(int fetchMinWidth) {
        this.fetchMinWidth = fetchMinWidth;
    }

    public int getFetchMinHeight() {
        return fetchMinHeight;
    }

    public void setFetchMinHeight(int fetchMinHeight) {
        this.fetchMinHeight = fetchMinHeight;
    }

    public int getFetchDeep() {
        return fetchDeep;
    }

    public void setFetchDeep(int fetchDeep) {
        this.fetchDeep = fetchDeep;
    }

    public boolean isFetchToMana() {
        return fetchToMana;
    }

    public void setFetchToMana(boolean fetchToMana) {
        this.fetchToMana = fetchToMana;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUserName() {
        return proxyUserName;
    }

    public void setProxyUserName(String proxyUserName) {
        this.proxyUserName = proxyUserName;
    }

    public String getProxyUserPassword() {
        return proxyUserPassword;
    }

    public void setProxyUserPassword(String proxyUserPassword) {
        this.proxyUserPassword = proxyUserPassword;
    }

    public boolean isAutoFit() {
        return autoFit;
    }

    public void setAutoFit(boolean autoFit) {
        this.autoFit = autoFit;
    }

    public boolean isIgnoreEmptyFolder() {
        return ignoreEmptyFolder;
    }

    public void setIgnoreEmptyFolder(boolean ignoreEmptyFolder) {
        this.ignoreEmptyFolder = ignoreEmptyFolder;
    }


    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public Set<String> getFileList() {
        return fileList;
    }

    public void setFileList(Set<String> fileList) {
        this.fileList = fileList;
    }

    public Set<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(Set<String> typeList) {
        this.typeList = typeList;
    }

    public boolean isExpland() {
        return isExpland;
    }

    public void setExpland(boolean expland) {
        isExpland = expland;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public String getLastOpenPath() {
        return lastOpenPath;
    }

    public void setLastOpenPath(String lastOpenPath) {
        this.lastOpenPath = lastOpenPath;
    }
}
