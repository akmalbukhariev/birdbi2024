package com.birdbi.api;

import com.birdbi.api.constant.Constant;
import com.birdbi.api.constant.Result;

public class BaseController {

    protected VersionResponseResult setResult(Result result) {
        return setResult(result, null);

    }

    protected VersionResponseResult setResult(Result result, Object obj) {
        VersionResponseResult resResult = new VersionResponseResult();


        resResult.setResultCode(Integer.toString(result.getCode()));
        resResult.setResultMsg(result.getMessage());
        resResult.setApiVersion(Constant.api_version);
        resResult.setWebVersion(Constant.web_version);
        resResult.setResultData(obj);

        return resResult;
    }
}
