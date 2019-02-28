import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class YoudaoOpenApi {
    public static String getResult(String selectTxt){
        JsonObject object = null;
        try {
            String result = getContent(selectTxt);
            if(StringUtils.isEmpty(result)){
                return null;
            }
            object = new Gson().fromJson(result,JsonObject.class);
        }catch (Exception el){
            el.printStackTrace();
            return null ;
        }
        StringBuffer buffer = new StringBuffer(200);
        String xing = "";
        String n=" \n ";
        buffer.append(selectTxt);
        buffer.append(" \n ").append(n);// /**
        if(null == object || null == object.get("translateResult")){
            return null ;
        }
        JsonArray array = new Gson().fromJson(object.get("translateResult"),JsonArray.class);
        array.forEach(defsVo -> {
            defsVo.getAsJsonArray().forEach(vo -> {
                buffer.append(xing).append(" " + vo.getAsJsonObject().get("src")).append(" ").append(vo.getAsJsonObject().get("tgt")).append(n);
            });
        });
        buffer.append(xing+xing).append(n);
        return buffer.toString();
    }

    private static String getContent(String text){
        String url = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i="+ URLEncoder.encode(text);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = HttpClientUtil.getHttpClient().execute(httpGet);
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode !=200){
            httpGet.abort();
            return null;
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            try {
                result = EntityUtils.toString(entity, "utf-8");
                EntityUtils.consume(entity);
                response.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        if(StringUtils.isEmpty(result)){
            return null;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.print(getResult("You are all well"));
    }
}
