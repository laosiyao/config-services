package com.indrasoft.configservices.util;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.text.*;
import java.util.*;

public class Util {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static HashMap toJson(String jsonStr) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, HashMap.class);
        } catch (JsonProcessingException e) {
            return (HashMap)Collections.emptyMap();
        }
    }

    public static boolean isJson(String str) {
        try {
            if (OBJECT_MAPPER.readTree(str) instanceof ObjectNode) {
                return true;
            }
        } catch (JsonProcessingException e) {
            return false;
        }
        return false;
    }

    public static boolean isArray(String str) {
        try {
            if (OBJECT_MAPPER.readTree(str) instanceof ArrayNode) {
                return true;
            }
        } catch (JsonProcessingException e) {
            return false;
        }
        return false;
    }

    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public static String getStackTrace(StackTraceElement[] trace) {
        StringBuilder sb = new StringBuilder();
        sb.append("    \r\n");
        // print the stack trace, ommitting the zeroth element, which is
        // always this method.
        for (int ctr = 0; ctr < trace.length; ctr++) {
            sb.append("        >");
            sb.append(formatStackTraceElement(trace[ctr])).append("\r\n");
        }
        return sb.toString();
    }

    private static String formatStackTraceElement(StackTraceElement ste) {
        return compressClassName(ste.getClassName())
                + "."
                + ste.getMethodName()
                + (ste.isNativeMethod() ? "(Native Method)" : (ste.getFileName() != null && ste.getLineNumber() >= 0 ? "("
                + ste.getFileName() + ":" + ste.getLineNumber() + ")" : (ste.getFileName() != null ? "("
                + ste.getFileName() + ")" : "(Unknown Source)")));
    }

    private static String compressClassName(String name) {
        // Note that this must end in . in order to be renamed correctly.
        String prefix = "com.sun.corba.se.";
        if (name.startsWith(prefix)) {
            return "(ORB)." + name.substring(prefix.length());
        } else {
            return name;
        }
    }

    // 判断编码格式方法
    @SuppressWarnings("resource")
    public static String getFilecharset(File sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset; // 文件编码为 ANSI
            } else if (first3Bytes[0] == (byte)0xFF && first3Bytes[1] == (byte)0xFE) {
                charset = "UTF-16LE"; // 文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte)0xFE && first3Bytes[1] == (byte)0xFF) {
                charset = "UTF-16BE"; // 文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte)0xEF && first3Bytes[1] == (byte)0xBB && first3Bytes[2] == (byte)0xBF) {
                charset = "UTF-8"; // 文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                @SuppressWarnings("unused")
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0) {
                        break;
                    }
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                    {
                        break;
                    }
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                        // (0x80
                        // - 0xBF),也可能在GB编码内
                        {
                            continue;
                        } else {
                            break;
                        }
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;

    }

    /**
     * 往路径后加内容（连接处会自动加上"/"，结尾不会）
     *
     * @param path
     * @param add  add为空直接返回path
     * @return
     * @author: siyao
     * @date 2019年12月31日
     */
    public static String addPathAtEnd(String path, String... adds) {

        for (String str : adds) {
            if (str.isEmpty()) {
                continue;
            }
            if (path.endsWith("/")) {
                path = path + str;
            } else {
                path = path + "/" + str;
            }
        }

        return path;
    }

    /**
     * @param timeStr
     * @param format  例：YYYY-MM-DD hh:mm:ss
     * @return
     * @author: laosiyao
     * @date 2020/06/17
     */
    public static boolean isValidDate(String timeStr, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            formatter.parse(timeStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 给字符串加上括号
     *
     * @param content
     * @return
     */
    public static String parentheses(String content) {
        return String.format("\"%s\"", content);
    }

    public static String ASCIIformat(String str) {
        if (str.indexOf("&") > 0 && str.indexOf(";") < 0) {
            str = str.replaceAll("&", "&amp;");
        }
        if (str.indexOf("<") > 0 || str.indexOf(">") > 0) {
            str = str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        }
        str = str.replaceAll("\n", "&#x000A;");
        str = str.replaceAll("\"", "&quot;");

        return str;

    }

    public static void main(String[] args) {
        System.out.println(StringEscapeUtils.escapeXml11("222&333"));
    }

}
