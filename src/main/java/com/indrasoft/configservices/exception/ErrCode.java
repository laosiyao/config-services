/**
 * @author: laosiyao
 * @date 2020年6月17日
 * @copyright IndraSoft
 */
package com.indrasoft.configservices.exception;

/**
 *
 * @author: laosiyao
 * @date 2020年6月17日
 * @copyright IndraSoft
 */
public interface ErrCode {

    String getCode();

    String getDesc();

    Boolean isPrintStack();

}
