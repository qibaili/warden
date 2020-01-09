package com.qibaili.warden.web.system.shiro;

import com.alibaba.fastjson.JSON;
import com.qibaili.warden.web.system.common.bean.ResponseCode;
import com.qibaili.warden.web.system.common.bean.ResponseResult;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author qibaili
 * @date 2019/10/13
 */
public class ShiroAuthcFilter extends FormAuthenticationFilter {

    /**
     * 如果 isAccessAllowed 返回 true 则 onAccessDenied 方法不会继续执行
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginRequest(request, response)) {
            return true;
        } else {
            Subject subject = getSubject(request, response);
            // If principal is not null, then the user is known and should be allowed access.
            return subject.getPrincipal() != null;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        renderJson(httpServletResponse, ResponseResult.e(ResponseCode.SESSION_TIME_OUT, false));
        return false;
    }

    /**
     * 渲染json对象
     */
    private static void renderJson(HttpServletResponse response, Object jsonObject) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
