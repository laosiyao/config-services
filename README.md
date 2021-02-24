# 可扩展导表工具

- 时间 ：2020年10月13日 
- 作者 ：laosiyao
- 内容 ：
- 创建文档


## 概述

此工具基于java1.8，使用  [MVEL][mvel] 表达式，可自定义导出文件模板(mvl文件)、自定义内容校验公式。

## 说明

### 基础

#### 支持文件格式

- .xls
- .xlsx
- .tsv

#### 术语名词

- .mvl文件

  使用者自定义的内容格式模板，遵循 [MVEL][mvel] 表达式规则

- mvel参数

  程序解析excel后，将内容转为固定格式的参数，参与mvel运算

- excel

  这里将所有需导出的配置文件统称为excel

### excel内容格式

| {"tableName":"TestTable","desc":"描述描述"},"globalMvl":"/mvl/GlobalFunction.mvl"} | {"mvl":"...","output":"...","profiles":["en","cn","kr"]}     | {"mvl":"...","output":"..."} |            |          |         |                                 |
| :----------------------------------------------------------- | ------------------------------------------------------------ | ---------------------------- | ---------- | -------- | ------- | ------------------------------- |
| h:desc                                                       | 唯一id                                                       | 名称[中文]                   | 名称[默认] | 名称[韩] | 颜色    | 价格                            |
| h:user                                                       | cs                                                           | c                            | c          | c        | c       | cs                              |
| profile                                                      |                                                              | cn                           |            | kr       |         |                                 |
| fname                                                        | id                                                           | name                         | name       | name     | color   | price                           |
| ftype                                                        | int                                                          | string                       | string     | string   | array   | json                            |
| check                                                        | def numberMaxLimit(limit, content){return content > limit ? false : true;}numberMaxLimit(3000,content); |                              |            |          |         | checkResource("11",40,content); |
| d                                                            | 1002                                                         | 头盔                         | helmet     | 헬멧     | [2,3,4] | {"1":300,"5":90}                |
| d                                                            | 1003                                                         | 鞋子                         | shoes      | 헬멧     | [5,6,7] | {"1":300,"5":90}                |
| d                                                            | 1004                                                         | 衣服                         | cloth      | 헬멧     | [6,7,8] | {"1":300,"5":90}                |

#### 说明

- 第一列：行属性标识，用于定义这一行内容的意义

  1. A1单元格`[可选]`

     全局属性，位置固定。json格式，该内容可透传到mvel参数

     内置定义：

     globalMvl：自定义函数所在文件

  2. 以"h:"开头的标识`[可选]`

     用户自定义头名称(head:)，如示例中的"h:desc","h:user"

  3. "profile" `[可选]`

     profile专属字段，如果存在于专属字段同名的默认字段（如例子中的name），则自动过滤默认字段

  4. "fn"  `[必填]`

     字段名称(field name)。字段名名称前加[xx]标识，表示该字段为指定profile，会导出到profile专属文件夹下

  5. "ft"  `[必填]`

     字段类型(field type)

  6. "check" `[可选]`

     自定义字段内容检查（mvel表达式），如检查不通过会中断导表并弹出提示。可直接填写函数、或将函数定义在一个公共文件，在A1的globalMvl填写路径

  7. "d" `[必填]`

     内容正文(data)

  8. 留空

     留空的一行不做处理

- 第一行2-n列：模板路径及导出路径

  json格式。

  "mvl"`[必填]`：mvl模板路径

  "output"`[必填]`：导出路径

  "profiles"`[可选]`：列表，可导出n份文件，路径以profile命名。字段名前加[profile]，可限制导出

  例：{"mvl":"/mvl/ClientXml.mvl","output":"/client_xml/@{profile}/Hero.xml","profiles":["cn","en"]}

  其中mvl及output填的是root文件夹下的路径，root路径在工具上选择（见"导表示例"）

#### 标准字段类型

- Int,long,float,bool,json,array,string
- 标准字段类型自带校验规则，如校验不通过会中断导表并弹窗提示错误

#### 其他

