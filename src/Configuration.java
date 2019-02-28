
import com.intellij.ide.util.PropertiesComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.naming.ConfigurationException;
import javax.swing.*;

/**
 * Created by moxun on 2017/6/14.
 */
public class Configuration implements Configurable {
    private JTextField appId;
    private JTextField appKey;
    private JPanel root;
    private JTextField c1;
    private JTextField c2;
    private JTextField c3;

    private static final String KEY_APP_ID = "YOUDAO_APP_ID";
    private static final String KEY_APP_KEY = "YOUDAO_APP_KEY";
    private static final String KEY_COLOR_KEY_1 = "KEY_COLOR_KEY_1";
    private static final String KEY_COLOR_KEY_2 = "KEY_COLOR_KEY_2";
    private static final String KEY_COLOR_KEY_3 = "KEY_COLOR_KEY_3";

    @Nls
    public String getDisplayName() {
        return "Translation";
    }

    @Nullable
    public String getHelpTopic() {
        return "";
    }

    @Nullable
    public JComponent createComponent() {
        appId.setText(PropertiesComponent.getInstance().getValue(KEY_APP_ID));
        appKey.setText(PropertiesComponent.getInstance().getValue(KEY_APP_KEY));
        c1.setText(PropertiesComponent.getInstance().getValue(KEY_COLOR_KEY_1));
        c2.setText(PropertiesComponent.getInstance().getValue(KEY_COLOR_KEY_2));
        c3.setText(PropertiesComponent.getInstance().getValue(KEY_COLOR_KEY_3));
        return root;
    }

    public boolean isModified() {
        return true;
    }

    public void apply() throws ConfigurationException {
        PropertiesComponent.getInstance().setValue(KEY_APP_ID, appId.getText());
        PropertiesComponent.getInstance().setValue(KEY_APP_KEY, appKey.getText());
        PropertiesComponent.getInstance().setValue(KEY_COLOR_KEY_1, c1.getText());
        PropertiesComponent.getInstance().setValue(KEY_COLOR_KEY_2, c2.getText());
        PropertiesComponent.getInstance().setValue(KEY_COLOR_KEY_3, c3.getText());
    }

    public void reset() {

    }

    public void disposeUIResources() {

    }

    public static String getAppId() {
        return PropertiesComponent.getInstance().getValue(KEY_APP_ID, "");
    }

    public static String getAppKey() {
        return PropertiesComponent.getInstance().getValue(KEY_APP_KEY, "");
    }

    public static int getC1() {
        String c = PropertiesComponent.getInstance().getValue(KEY_COLOR_KEY_1, "61");
        if(StringUtils.isBlank(c)){
            c="61";
        }
        return Integer.parseInt(c);
    }

    public static int getC2() {
        String c = PropertiesComponent.getInstance().getValue(KEY_COLOR_KEY_2, "62");
        if(StringUtils.isBlank(c)){
            c="62";
        }
        return Integer.parseInt(c);
    }

    public static int getC3() {
        String c = PropertiesComponent.getInstance().getValue(KEY_COLOR_KEY_3, "66");
        if(StringUtils.isBlank(c)){
            c="66";
        }
        return Integer.parseInt(c);
    }

    @Override
    public RequestConfig getConfig() {
        return null;
    }
}