import com.google.gson.JsonArray;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class YoudaoUtil {

    private static String appkey="4ee8716a69175cce";
    private static String URL="http://openapi.youdao.com/api";
    private static String auth = "wpqP8bVRo2daY1ThY5o3DhGYZC2FfZwK";

    public static void main(String[] args) throws Exception {
        String query = "你好";
//        String salt = String.valueOf(System.currentTimeMillis());
//        String from = "zh-CHS";
//        String to = "EN";
//        String sign = md5(appkey + query + salt + auth);
//        Map params = new HashMap();
//        params.put("q", query);
//        params.put("from", from);
//        params.put("to", to);
//        params.put("sign", sign);
//        params.put("salt", salt);
//        params.put("appKey", appkey);
//        String result = requestForHttp(URL, params);
//
//        System.out.println(result);
//        String str =getstr(result);
//        System.out.println(str);
        System.out.print(getResult("厉害"));
    }
    public static String getResult(String query){
        try {
            String result = requestForHttp(URL, getParams(query));

            return getstr(result);
        }catch (Exception e){
            return null;
        }
    }

    private static Map getParams(String query){
        String salt = String.valueOf(System.currentTimeMillis());
        String from = "auto";
        String to = "auto";
        String sign = md5(appkey + query + salt + auth);
        Map params = new HashMap();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("sign", sign);
        params.put("salt", salt);
        params.put("appKey", appkey);
        return params;
    }

    public static String getstr(String str) throws Exception{
        JSONObject replyJson = new JSONObject(str);
        String errorCode = replyJson.getString("errorCode");
        if (errorCode.equals("0")) {
            String query = replyJson.getString("query");
            JSONArray translation
                    = replyJson.has("translation") ? replyJson.getJSONArray("translation") : null;
            JSONObject basic
                    = replyJson.has("basic") ? replyJson.getJSONObject("basic") : null;
            JSONArray web
                    = replyJson.has("web") ? replyJson.getJSONArray("web") : null;
            String phonetic=null;
            String uk_phonetic=null;
            String us_phonetic=null;
            JSONArray explains=null;
            if(basic!=null){
                phonetic=basic.has("phonetic")? basic.getString("phonetic"):null;
                uk_phonetic=basic.has("uk-phonetic")? basic.getString("uk-phonetic"):null;
                us_phonetic=basic.has("us-phonetic")? basic.getString("us-phonetic"):null;
                explains=basic.has("explains")? basic.getJSONArray("explains"):null;
            }
            String translationStr="";
            if(translation!=null){
                translationStr="\n翻译：\n";
                for(int i=0;i<translation.length();i++){
                    translationStr+="\t"+(i+1)+"、"+translation.getString(i)+"\n";
                }
            }
            String webstr = "";
            if(web!=null){
                webstr = " Web : \n" ;
                for(int i=0;i<web.length();i++){
                    JSONObject web_ = web.getJSONObject(i);
                    webstr+="\t"+(i+1)+"、"+web_.get("key")+" :  ";
                    JSONArray webs = web_.getJSONArray("value");
                    for(int j=0;j<webs.length();j++){
                        webstr+=webs.get(j).toString()+";  " ;
                    }
                    webstr +="\n";
                }

            }

            String phoneticStr=(phonetic!=null? "\n发音："+phonetic:"")
                    +(uk_phonetic!=null? "\n英式发音："+uk_phonetic:"")
                    +(us_phonetic!=null? "\n美式发音："+us_phonetic:"");
            String explainStr="";
            if(explains!=null){
                explainStr="\n\n释义：\n";
                for(int i=0;i<explains.length();i++){
                    explainStr+="\t"+(i+1)+"、"+explains.getString(i)+"\n";
                }
            }

            str="原文："+query+"\n"+translationStr+phoneticStr+explainStr+webstr;

        }
        return str;
    }

    public static String requestForHttp(String url, Map requestParams) throws Exception {
        String result = null;
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();//HttpClients.createDefault();
        /**HttpPost*/
        HttpPost httpPost = new HttpPost(url);
        System.out.println(new JSONObject(requestParams).toString());
        List params = new ArrayList();
        Iterator it = requestParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry en = (Map.Entry) it.next();
            String key = (String) en.getKey();
            String value = (String) en.getValue();
            if (value != null) {
                params.add(new BasicNameValuePair(key, value));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        /**HttpResponse*/
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "utf-8");
            EntityUtils.consume(httpEntity);
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 生成32位MD5摘要
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = string.getBytes("utf-8");
            /** 获得MD5摘要算法的 MessageDigest 对象 */
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            /** 使用指定的字节更新摘要 */
            mdInst.update(btInput);
            /** 获得密文 */
            byte[] md = mdInst.digest();
            /** 把密文转换成十六进制的字符串形式 */
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 根据api地址和参数生成请求URL
     *
     * @param url
     * @param params
     * @return
     */
    public static String getUrlWithQueryString(String url, Map<String ,String> params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key).toString();
            if (value == null) { // 过滤空的key
                continue;
            }

            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(encode(value));

            i++;
        }

        return builder.toString();
    }

    /**
     * 进行URL编码
     *
     * @param input
     * @return
     */
    public static String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }
}
