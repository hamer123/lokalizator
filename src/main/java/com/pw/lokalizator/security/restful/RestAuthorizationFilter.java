package com.pw.lokalizator.security.restful;
import com.pw.lokalizator.model.session.RestSession;
import com.pw.lokalizator.model.enums.Roles;
import com.pw.lokalizator.rest.resource.RestAttribute;
import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Provider
@Secured
@Priority(Priorities.AUTHORIZATION)
public class RestAuthorizationFilter implements ContainerRequestFilter{
	@Context 
	private HttpServletRequest request;
	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(!isLoginRequest(request)){
			RestSession restSession = (RestSession) request.getAttribute(RestAttribute.REST_SESSION);
			restSession.setLastUsed(new Date());

			// Get the resource class which matches with the requested URL
			// Extract the roles declared by it
			Class<?> resourceClass = resourceInfo.getResourceClass();
			List<Roles> classRoles = extractRoles(resourceClass);

			// Get the resource method which matches with the requested URL
			// Extract the roles declared by it
			Method resourceMethod = resourceInfo.getResourceMethod();
			List<Roles> methodRoles = extractRoles(resourceMethod);

			// Check if there are annotations on method or class, if they are check user permission
			try{
				if(!methodRoles.isEmpty()){
					checkPermissions(restSession,methodRoles);
				} else {
					if(!classRoles.isEmpty())
						checkPermissions(restSession,classRoles);
				}

				// Add SecurityContext to JAX-RS context
				requestContext.setSecurityContext(new SecurityContextRestful(restSession));
			} catch(Exception e){
				requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
			}

		}
	}

	private void checkPermissions(RestSession restSession, List<Roles>roles) throws Exception {
		for(Roles role : roles)
			if(restSession.isInRole(role)) return;

		throw new Exception("Authorization has been failed");
	}

	private boolean isLoginRequest(HttpServletRequest request){
		String requestURL = request.getRequestURI();
		return requestURL.endsWith("/login");
	}

	//Extract the roles from the annotated element
	private List<Roles> extractRoles(AnnotatedElement annotatedElement) {
		if (annotatedElement == null) {
			return new ArrayList<Roles>();
		} else {
			Secured secured = annotatedElement.getAnnotation(Secured.class);
			if (secured == null) {
				return new ArrayList<Roles>();
			} else {
				Roles[] allowedRoles = secured.value();
				return Arrays.asList(allowedRoles);
			}
		}
	}

	private class SecurityContextRestful implements SecurityContext{
		private List<Roles>roles = new ArrayList<>();
		private String email;

		public SecurityContextRestful(RestSession restSession){
			this.email = restSession.getUser().getEmail();
			this.roles = restSession.getUser().getRoles();
		}

		@Override
		public Principal getUserPrincipal() {
			return new Principal() {
				@Override
				public String getName() {return email; }
			};
		}

		@Override
		public boolean isUserInRole(String role) {
			for(Roles _role : roles)
				if(_role.name().equalsIgnoreCase(role))
					return true;
			return false;
		}

		@Override
		public boolean isSecure() {
			return request.isSecure();
		}

		@Override
		public String getAuthenticationScheme() {
			return "DIGEST_AUTH ";
		}
	}
}
