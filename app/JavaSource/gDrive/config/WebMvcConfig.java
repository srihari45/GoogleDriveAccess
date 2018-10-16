package gDrive.config;

import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import gDrive.utils.UIFormConstants;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"gDrive.actions.controller.pub", "gDrive.actions.base"})
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setDefinitions("/WEB-INF/views/**/tiles-public.xml");
		tilesConfigurer.setCheckRefresh(true);
		return tilesConfigurer;
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		TilesViewResolver tilesViewResolver = new TilesViewResolver();
		registry.viewResolver(tilesViewResolver);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
		registry.addResourceHandler("/fonts/roboto/**").addResourceLocations("/fonts/roboto/");
		registry.addResourceHandler("/images/**").addResourceLocations("/images/");
		//registry.addResourceHandler("/app/**").addResourceLocations("/app/");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName(UIFormConstants.TILES_INDEX);
		// registry.addStatusController("/pub/403.html", HttpStatus.FORBIDDEN);
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		return new MultipartConfigElement("");
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(1000000000);
		return multipartResolver;
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		messageSource.setCacheSeconds(5);
		return messageSource;
	}

	/*@Bean
	public SimpleMappingExceptionResolver exceptionResolver() {
		ExceptionActionControllerResolver exceptionResolver = new ExceptionActionControllerResolver();
		Properties properties = new Properties();
		properties.put("org.springframework.web.HttpSessionRequiredException", "exception");
		exceptionResolver.setExceptionMappings(properties);
		exceptionResolver.setDefaultErrorView("exception");
		return exceptionResolver;
	}*/

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		resolvers.add(new ExceptionHandlerExceptionResolver());
	}

}
