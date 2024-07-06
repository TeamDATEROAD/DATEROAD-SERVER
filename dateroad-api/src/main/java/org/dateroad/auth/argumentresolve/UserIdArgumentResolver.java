package org.dateroad.auth.argumentresolve;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        //UserId 어노테이션을 parameter가 가지고 있는지 확인
        boolean isParamHasUserIdAnnotation = parameter.hasParameterAnnotation(UserId.class);

        //parameter의 타입이 long인지 확인 -> 필터에서 이미 long인지 타입이 확인되는데 필요할까? 의문이 생김
        boolean isParamlongType = long.class.equals(parameter.getParameterType());

        return isParamHasUserIdAnnotation && isParamlongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
