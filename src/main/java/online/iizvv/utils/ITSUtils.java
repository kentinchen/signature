package online.iizvv.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import online.iizvv.core.config.Config;
import online.iizvv.pojo.Apple;
import online.iizvv.pojo.Authorize;
import online.iizvv.service.DeviceServiceImpl;
import sun.security.ec.ECPrivateKeyImpl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ITSUtils {

    /**
     * create by: iizvv
     * description: 获取帐号信息
     * create time: 2019-06-29 15:13
     *

     * @return number：可用数量， udids已有设备, cerId证书id
     */
    public static Map getNumberOfAvailableDevices(Authorize authorize) throws InvalidKeyException {
        Map header = getToken(authorize.getP8(), authorize.getIss(), authorize.getKid());
        Map res = new HashMap();
        String url = "https://api.appstoreconnect.apple.com/v1/devices";
        Map body = new HashMap();
        body.put("limit", 200);
        String result = HttpRequest.get(url).
                addHeaders(header).form(body).
                execute().body();
        System.out.println(result);
        Map map = JSON.parseObject(result, Map.class);
        JSONArray errors = (JSONArray)map.get("errors");
        if (errors!=null) {
            res.put("msg", "与苹果建立连接失败, 帐号无法使用或p8文件不正确：errors: " + result);
        }else {
            JSONArray data = (JSONArray)map.get("data");
            List devices = new LinkedList();
            for (Object datum : data) {
                Map device = new HashMap();
                Map m = (Map) datum;
                String id = (String)m.get("id");
                Map attributes = (Map)m.get("attributes");
                String udid = (String)attributes.get("udid");
                device.put("deviceId", id);
                device.put("udid", udid);
                devices.add(device);
            }
            removeCertificates(header);
            removeBundleIds(header);
            Map <String, String>cerMap = insertCertificates(header, authorize.getCsr());
            if (cerMap.get("msg") != null) {
                res.put("msg", "创建cer文件失败: errors:" + cerMap.get("msg"));
            }else {
                String bundleIds = insertBundleIds(header);
                Map meta = (Map) map.get("meta");
                Map paging = (Map)meta.get("paging");
                int total = (int) paging.get("total");
                res.put("number", Config.total-total);
                res.put("devices", devices);
                res.put("cerId", cerMap.get("id"));
                res.put("bundleIds", bundleIds);
                System.out.println(res);
            }
        }
        return res;
    }

    /**
     * create by: iizvv
     * description: 添加设备
     * create time: 2019-06-29 15:14
     *

     * @return String
     */
    public static String insertDevice(String udid, Authorize authorize) throws InvalidKeyException {
        Map body = new HashMap();
        body.put("type", "devices");
        Map attributes = new HashMap();
        attributes.put("name", udid);
        attributes.put("udid", udid);
        attributes.put("platform", "IOS");
        body.put("attributes", attributes);
        Map data = new HashMap();
        data.put("data", body);
        String url = "https://api.appstoreconnect.apple.com/v1/devices";
        String result = HttpRequest.post(url).
                addHeaders(getToken(authorize.getP8(), authorize.getIss(), authorize.getKid())).
                body(JSON.toJSONString(data)).execute().body();
        System.out.println("添加账号执行完成: " + result);
        Map map = JSON.parseObject(result, Map.class);
        Map data1 = (Map) map.get("data");
        String id = (String)data1.get("id");
        return id;
    }

    /**
     * create by: iizvv
     * description: 创建个人资料
     * create time: 2019-06-29 15:33
     *

     * @return File
     */
    public static String insertProfile(Apple apple, String devId) throws InvalidKeyException {
        Map body = new HashMap();
        body.put("type", "profiles");
        String name = IdUtil.simpleUUID();
        Map attributes = new HashMap();
        attributes.put("name", name);
        attributes.put("profileType", "IOS_APP_ADHOC");
        body.put("attributes", attributes);
        Map relationships = new HashMap();
        Map bundleId = new HashMap();
        Map data2 = new HashMap();
        data2.put("id", apple.getBundleIds());
        data2.put("type", "bundleIds");
        bundleId.put("data", data2);
        relationships.put("bundleId", bundleId);
        Map certificates = new HashMap();
        Map data3 = new HashMap();
        data3.put("id", apple.getCerId());
        data3.put("type", "certificates");
        List list = new LinkedList();
        list.add(data3);
        certificates.put("data", list);
        relationships.put("certificates", certificates);
        Map devices = new HashMap();
        Map data4 = new HashMap();
        data4.put("id", devId);
        data4.put("type", "devices");
        List list2 = new LinkedList();
        list2.add(data4);
        devices.put("data", list2);
        relationships.put("devices", devices);
        body.put("relationships", relationships);
        Map data = new HashMap();
        data.put("data", body);
        String url = "https://api.appstoreconnect.apple.com/v1/profiles";
        String result = HttpRequest.post(url).
                addHeaders(getToken(apple.getP8(), apple.getIss(), apple.getKid())).
                body(JSON.toJSONString(data)).execute().body();
        System.out.println("证书创建执行完成: " + result);
        Map map = JSON.parseObject(result, Map.class);
        JSONArray errors = (JSONArray)map.get("errors");
        if (errors != null) {
            return "errors";
        }
        Map o = (Map) map.get("data");
        Map o2 = (Map) o.get("attributes");
        String profileContent = (String) o2.get("profileContent");
        return profileContent;
    }

    /**
     * create by: iizvv
     * description: 删除当前帐号下的所有证书
     * create time: 2019-06-29 16:00
     *

     * @return void
     */
    static void removeCertificates(Map header) {
        System.out.println("开始删除所有已存在证书");
        String url = "https://api.appstoreconnect.apple.com/v1/certificates";
        String result = HttpRequest.
                get(url).
                addHeaders(header).
                execute().
                body();
        System.out.println("获取完成：" + result);
        Map map = JSON.parseObject(result, Map.class);
        JSONArray data = (JSONArray)map.get("data");
        for (Object datum : data) {
            Map m = (Map) datum;
            String id = (String) m.get("id");
            String result2 = HttpRequest.delete(url+"/"+id).
                    addHeaders(header).
                    execute().
                    body();
            System.out.println("删除完成：" + result2);
        }
    }

    /**
     * create by: iizvv
     * description: 删除所有绑定的ids
     * create time: 2019-07-01 14:41
     *
     * @return void
     */
    static void removeBundleIds(Map header) {
        System.out.println("开始删除绑定的ids");
        String url = "https://api.appstoreconnect.apple.com/v1/bundleIds";
        String result = HttpRequest.
                get(url).
                addHeaders(header).
                execute().
                body();
        System.out.println("获取完成: " + result);
        Map map = JSON.parseObject(result, Map.class);
        JSONArray data = (JSONArray) map.get("data");
        for (Object datum : data) {
            Map m = (Map) datum;
            String id = (String) m.get("id");
            String result2 = HttpRequest.delete(url + "/" + id).
                    addHeaders(header).
                    execute().
                    body();
            System.out.println("删除完成：" + result2);
        }
    }



    /**
     * create by: iizvv
     * description: 创建证书
     * create time: 2019-06-29 16:34
     *

     * @return 创建的证书id
     */
    static Map <String, String>insertCertificates(Map header, String csr) {
        System.out.println("开始创建cer证书");
        HashMap hashMap = new HashMap();
        String url = "https://api.appstoreconnect.apple.com/v1/certificates";
        Map body = new HashMap();
        body.put("type", "certificates");
        Map attributes = new HashMap();
        attributes.put("csrContent", csr);
        attributes.put("certificateType", "IOS_DISTRIBUTION");
        body.put("attributes", attributes);
        Map data = new HashMap();
        data.put("data", body);
        String result = HttpRequest.post(url).
                addHeaders(header).
                body(JSON.toJSONString(data)).
                execute().body();
        System.out.println("创建完成: " + result);
        Map map = JSON.parseObject(result, Map.class);
        JSONArray errors = (JSONArray)map.get("errors");
        if (errors != null) {
            System.out.println("创建失败: " + result);
            hashMap.put("msg", result);
            return hashMap;
        }
        Map data1 = (Map) map.get("data");
        String id = (String) data1.get("id");
        hashMap.put("id", id);
        return hashMap;
    }

    /**
     * create by: iizvv
     * description: 创建BundleIds
     * create time: 2019-07-01 15:00
     *
     * @return id
     */
    static String insertBundleIds(Map header) {
        System.out.println("开始创建BundleIds");
        String url = "https://api.appstoreconnect.apple.com/v1/bundleIds";
        Map body = new HashMap();
        body.put("type", "bundleIds");
        Map attributes = new HashMap();
        attributes.put("identifier", "com.app.*");
        attributes.put("name", "AppBundleId");
        attributes.put("platform", "IOS");
        body.put("attributes", attributes);
        Map data = new HashMap();
        data.put("data", body);
        String result = HttpRequest.post(url).
                addHeaders(header).
                body(JSON.toJSONString(data)).
                execute().body();
        System.out.println("创建完：" + result);
        Map map = JSON.parseObject(result, Map.class);
        Map data1 = (Map) map.get("data");
        String id = (String)data1.get("id");
        return id;
    }

    /**
     * create by: iizvv
     * description: 获取Token
     * create time: 2019-06-29 15:14
     *

     * @return 请求头
     */
    static Map getToken(String p8, String iss, String kid) throws InvalidKeyException {
        String s = p8.
                replace("-----BEGIN PRIVATE KEY-----", "").
                replace("-----END PRIVATE KEY-----", "");
        byte[] encodeKey = Base64.decode(s);
        String token = token = Jwts.builder().
                setHeaderParam(JwsHeader.ALGORITHM, "ES256").
                setHeaderParam(JwsHeader.KEY_ID,kid).
                setHeaderParam(JwsHeader.TYPE, "JWT").
                setIssuer(iss).
                claim("exp", System.currentTimeMillis()/1000 +  60 * 10).
                setAudience("appstoreconnect-v1").
                signWith(SignatureAlgorithm.ES256, new ECPrivateKeyImpl(encodeKey)).
                compact();
        Map map = new HashMap();
        map.put("Content-Type", "application/json");
        map.put("Authorization", "Bearer " + token);
        System.out.println("jwt=" + map);
        return map;
    }

}
