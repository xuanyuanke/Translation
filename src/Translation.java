import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class Translation implements ApplicationComponent {

    public static String translationStr = "/**\n" +
            " * 翻译结果 {}\n" +
            " * \n" +
            " */ ";


    public void initComponent() {

        // TODO: insert component initialization logic here

    }



    public void disposeComponent() {

        // TODO: insert component disposal logic here

    }



    @NotNull
    public String getComponentName() {

        return "Translation";

    }



    public void message(String str) {

        // Show dialog with message

        Messages.showMessageDialog(
                str,

                "Sample",

                Messages.getInformationIcon()
        );

    }

}
