package com.qpark.eip.core.spring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.channel.AbstractMessageChannel;

/**
 * Adds all {@link EipWsChannelInterceptor}s to the
 * {@link AbstractMessageChannel}s having names ending with
 * <i>WsChannelRequest</i> or <i>WsChannelResponse</i>.
 * 
 * @author bhausen
 */
public class EipWsChannelInterceptorInitializer implements InitializingBean, ApplicationContextAware {
    /** The {@link ApplicationContext}. */
    private ApplicationContext applicationContext;

    /** The {@link org.slf4j.Logger}. */
    private final Logger logger = LoggerFactory.getLogger(EipWsChannelInterceptorInitializer.class);

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
	this.logger.debug("+afterPropertiesSet");
	int i = 0;
	String[] channelNames = this.applicationContext.getBeanNamesForType(AbstractMessageChannel.class);
	String[] interceptorNames = this.applicationContext.getBeanNamesForType(EipWsChannelInterceptor.class);

	List<EipWsChannelInterceptor> interceptors = new ArrayList<EipWsChannelInterceptor>(interceptorNames.length);
	Object proxy;
	for (String interceptorName : interceptorNames) {
	    proxy = this.applicationContext.getBean(interceptorName);
	    interceptors.add(this.getTargetObject(proxy, EipWsChannelInterceptor.class));
	}
	AbstractMessageChannel channel;
	for (String channelName : channelNames) {
	    if (channelName.startsWith("eip")
		    && (channelName.endsWith("WsChannelRequest") || channelName.endsWith("WsChannelResponse"))) {
		try {
		    proxy = this.applicationContext.getBean(channelName);
		    channel = this.getTargetObject(proxy, AbstractMessageChannel.class);
		    for (EipWsChannelInterceptor interceptor : interceptors) {
			channel.addInterceptor(interceptor);
		    }
		    i++;
		} catch (RuntimeException e) {
		    this.logger.error(e.getMessage());
		} catch (Exception e) {
		    this.logger.error(e.getMessage());
		} catch (Throwable e) {
		    this.logger.error(e.getMessage());
		}
	    }
	}

	this.logger.debug(
		"-afterPropertiesSet AbstractMessageChannels {}, EipWsChannnelInterceptors {}, channels changed {}",
		channelNames.length, interceptorNames.length, i);
    }

    @SuppressWarnings("unchecked")
    private <T> T getTargetObject(final Object proxy, final Class<T> targetClass) throws Exception {
	if (AopUtils.isJdkDynamicProxy(proxy)) {
	    return (T) ((Advised) proxy).getTargetSource().getTarget();
	} else {
	    return (T) proxy;
	}
    }

    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
	this.applicationContext = applicationContext;
    }
}
