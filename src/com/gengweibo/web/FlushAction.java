/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.web;

import z.action.ActionSupport;
import z.action.Result;
import z.action.api.Api;

/**
 * @author auzll@msn.com
 * @since 2011-3-10
 */
public class FlushAction extends ActionSupport {
    private static final Result RESULT = new Result("/WEB-INF/jsp/result.jsp")
            .addValue("result", "{\"status\":\"ok\"}");

    @Api("flush")
    public Result flush() {
        return RESULT;
    }
}
