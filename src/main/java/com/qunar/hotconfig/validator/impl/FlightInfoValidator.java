package com.qunar.hotconfig.validator.impl;

import com.google.common.base.Strings;
import com.qunar.hotconfig.util.validateUtil.ParamException;
import com.qunar.hotconfig.validator.Validator;
import java.util.Map;

/**
 * Created by llnn.li on 2017/4/8.
 */
public class FlightInfoValidator implements Validator {

    /** 分隔符，用来分隔提示信息：如“CODE不能为空，DEPARTURE＿CITY字段不存在” */
    private static final String SEPARATOR_SYMBOL = ",";

    private static final String CODE = "code";
    private static final String DEPARTURE＿CITY = "departure_city";
    private static final String ARRIVAL＿CITY = "arrival_city";
    private static final String SUM＿LATE＿TIME = "sum_latetime";
    private static final String SUM＿TIMES = "sum_times";
    private static final String SUM＿ON＿TIME = "sum_ontime";
    private static final String RATE＿ON＿TIME = "rate_ontime";
    private static final String AVG＿LATE＿TIME = "avg_latetime";
    private static final String NAME = "name";
    private static final String WRAPPER_ID = "wrapper_id";
    private static final String WORD_OF_MOUTH = "word_of_mouth";
    private static final String TICKET_NUM = "ticket_num";

    private static final String CODE_REGEX = "^[a-zA-Z0-9]{6}$";
    private static final String CODE_ERROR_MESS = "必须是长度为6位包含字母或数字的字符";
    private static final String SUM＿LATE＿TIME_REGEX = "^[0-9]{3}$";
    private static final String SUM＿LATE＿TIME_ERROR_MESS = "必须为3位数字";
    private static final String SUM＿TIMES_REGEX = "^[0-9]{4}$";
    private static final String SUM＿TIMES_ERROR_MESS = "必须为4位数字";
    private static final String COMMON_REGEX = "^[0-9]{2}$";
    private static final String COMMON_ERROR_MESS = "必须为2位数字";
    private static final String NAME_REGEX = "^[\u4e00-\u9fa5a-zA-Z0-9]{0,20}$";
    private static final String NAME_ERROR_MESS = "必须为字母、数字或汉字的组合,且长度不能超过20位";
    private static final String WRAPPER_ID_REGEX = "^[a-z]{7}[0-9]{4}$";
    private static final String WRAPPER_ID_ERROR_MESS = "必须前7位为小写字母，后4位为数字";
    private static final String RATE＿ON＿TIME_REGEX = "^0[.][0-9]{1,2}$";
    private static final String RATE＿ON＿TIME_ERROR_MESS = "必须为大于0小于1的小数，小数位数不能超过2位";
    private static final String WORD_COMMON_REGEX = "^[0-9]{1,2}$";
    private static final String WORD_COMMON_ERROR_MESS = "必须为1-2位数字";

    @Override
    public void validate(Map<String, Object> paramMap) {
        StringBuilder builder = new StringBuilder();

        builder.append(checkParam(paramMap, CODE, CODE_REGEX, CODE_ERROR_MESS));
        builder.append(checkParam(paramMap, DEPARTURE＿CITY, NAME_REGEX, NAME_ERROR_MESS));
        builder.append(checkParam(paramMap, ARRIVAL＿CITY, NAME_REGEX, NAME_ERROR_MESS));
        builder.append(checkParam(paramMap, SUM＿LATE＿TIME, SUM＿LATE＿TIME_REGEX, SUM＿LATE＿TIME_ERROR_MESS));
        builder.append(checkParam(paramMap, SUM＿TIMES, SUM＿TIMES_REGEX, SUM＿TIMES_ERROR_MESS));
        builder.append(checkParam(paramMap, SUM＿ON＿TIME, COMMON_REGEX, COMMON_ERROR_MESS));
        builder.append(checkParam(paramMap, RATE＿ON＿TIME, RATE＿ON＿TIME_REGEX, RATE＿ON＿TIME_ERROR_MESS));
        builder.append(checkParam(paramMap, AVG＿LATE＿TIME, COMMON_REGEX, COMMON_ERROR_MESS));
        builder.append(checkParam(paramMap, NAME, NAME_REGEX, NAME_ERROR_MESS));
        builder.append(checkParam(paramMap, WRAPPER_ID, WRAPPER_ID_REGEX, WRAPPER_ID_ERROR_MESS));
        builder.append(checkParam(paramMap, WORD_OF_MOUTH, WORD_COMMON_REGEX, WORD_COMMON_ERROR_MESS));
        builder.append(checkParam(paramMap, TICKET_NUM, WORD_COMMON_REGEX, WORD_COMMON_ERROR_MESS));

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