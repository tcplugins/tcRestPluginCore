package teamcity.plugin.rest.core.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.intellij.openapi.diagnostic.Logger;

import jetbrains.buildServer.serverSide.auth.AccessDeniedException;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.web.util.SessionUser;
import teamcity.plugin.rest.core.Loggers;

@Service
public class PermissionsService {
	
	public static final Logger restLogger = Loggers.rest(PermissionsService.class.getName());
	
	public boolean checkAdmininstrator() {

		SUser user;
		try {
			user = getUser();
		} catch (Exception ex) {
			restLogger.warn("PermissionsService:: Unable to determine user");
			throw new AccessDeniedException(null, "Unable to determine user");
		}
		if (getUser().isSystemAdministratorRoleGranted()) {
			restLogger.debug(String.format("PermissionsService:: User '%s' has SystemAdministratorRoleGranted",user.getUsername()));
			return true;
		} else {
			throw new AccessDeniedException(getUser(), String.format("User '%s' is not a system administrator", user.getUsername()));
		}
	}
	
	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	}
	
	public SUser getUser() {
		return SessionUser.getUser(getRequest());
	}

}
