package teamcity.plugin.rest.core;

import java.util.Collection;

public interface RestApiMappings {

	/**
	 * <p>A Collection of mapping prefixes to be mapped into the TeamCity
	 * URL namespace. Mappings should be defined as per the examples below and determine
	 * the mapping location for your rest controllers.</p>
	 * <p><strong>Be careful not to map anything over the top of an existing REST service.</strong></p>
	 * <p>The "<code>/app/rest</code>" will be prepended when the mapping is created.<br>
	 * Example:
	 * <ul>
	 * <li><code>/plugins/** -&gt; /app/rest/plugins/**</code> (maps any controller with that prefix)</li>
	 * <li><code>/server/restart -&gt; /app/rest/server/restart</code> (maps just that single location)</li>
	 * </ul></p>
	 * 		
	 * @return Collection of mapping prefixes.
	 */
	Collection<String> getMappingPrefixes();
}
