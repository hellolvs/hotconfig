package com.qunar.hotconfig.service;

import com.qunar.hotconfig.util.jsonUtil.DiffResult;

import java.util.List;

/**
 * Created by shadandan on 17/4/5.
 */
public interface DiffService {

    List<DiffResult> configDiff(String fileId);

}
