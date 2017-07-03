package com.qunar.hotconfig.validator.impl;

import com.google.common.base.Strings;
import com.qunar.hotconfig.util.validateUtil.ParamException;
import com.qunar.hotconfig.validator.Validator;
import java.util.Map;

/**
 * Created by llnn.li on 2017/4/8.
 */
public class FuelTaxValidator implements Validator {

    /** 分隔符，用来分隔提示信息：如“CODE不能为空，DEPARTURE＿CITY字段不存在” */
    private static final String SEPARATOR_SYMBOL = ",";

    private static final String DEP＿CODE = "dep_code";
    private static final String ARR＿CODE = "arr_code";
    private static final String FUEL＿TAX = "tax";

    private static final String CODE_REGEX = "^[a-zA-Z]{3}$";
    private static final String CODE_ERROR_MESS = "必须为3位字母";
    private static final String TAX_REGEX = "^[0-9]{1,4}$";
    private static final String TAX_ERROR_MESS = "必须为0-9999的整数";

    @Override
    public void validate(Map<String, Object> paramMap) {
        StringBuilder builder = new StringBuilder();
        builder.append(checkParam(paramMap, DEP＿CODE, CODE_REGEX, CODE_ERROR_MESS));
        builder.append(checkParam(paramMap, ARR＿CODE, CODE_REGEX, CODE_ERROR_MESS));
        builder.append(checkParam(paramMap, FUEL＿TAX, TAX_REGEX, TAX_ERROR_MESS));

        if (!builder.toString().isEmpty()) {
            throw new ParamException(builder.toString());
        }
    }

    private static String checkParam(Map<String, Object> paramMap, String param, String regex, String errorMess) {
        StringBuilder ret = new StringBuilder();
        if (paramMap.containsKey(param)) {
            String checkWaitParam = paramMap.get(param).toString().trim();
            if (!Strings.isNullOrEmpty(checkWaitParam)) {
                if (checkWaitParam.replaceAll(regex, "").length() != 0) {
                    ret.append(param).append(errorMess).append(SEPARATOR_SYMBOL);
                }
            } else {
                ret.append(param).append("不能为空").append(SEPARATOR_SYMBOL);
            }
        } else {
            ret.append(param).append("字段不存在").append(SEPARATOR_SYMBOL);
        }
        return ret.toString();
    }
}
