package teamcity.plugin.rest.core;

import com.intellij.openapi.diagnostic.Logger;

public final class Loggers {
	public static final Logger SERVER 		= Logger.getInstance("jetbrains.buildServer.SERVER");
	public static final Logger ACTIVITIES  	= Logger.getInstance("jetbrains.buildServer.ACTIVITIES");
	
	private Loggers(){}
	
	public static Logger rest(String logSuffix) {
		return Logger.getInstance("jetbrains.buildServer.server.rest." + logSuffix);
	}
}