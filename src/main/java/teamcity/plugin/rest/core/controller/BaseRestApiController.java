package teamcity.plugin.rest.core.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.intellij.openapi.diagnostic.Logger;

import teamcity.plugin.rest.core.Loggers;
import teamcity.plugin.rest.core.service.PermissionsService;

public abstract class BaseRestApiController {
	
	@Autowired
	PermissionsService permissionsService;
	
	
	public static final Logger restLogger = Loggers.rest(BaseRestApiController.class.getName());	

	public void checkIfAdministator() {
		permissionsService.checkAdmininstrator();
	}
	
}
