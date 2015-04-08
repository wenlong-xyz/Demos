/**
 * 
 */
package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import config.BaseResultVo;

public class BaseController
{

    SerializerFeature[] feature =
    { SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteMapNullValue };

    /**
     * @description: 构造成功返回结果
     * @author: Wind-spg
     * @param resultData
     *            : 需要返回的数据，可选
     * @return
     */
    protected String buildSuccessResultInfo(Object resultData)
    {
        BaseResultVo resultVo = new BaseResultVo();
        resultVo.setResultData(resultData);
        resultVo.setResultMessage("success");
        return JSON.toJSONString(resultVo, feature);
    }

    /**
     * @description: 构造失败返回结果
     * @author: Wind-spg
     * @param resultCode
     *            :任意非0数字，代表失败
     * @param failedMsg
     *            ：失败信息
     * @return
     */
    protected String buildFailedResultInfo(int resultCode, String failedMsg)
    {
        BaseResultVo resultVo = new BaseResultVo(resultCode, failedMsg);
        return JSON.toJSONString(resultVo, feature);
    }

}
