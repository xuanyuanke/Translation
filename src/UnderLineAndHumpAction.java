import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import org.apache.commons.lang.StringUtils;

public class UnderLineAndHumpAction extends AnAction {



    @Override
    public void actionPerformed(AnActionEvent e) {
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
            ShowUtils.showPopupBalloon("请选择文本",editor,"错误");
            return;
        }
        String str = "";
        if(selectTxt.indexOf("_") >0 ){
            str = UnderlineAndHumpUtil.UnderlineToHump(selectTxt);
        }else{
            str = UnderlineAndHumpUtil.HumpToUnderline(selectTxt);
        }
        final String strf = str;
        //替换当前文本
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(start, end, strf)
        );
    }
}
