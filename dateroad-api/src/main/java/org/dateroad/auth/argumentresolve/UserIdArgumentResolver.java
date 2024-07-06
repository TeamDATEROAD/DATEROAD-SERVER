package org.dateroad.auth.argumentresolve;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        //UserId 어노테이션을 parameter가 가지고 있는지 확인
        boolean isParamHasUserIdAnnotation = parameter.hasParameterAnnotation(UserId.class);

        //parameter의 타입이 Long인지 확인
        boolean isParamLongType = Long.class.equals(parameter.getParameterType());
        return isParamHasUserIdAnnotation && isParamLongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
