package com.luyc.esclient.util;


import java.util.ArrayList;
import java.util.List;

public class StrKit {

    /**
     * @author luyc
     * @Description 默认分隔字段
     * @Date 2023/2/16 9:35
     * @param null
     * @return
     **/
    public static final String split = ",";

    /**
     * @author luyc
     * @Description 分隔符-斜杠
     * @Date 2023/3/25 9:58
     * @param null
     * @return
     **/
    public static final String LASH_SPLIT = "/";


    /**
     * @author ylh
     * @Description 以加号分隔字段
     * @Date 2023/2/28 16:09
     * @param null
     * @return
     **/
    public static final String splitByPlusSign = "\\+";


    /**
     * 处理姓名等敏感字段
     *
     * @param name
     * @return
     */
    public static String changeName(String name) {
        if (notBlank(name)) {
            StringBuilder sb = new StringBuilder();
            sb.append(name.charAt(0));
            for (int i = 0; i < name.length() - 1; i++) {
                sb.append("x");
            }
            return sb.toString();
        } else {
            return name;
        }
    }


    /**
     * 首字母变小写
     */
    public static String firstCharToLowerCase(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            char[] arr = str.toCharArray();
            arr[0] += ('a' - 'A');
            return new String(arr);
        }
        return str;
    }

    /**
     * 首字母变大写
     */
    public static String firstCharToUpperCase(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            char[] arr = str.toCharArray();
            arr[0] -= ('a' - 'A');
            return new String(arr);
        }
        return str;
    }

    /**
     * 字符串为 null 或者为  "" 时返回 true
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim()) ? true : false;
    }

    /**
     * 字符串不为 null 而且不为  "" 时返回 true
     */
    public static boolean notBlank(String str) {
        return str == null || "".equals(str.trim()) ? false : true;
    }

    public static boolean notBlank(String... strings) {
        if (strings == null) {
            return false;
        }
        for (String str : strings) {
            if (str == null || "".equals(str.trim())) {
                return false;
            }
        }
        return true;
    }

    public static boolean notNull(Object... paras) {
        if (paras == null) {
            return false;
        }
        for (Object obj : paras) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据字符串List 拼接成字符串
     *
     * @param list
     * @param split
     * @return
     */
    public static String listJoinString(List<String> list, String split) {
        StringBuffer sb = new StringBuffer();
        int maxIndex = list.size() - 1;
        for (int i = 0; i <= maxIndex; i++) {
            sb.append(list.get(i));
            if (i != maxIndex) {
                sb.append(split);
            }
        }
        return sb.toString();
    }


    /**
     * @author luyc
     * @Description 将字符串转换为list
     * @Date 2022/12/14 14:43
     * @param strs
     * @param split
     * @return java.util.List<java.lang.String>
     **/
    public static List<String> strsToList(String strs,String split){
        String[] array = strs.split(split);
        List<String> list = new ArrayList<>(array.length);
        for(int i = 0;i<array.length;i++){
            list.add(array[i]);
        }
        return list;
    }

    /**
     * @return java.lang.String
     * @Author luyc
     * @Description 字符串拼接
     * @Date 12:14 2022/9/21
     * @Param [split, strs]
     **/
    public static String join(String split, String... strs) {
        StringBuffer sb = new StringBuffer();
        String[] array = strs;
        int maxIndex = array.length - 1;
        for (int i = 0; i <= maxIndex; i++) {
            sb.append(array[i]);
            if (i != maxIndex) {
                sb.append(split);
            }
        }
        return sb.toString();
    }




    /**
     * 返回由逗号分隔符分隔的数组
     * @param str
     * @return
     */
	/*public static List<Integer> listOfStrByComma(String str) throws Exception{
		return listOfStr(str, ",");
	}

	/**
	 * 返回由分隔符分隔的数组
	 * @param str
	 * @param defaultSplit
	 * @return
	 *//*
	public static List<Integer> listOfStr(String str, String defaultSplit) throws Exception{
		List<Integer> list = new ArrayList<>();
		if(StringUtils.isNullOrEmpty(str)){
			return null;
		}
		String[] strList = str.split(defaultSplit);
		for(String singleStr : strList){
			if(!StringUtils.isNullOrEmpty(singleStr)){
				list.add(Integer.valueOf(singleStr));
			}
		}
		return list;
	}*/

}