- 可多个sheet，只要第一行有单元格内容符合{"mvl":"...","output":"..."}



### 数据结构

```java
// Attribute
String profile;
Map<String, Object> params; // A1 自定义参数
String fileName; // 文件名
String sheetName; // 工作表名
List<FieldInfo> fieldInfoList; // 字段信息
List<List<CellData>> itemList; // 具体内容（行列）Info

// FieldInfo
String fieldName;
String fieldType;
String checkCode;
Map<String, String> customHeader = new HashMap<>(); // 自定义headata

// CellData
String content;
FieldInfo fieldInfo;
Map<String, CellData> fieldList; // 同一行其余字段
```



### mvel表达式示例

- 用于内容校验的mvel表达式。必须返回一个boolean，否则校验不起作用。传入参数：CellData对象

  如示例中字段名为id的check行，定义了一个名为numberMaxLimit的函数，调用函数检查数值是否超过3000

  或可将函数放入另一个文件，在A1单元格通过globalMvl指定文件。

```java
def numberMaxLimit(limit, content){
  return content > limit ? false : true;
}
numberMaxLimit(3000,content);
```

- 导出内容mvel模板。文件需以.mvl结尾，见 [模板路径及导出路径]。传入参数：Attribute对象

```java
@comment{
  @comment{}括起来的内容不影响表达式
  @code{} 可穿于整个表达式,如下面escapeXml11()为预设函数（转义xml关键字符），更多预设函数见函数库
  注意：
  @code里面遵循java语法
  表达式计算抛出异常时，浮标定位不一定准确（坑）
}
<root profile="@{profile}">
    <header table="@{params.tableName}" desc="@{params.desc}">@comment{ --遍历所有字段,这样子还可以避免一次换行
    }@foreach{fieldInfo : fieldInfoList}@code{
            skipTag = false;
            // excel会有多余列
            if(fieldInfo.fieldName == ""){
                skipTag = true;
            }
        }@if{skipTag == false}
        <field type="@{fieldInfo.fieldType}" name="@{fieldInfo.fieldName}" desc="@{fieldInfo.customHeader.desc}" />  @end{}@end{}
    </header>

    @foreach{lineList : itemList}@code{
        skipTag = false;
    }<item @foreach{item : lineList}@code{
        // excel会有多余列
        if(item.fieldInfo.fieldName == ""){
            skipTag = true;
         }
        formatContent=escapeXml11(item.content)
    }@if{skipTag==false}@{item.fieldInfo.fieldName}="@{formatContent}"  @end{}@end{} />
    @end{}
</root>
```

结果

```xml
<root profile="kr">
    <header table="TestTable" desc="描述描述">
        <field type="int" name="id" desc="唯一id" />  
        <field type="string" name="name" desc="名称[韩]" />  
        <field type="array" name="color" desc="颜色" />  
        <field type="json" name="price" desc="价格" />  
    </header>

    <item id="1002"  name="헬멧"  color="[2,3,4]"  price="{&quot;1&quot;:300,&quot;5&quot;:90}"   />
    <item id="1003"  name="헬멧"  color="[5,6,7]"  price="{&quot;1&quot;:300,&quot;5&quot;:90}"   />
    <item id="1004"  name="헬멧"  color="[6,7,8]"  price="{&quot;1&quot;:300,&quot;5&quot;:90}"   />
    
</root>
```





## 附录，预设函数

- java.lang.Math

  double sin(double a)

  int round(float a)

  abs(int a)

  ...

- org.apache.commons.text.StringEscapeUtils

  String escapeXml11(final String input)   --转义xml关键字符

  String escapeJson(final String input) 

  ...

- org.apache.commons.lang3.StringUtils

  String join(final Object[] array, final String separator)

  String replaceChars(final String str, final String searchChars, String replaceChars)

  ...

- org.apache.commons.lang3.math.NumberUtils

- 其他

  HashMap toJson(String jsonStr)   

  boolean isJson(String str)

  boolean isArray(String str)





[mvel]:http://mvel.documentnode.com/#function-definition

