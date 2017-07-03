package com.qunar.hotconfig.service.impl;

import com.google.common.collect.Maps;
import com.qunar.hotconfig.service.ValidateConfFileService;
import com.qunar.hotconfig.validator.Validator;
import com.qunar.hotconfig.validator.impl.FlightInfoValidator;
import com.qunar.hotconfig.validator.impl.FuelTaxValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by llnn.li on 2017/4/8.
 */
@Service
public class ValidateConfFileServiceImpl implements ValidateConfFileService {

    private static final Map<String, Validator> VALIDATE_MAP = Maps.newHashMap();

    static {
        VALIDATE_MAP.put("flight_info", new FlightInfoValidator());
        VALIDATE_MAP.put("fuel_tax", new FuelTaxValidator());
    }

    /**
     * 参数校验
     */
    @Override
    public void checkParam(String fileId, Map<String, Object> paramMap) {
        VALIDATE_MAP.get(fileId).validate(paramMap);
    }

}
