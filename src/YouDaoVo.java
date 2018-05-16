import java.util.List;
import java.util.Map;

public class YouDaoVo {
    private List<Map<String,List<String>>> web;
    private String query;
    private List<String> translation;
    private Map<String,List<String>> basic;


    public List<Map<String, List<String>>> getWeb() {
        return web;
    }

    public void setWeb(List<Map<String, List<String>>> web) {
        this.web = web;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public Map<String, List<String>> getBasic() {
        return basic;
    }

    public void setBasic(Map<String, List<String>> basic) {
        this.basic = basic;
    }
}
