package com.indrasoft.configservices.template;

import com.indrasoft.configservices.source.data.Attributes;

import java.util.Map;

/**
 * @Author laosiyao
 * @Date 2020/9/29 10:59 上午.
 */
public interface Template {

    String getContent(Attributes attributes);

    String getContent(Map attributes);

}


