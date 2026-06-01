package kz.am.imagehosting.security;

import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

import org.springframework.security.web.csrf.CsrfToken;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
	private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
	private CsrfTokenRequestHandler xor;

	public SpaCsrfTokenRequestHandler() {
		XorCsrfTokenRequestAttributeHandler requestHandler = new XorCsrfTokenRequestAttributeHandler();
		requestHandler.setCsrfRequestAttributeName(null);
		this.xor = requestHandler;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
		/*
		 * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection of
		 * the CsrfToken when it is rendered in the response body.
		 */
		// System.out.println("handle");
		// System.out.print("request.getAttribute(CsrfToken.class.getName()). ");
		// System.out.print(CsrfToken.class.getName());
		// System.out.println(request.getAttribute(CsrfToken.class.getName()));
		// System.out.print("request.getAttribute('_csrf'), ");
		// System.out.println(request.getAttribute("_csrf"));
		this.xor.handle(request, response, csrfToken);
		/*
		 * Render the token value to a cookie by causing the deferred token to be loaded.
		 */
		csrfToken.get();
	}

	@Override
	public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
		// System.out.println("resolveCsrfTokenValue");
		// System.out.print("csrfToken.getHeaderName(), ");
		// System.out.println(csrfToken.getHeaderName());
		// System.out.print("csrfToken.getParameterName(), ");
		// System.out.println(csrfToken.getParameterName());
		// System.out.print("csrfToken.getToken(), ");
		// System.out.println(csrfToken.getToken());
		String headerValue = request.getHeader(csrfToken.getHeaderName());
		// System.out.print("headerValue, ");
		// System.out.println(headerValue);
		/*
		 * If the request contains a request header, use CsrfTokenRequestAttributeHandler
		 * to resolve the CsrfToken. This applies when a single-page application includes
		 * the header value automatically, which was obtained via a cookie containing the
		 * raw CsrfToken.
		 *
		 * In all other cases (e.g. if the request contains a request parameter), use
		 * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
		 * when a server-side rendered form includes the _csrf request parameter as a
		 * hidden input.
		 */
		return (StringUtils.hasText(headerValue) ? this.plain : this.xor).resolveCsrfTokenValue(request, csrfToken);
	}
}