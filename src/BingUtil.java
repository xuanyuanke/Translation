import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLDecoder;

public class BingUtil {


    private static String result(String selectTxt){
        ContentVo object = null;
        try {
            String result = getContent(selectTxt);
            if(StringUtils.isEmpty(result)){
                return null;
            }
            object = new Gson().fromJson(result,ContentVo.class);
        }catch (Exception el){
            el.printStackTrace();
            return null ;
        }
        StringBuffer buffer = new StringBuffer(200);
        String xing = "";
        String n=" \n ";
        buffer.append(selectTxt);
        buffer.append(" \n ").append(n);// /**
//        buffer.append(xing).append("word : "+object.getWord()).append(n);
        if(null == object || null == object.getDefs()){
            return null ;
        }
        object.getDefs().forEach(defsVo->{
            buffer.append(xing).append(" "+defsVo.getPos()).append(" ").append(defsVo.getDef()).append(n);
        });
        buffer.append(xing+xing).append(n); // +"/"
        return buffer.toString();
    }

    private static String getContent(String text){
        String url = "http://xtk.azurewebsites.net/BingDictService.aspx?Word="+ URLDecoder.decode(text);
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
        System.out.print(result("你好"));
    }
}
