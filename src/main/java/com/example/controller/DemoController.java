package com.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Api(tags = "DEMO")
@RestController
public class DemoController {

    private String token;
    private String temp_code;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "corpid", value = "corpID", required = true),
            @ApiImplicitParam(name = "corpsecret", value = "corp密钥", required = true)
    })
    @ApiOperation(value = "getToken")
    @GetMapping("/ebus/wwlapi/cgi-bin/gettoken")
    public ResponseEntity<String> getToken(@RequestParam(value = "corpid")String corpid,
                                        @RequestParam(value = "corpsecret")String corpsecret){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<256;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        token = "{access_token:\"" + sb + "\"}";
        JSONObject json_token=JSONObject.fromObject(token);

        Map<String,String> params=new HashMap<>();

        params.put("status","0");
        params.put("message","corpid:" + corpid + ",corpsecret:" + corpsecret);


        JSONObject jsonObject=JSONObject.fromObject(params);
        jsonObject.put("data",json_token);
        String jsonStr=jsonObject.toString();
        System.out.println("success!" + corpid + corpsecret);
        return ResponseEntity.ok(jsonStr);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "appid", value = "appid", required = true),
            @ApiImplicitParam(name = "redirect_uri", value = "redirect_uri", required = true),
            @ApiImplicitParam(name = "response_type", value = "response_type", required = true),
            @ApiImplicitParam(name = "scope", value = "scope", required = true),
            @ApiImplicitParam(name = "agentid", value = "agentid", required = true),
            @ApiImplicitParam(name = "state", value = "state", required = true)
    })
    @ApiOperation(value = "getCode")
    @GetMapping("/connect/Oauth2/authorize")
    public ResponseEntity<String> getCode(@RequestParam(value = "appid")String appid,
                                          @RequestParam(value = "redirect_uri")String redirect_uri,
                                          @RequestParam(value = "response_type")String response_type,
                                          @RequestParam(value = "scope")String scope,
                                          @RequestParam(value = "agentid")String agentid,
                                          @RequestParam(value = "state")String state){

        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<512;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        temp_code = "{code:\"" + sb + "\"}";
        JSONObject json_code=JSONObject.fromObject(temp_code);


        Map<String,String> params=new HashMap<>();

        params.put("message","state:" + state);
        params.put("status", "0");

        JSONObject jsonObject=JSONObject.fromObject(params);
        jsonObject.put("data",json_code);

        String jsonStr=jsonObject.toString();
        System.out.println("success!getCode");
        return ResponseEntity.ok(jsonStr);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "code", required = true),
            @ApiImplicitParam(name = "access_token", value = "token", required = true)
    })
    @ApiOperation(value = "getuserinfo")
    @GetMapping("/ebus/wwlapi/cgi-bin/user/getuserinfo")
    public ResponseEntity<String> getUserId(@RequestParam(value = "code")String code,
                                           @RequestParam(value = "access_token")String access_token) {
        String user_id = "admin123";
        String user_info = "{user_id:\"" + user_id + "\"}";
        JSONObject json_user=JSONObject.fromObject(user_info);
        try{
            assert code.equals(temp_code);
            assert access_token.equals(token);
            Map<String,String> params=new HashMap<>();

            params.put("message","success");
            params.put("status", "0");

            JSONObject jsonObject=JSONObject.fromObject(params);
            jsonObject.put("data",json_user);

            String jsonStr=jsonObject.toString();
            System.out.println("success!getuserinfo");
            return ResponseEntity.ok(jsonStr);
        }catch (Exception e){
            return ResponseEntity.ok(e.toString());
        }
    }
}