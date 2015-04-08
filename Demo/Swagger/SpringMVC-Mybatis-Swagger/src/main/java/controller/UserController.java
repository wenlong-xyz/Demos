package controller;

import javax.servlet.http.HttpServletRequest;

import model.UserInfo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import config.BaseResultVo;

@Controller
public class UserController extends BaseController {

	@ResponseBody
	@RequestMapping(value = "addUser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "添加用户", httpMethod = "POST", response = BaseResultVo.class, notes = "add user")
	public String addUser(
			@ApiParam(required = true, name = "postData", value = "用户信息json数据") @RequestParam(value = "postData") String postData,
			HttpServletRequest request) {
		if (null == postData || postData.isEmpty()) {
			return super.buildFailedResultInfo(-1, "post data is empty!");
		}

		UserInfo user = JSON.parseObject(postData, UserInfo.class);
		System.out.println(user);
		return buildSuccessResultInfo(111);
	}

}
