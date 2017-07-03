package com.qunar.hotconfig.util.jsonUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by llnn.li on 2017/4/5.
 */
public class JsonModel implements Serializable {
    private boolean ret;
    private int errcode;
    private String errmsg;
    private int ver;
    private Data data;

    public static JsonModel errJsonModel(String errmsg) {
        JsonModel jsonModel = new JsonModel();
        jsonModel.ret = false;
        jsonModel.errcode = -1;
        jsonModel.errmsg = errmsg;
        jsonModel.ver = 1;
        jsonModel.data = null;
        return jsonModel;
    }

    public static JsonModel defaultJsonModel() {
        JsonModel jsonModel = new JsonModel();
        jsonModel.ret = true;
        jsonModel.errcode = 0;
        jsonModel.errmsg = "";
        jsonModel.ver = 1;
        jsonModel.data = null;
        return jsonModel;
    }

    public static JsonModel generateJsonModel(List list) {
        return generateJsonModel(list.size(),list);
    }

    public static JsonModel generateJsonModel(int totalCount, List list) {

        Data data = new Data();
        data.setTotalCount(totalCount);
        data.setList(list);
        JsonModel jsonModel = new JsonModel();
        jsonModel.ret = true;
        jsonModel.errcode = 0;
        jsonModel.errmsg = "";
        jsonModel.ver = 1;
        jsonModel.data = data;
        return jsonModel;
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int var) {
        this.ver = ver;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
