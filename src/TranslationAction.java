import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.JBColor;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URLDecoder;

public class TranslationAction extends AnAction {

    private static final CloseableHttpClient httpclient;
    public static final String CHARSET = "UTF-8";

    static{
        RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(3000).build();
        httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Application application = ApplicationManager.getApplication();

        Translation message = application.getComponent(Translation.class);
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();
        final int start = selectionModel.getSelectionStart();
        final int end = selectionModel.getSelectionEnd();
        // 当前选中的起止位置和文本
        TextRange range = new TextRange(start, end);
        String selectTxt = document.getText(range);
        if(StringUtils.isEmpty(selectTxt)){
            showPopupBalloon("请选择需要翻译的文本",editor);
            return;
        }
        String url = "http://xtk.azurewebsites.net/BingDictService.aspx?Word="+ URLDecoder.decode(selectTxt);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e1) {
            e1.printStackTrace();
            showPopupBalloon("翻译异常",editor);
            return;
        }
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode !=200){
            httpGet.abort();
            showPopupBalloon("HttpClient,error status code :" + statusCode,editor);
            return;
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
                showPopupBalloon("翻译异常",editor);
                return;
            }
        }
        if(StringUtils.isEmpty(result)){
            showPopupBalloon("无相关翻译结果",editor);
            return;
        }
        ContentVo object = new Gson().fromJson(result,ContentVo.class);
        StringBuffer buffer = new StringBuffer(200);
        String xing = "";
        String n=" \n ";
        buffer.append(selectTxt);
        buffer.append(" \n ").append(n);// /**
//        buffer.append(xing).append("word : "+object.getWord()).append(n);
        if(null == object || null == object.getDefs()){
            showPopupBalloon("无相关翻译结果！",editor);
            return;
        }
        object.getDefs().forEach(defsVo->{
            buffer.append(xing).append(" "+defsVo.getPos()).append(":").append(defsVo.getDef()).append(n);
        });
        buffer.append(xing+xing).append(n); // +"/"
        //这里可以替换当前文本
//        WriteCommandAction.runWriteCommandAction(project, () ->
//                document.replaceString(start, end, buffer.toString())
//        );
        showPopupBalloon(buffer.toString(),editor);
    }
    private void showPopupBalloon(final String result,Editor editor) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                JBPopupFactory factory = JBPopupFactory.getInstance();
                factory.createHtmlTextBalloonBuilder(result,null, new JBColor(new Color(255, 255, 255), new Color(255, 255, 255)), null)
                        .setTitle("翻译结果")
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
            }
        });
    }
}
