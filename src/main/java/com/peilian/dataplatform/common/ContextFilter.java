package com.peilian.dataplatform.common;

import com.peilian.dataplatform.config.GlobalVar;
import com.peilian.dataplatform.util.IdGen;
import com.peilian.dataplatform.util.NetUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 上下文设置
 */
@Log4j2
@Configuration
public class ContextFilter extends OncePerRequestFilter {

    @Value("${jwt.inner.domain:peilian.work}")
    private String innerDomain;

    @Value("${spring.application.name}")
    private String svName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            //清空当前线程副本
            UserThreadContext.requestStart();

            //从请求头获取logId，获取不到则生成一个塞到request请求属性中
            String logId = request.getHeader(GlobalVar.LOGID);
            if (logId == null || logId.length() < 1) {
                logId = IdGen.get62Str();
            }
            request.setAttribute(GlobalVar.LOGID, logId);
            //获取客户端IP，并判断是否内网IP
            String clientIp = NetUtil.getClientIpAddr(request);
            request.setAttribute(GlobalVar.IP, clientIp);
            boolean isInnerIp = NetUtil.isInnerIp(clientIp);
            //根据请求url、内部一级域名、当前服务在k8s中的serviceName判断用户请求地址类型
            String url = request.getRequestURL().toString();
            //0：外网url 1：内部特定域名url 2：内部ip或本地访问 3：serviceName访问
            int urlStatus = NetUtil.justUrlStatus(url, innerDomain, svName);
            //如果用户访问地址是内部地址或客户端IP是内网IP则认为是内部请求
            boolean isInnerRequest = urlStatus > 0 || isInnerIp;
            UserVisitor visitor = getRequestInfo(request);
            UserThreadContext.setUserVisitor(visitor);
            UserThreadContext.setAttribute(UserThreadContext.REQUEST_WORK_REQUEST_KEY,isInnerRequest);
            filterChain.doFilter(request,response);
        } finally {
            //清空当前线程副本
            UserThreadContext.requestEnd();
        }
    }

    /**
     * 把请求头塞到用户的上下文线程中
     *
     * @param request
     * @return
     */
    public UserVisitor getRequestInfo(HttpServletRequest request){
        String logId = request.getAttribute(GlobalVar.LOGID).toString();
        String jwtToken = request.getHeader(AppRequestConstants.HEADER_JWTTOKEN);
        UserVisitor visitor =  new UserVisitor();
        visitor.setLogId(logId);
        visitor.setJwtToken(jwtToken);
        return visitor;
    }

}
