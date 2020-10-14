import com.fasterxml.jackson.databind.util.BeanUtil;
import com.indrasoft.configservices.source.data.*;
import com.indrasoft.configservices.template.*;
import jdk.nashorn.internal.ir.ObjectNode;
import org.apache.commons.beanutils.*;
import org.apache.commons.io.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.*;
import org.mvel2.MVEL;
import org.mvel2.templates.TemplateRuntime;
import org.slf4j.*;
import sun.swing.BeanInfoUtils;

import javax.tools.JavaCompiler;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Author laosiyao
 * @Date 2020/9/19 2:42 下午.
 */
public class MvelTest {

    Logger logger = LoggerFactory.getLogger(MvelTest.class);

    //    String loadMvl() throws IOException {
    //        File file = new File("./src/test/resources/NoticeModelConfig.mvl");
    //        List<String> content = FileUtils.readLines(file, Charset.defaultCharset().name());
    //        return StringUtils.join(content, StringUtils.LF);
    //    }

    //    @Test
    public void testNormal() {
        logger.info(StringEscapeUtils.escapeXml11("qwweew\nsdsd"));
    }

    //    @Test
    public void test() throws Exception {

        //        Math

        Map<String, Object> params = new HashMap<>();
        params.put("content", "{\"1\":100, \"2\":300}");
        params.put("ft", "json");
        String template = loadMvl("./src/test/resources/excels/Trunk/mvl/CodeTest.mvl");

        //        Serializable serializable = MVEL.compileExpression(template, MvelTemplate.PARSER_CONTEXT_LOCAL.get());
        //        String output = String.valueOf(MVEL.executeExpression(serializable, params));

        Template template1 = MvelTemplate.of(template);
        String output = template1.getContent(params);

        logger.info("result=" + output);
    }

    public static class Content {

        public String content;
        public FieldInfo fieldInfo;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public FieldInfo getFieldInfo() {
            return fieldInfo;
        }

        public void setFieldInfo(FieldInfo fieldInfo) {
            this.fieldInfo = fieldInfo;
        }

        public Content(String content, FieldInfo fieldInfo) {
            this.content = content;
            this.fieldInfo = fieldInfo;
        }

        public Content() {
        }

    }

    @Test
    public void test3() throws Exception {

        //        Math

        //        Map<String, Object> params = new HashMap<>();
        //        params.put("content", "9999");

        Content params = new Content();
        params.setContent("99999");
        params.setFieldInfo(new FieldInfo("xen", "11", "int", "11", new HashMap<>()));

        BeanMap beanmap = new BeanMap(params);
        Map<String, Object> map = new HashMap<>();
        for (Object key : beanmap.keySet()) {
            map.put(key.toString(), beanmap.get(key.toString()));
        }

        //        Map map = BeanUtils.describe(params);
        String expresion
                = "def numberMaxLimit(limit, content){return content == \"en\" ? false : true;}numberMaxLimit(3000,fieldInfo.profile);";
        Serializable serializable = MVEL.compileExpression(expresion, MvelConst.PARSER_CONTEXT_LOCAL.get());

        Object resultObject = MVEL.executeExpression(serializable, map);

        logger.info("result=" + resultObject);
    }

    //    @Test
    public void test2() throws Exception {

        //                        String template = "Hello, my name is @{name.toUpperCase()}";
        String template = loadMvl("./src/test/resources/NoticeModelConfig.mvl");
        logger.info(template);
        Map<String, Object> params = new HashMap<>();
        List<Map<String, String>> contents = new ArrayList<>();
        Map<String, String> doc = new HashMap<>();
        doc.put("name", "Tom");
        doc.put("age", "30");
        Map<String, String> doc2 = new HashMap<>();
        doc2.put("name", "Jack");
        doc2.put("age", "29");
        contents.add(doc);
        contents.add(doc2);
        params.put("itemList", contents);

        String output = (String)TemplateRuntime.eval(template, params);

        logger.info(output);

        File outFile = new File("./src/test/resources/NoticeModelConfig.txt");
        FileUtils.writeStringToFile(outFile, output);
    }

    public static String loadMvl(String templatePath) throws Exception {
        InputStream in = null;
        String template = null;
        try {
            in = new FileInputStream(templatePath);
            template = IOUtils.toString(in, Charset.defaultCharset().name());
            return template;
        } catch (FileNotFoundException e) {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath);
            if (in == null) {
                throw e;
            }
        } finally {
            IOUtils.closeQuietly(in);
        }
        return null;
    }

}
