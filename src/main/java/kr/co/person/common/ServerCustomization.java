package kr.co.person.common;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ServerCustomization extends ServerProperties {

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		super.customize(container);
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error404"));
//		container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/jsp/500.jsp"));
//		container.addErrorPages(new ErrorPage("/jsp/error.jsp"));
	}
}
