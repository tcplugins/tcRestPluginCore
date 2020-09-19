package teamcity.plugin.rest.core.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import jetbrains.buildServer.controllers.BaseController;

@Configuration
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class PluginConfiguration extends BaseController {

	private ApplicationContext applicationContext;

	public PluginConfiguration(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * Creates and returns a {@link RequestMappingHandlerAdapter} with
	 * the necessary {@link HttpMessageConverter}s configured.
	 *
	 * This code is copied from WebMvcConfigurationSupport. However we can't
	 * user WebMvcConfigurationSupport directly, as it wants access to the
	 * {@link ApplicationContext}, and the version supplied by TeamCity
	 * is not compatible.
	 *
	 * @return a RequestMappingHandlerAdapter instance
	 */
	@Bean
	public HandlerAdapter handlerAdapter() {
		RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
		adapter.setMessageConverters(getMessageConverters());
		return adapter;
	}

	@Bean
	public HandlerMapping handlerMapping() {
		return new RequestMappingHandlerMapping();
	}

	@Bean
	public HandlerExceptionResolver handlerExceptionResolver() {
		List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<>();
		addDefaultHandlerExceptionResolvers(exceptionResolvers);
		HandlerExceptionResolverComposite composite = new HandlerExceptionResolverComposite();
		composite.setOrder(0);
		composite.setExceptionResolvers(exceptionResolvers);
		return composite;
	}

	@SuppressWarnings("squid:S1452")
	protected final List<HttpMessageConverter<?>> getMessageConverters() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		addDefaultHttpMessageConverters(messageConverters);
		return messageConverters;
	}

	/**
	 * This code is copied from WebMvcConfigurationSupport. However we can't
	 * user WebMvcConfigurationSupport directly, as it wants access to the
	 * {@link ApplicationContext}, and the version supplied by TeamCity
	 * is not compatible.
	 * @param messageConverters List to add {@link HttpMessageConverter}s to.
	 */
	protected final void addDefaultHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
		stringConverter.setWriteAcceptCharset(false);

		messageConverters.add(new ByteArrayHttpMessageConverter());
		messageConverters.add(stringConverter);
		messageConverters.add(new ResourceHttpMessageConverter());
		messageConverters.add(new SourceHttpMessageConverter<Source>());
		messageConverters.add(new AllEncompassingFormHttpMessageConverter());

		// For the REST API, just support XML and JSON.
		messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
		messageConverters.add(new GsonHttpMessageConverter());
	}

	protected final void addDefaultHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		ExceptionHandlerExceptionResolver exceptionHandlerResolver = new ExceptionHandlerExceptionResolver();
		exceptionHandlerResolver.setMessageConverters(getMessageConverters());
		exceptionHandlerResolver.setApplicationContext(this.applicationContext);
		exceptionHandlerResolver.afterPropertiesSet();
		exceptionResolvers.add(exceptionHandlerResolver);

		ResponseStatusExceptionResolver responseStatusResolver = new ResponseStatusExceptionResolver();
		responseStatusResolver.setMessageSource(this.applicationContext);
		exceptionResolvers.add(responseStatusResolver);

		exceptionResolvers.add(new DefaultHandlerExceptionResolver());
	}

	/**
	 * We must implement {@link #doHandle(HttpServletRequest, HttpServletResponse)}, because
	 * we are a TeamCity Controller, however it's never used because we don't register ourselves
	 * into the URL space.
	 */
	@Override
	protected ModelAndView doHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}

}
