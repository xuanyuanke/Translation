import java.util.List;

public class ContentVo {
    private String word;
    private List<DefsVo> defs;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<DefsVo> getDefs() {
        return defs;
    }

    public void setDefs(List<DefsVo> defs) {
        this.defs = defs;
    }
}
