package com.qunar.hotconfig.validator;

import java.util.Map;

/**
 * Created by llnn.li on 2017/4/8.
 */
public interface Validator {

    void validate(Map<String, Object> paramMap);

}
