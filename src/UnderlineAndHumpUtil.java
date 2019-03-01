public class UnderlineAndHumpUtil {

    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */
    public static String UnderlineToHump(String para){
        StringBuilder result=new StringBuilder();
        String a[]=para.split("_");
        for(String s:a){
            if (!para.contains("_")) {
                result.append(s);
                continue;
            }
            if(result.length()==0){
                result.append(s.toLowerCase());
            }else{
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }



    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String HumpToUnderline(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        if (!para.contains("_")) {
            for(int i=0;i<para.length();i++){
                if(Character.isUpperCase(para.charAt(i))){
                    // 如果第一个字母大写，则前方不加下划线
                    if(i>0) {
                        sb.insert(i + temp, "_");
                        temp += 1;
                    }
                }
            }
        }
        return sb.toString().toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(UnderlineToHump("S_S_S_SSSSS_D_D_D_D"));
    }
}
