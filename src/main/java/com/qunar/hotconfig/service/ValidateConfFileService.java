package com.qunar.hotconfig.service;

import java.util.Map;

/**
 * Created by llnn.li on 2017/4/7.
 */
public interface ValidateConfFileService {

    /** 参数校验 */
    void checkParam(String fileId, Map<String, Object> paramMap);

}
