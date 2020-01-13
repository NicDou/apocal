package com.jd.common.bean.aop;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jd.common.constants.SecurityConstants;

@Slf4j
@Aspect
@Component
public class ControllerAop {

	@Pointcut("execution(public com.jd.common.utils.RestServiceResult *(..))")
	public void pointCutR() {
	}

	/**
	 * 拦截器具体实现
	 *
	 * @param pjp
	 *            切点 所有返回对象R
	 * @return R 结果包装
	 */
	@Around("pointCutR()")
	public Object methodRHandler(ProceedingJoinPoint pjp) throws Throwable {
		return methodHandler(pjp);
	}

	@Pointcut("execution(public com.baomidou.mybatisplus.extension.plugins.pagination.Page *(..))")
	public void pointCutPage() {
	}

	/**
	 * 拦截器具体实现
	 *
	 * @param pjp
	 *            切点 所有返回对象Page
	 * @return R 结果包装
	 */
	@Around("pointCutPage()")
	public Object methodPageHandler(ProceedingJoinPoint pjp) throws Throwable {
		return methodHandler(pjp);
	}

	private Object methodHandler(ProceedingJoinPoint pjp) throws Throwable {
		long startTime = System.currentTimeMillis();

		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		String username = request.getHeader(SecurityConstants.USER_HEADER);
		if (StringUtils.isNotBlank(username)) {
			log.info("Controller AOP get username:{}", username);

		}

		log.info("URL : " + request.getRequestURL().toString());
		log.info("HTTP_METHOD : " + request.getMethod());
		log.info("IP : " + request.getRemoteAddr());
		log.info("CLASS_METHOD : " + pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
		log.info("ARGS : " + Arrays.toString(pjp.getArgs()));

		Object result;

		result = pjp.proceed();
		log.info("{} use time:{} ms", pjp.getSignature(), System.currentTimeMillis() - startTime);

		return result;
	}

}
