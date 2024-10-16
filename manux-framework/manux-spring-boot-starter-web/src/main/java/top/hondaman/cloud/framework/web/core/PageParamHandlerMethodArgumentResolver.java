package top.hondaman.cloud.framework.web.core;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import top.hondaman.cloud.framework.common.pojo.SortParam;
import top.hondaman.cloud.infra.system.model.PageParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class PageParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PageParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        PageParam pageParam = new PageParam();
        pageParam.setPage(new Integer(webRequest.getNativeRequest(HttpServletRequest.class).getParameter("page")));
        pageParam.setLimit(new Integer(webRequest.getNativeRequest(HttpServletRequest.class).getParameter("limit")));

        //默认插入时间倒序
        List<SortParam> sortList = Arrays.asList(
                new SortParam(){{setName("insert_time"); setOrder(Order.DESC);}});
        pageParam.setSort(sortList);

        return pageParam;
    }
}
