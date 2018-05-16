import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.JBColor;
import org.apache.commons.lang.StringUtils;

import java.awt.*;

public class TranslationAction extends AnAction {



    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
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
        String str = YoudaoUtil.getResult(selectTxt);

        //这里可以替换当前文本
//        WriteCommandAction.runWriteCommandAction(project, () ->
//                document.replaceString(start, end, buffer.toString())
//        );
        showPopupBalloon(str,editor);
    }
    private void showPopupBalloon(final String result,Editor editor) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                JBPopupFactory factory = JBPopupFactory.getInstance();
                factory.createHtmlTextBalloonBuilder(result,null, new JBColor(new Color(61, 62, 66), new Color(61, 62, 66)), null)
                        .setTitle("翻译结果")
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
            }
        });
    }


}
