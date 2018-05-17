import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.util.TextRange;
import org.apache.commons.lang.StringUtils;

public class ToolJsonFormatAction extends AnAction {
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
        if (StringUtils.isEmpty(selectTxt)) {
            ShowUtils.showPopupBalloon("请选择展示的文本", editor, "错误");
            return;
        }
        String str = JsonShowUtil.formatJson(selectTxt);
        String htmlstr = "<html>\n <body>\n  " + str + " </body>\n  </html>";
        System.out.println(str);
        ShowUtils.showPopupBalloon(htmlstr, editor, "SON 格式化");
    }

}
