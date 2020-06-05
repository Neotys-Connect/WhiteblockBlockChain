package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import org.json.JSONObject;

public class WhiteblockBuildMeta {
	List<String> domains;
	JSONObject cloudDetails; // TODO: define these out so they can be used by our users
	JSONObject dockerAuth; // TODO: define these out so they can be used by our users
	String definitionRaw;

	public WhiteblockBuildMeta(String definitionRaw) {
		this.domains = null;
		this.cloudDetails = null;
		this.dockerAuth = null;
		this.definitionRaw = definitionRaw;
	}

	public WhiteblockBuildMeta(List<String> domains, String definitionRaw) {
		this.domains = domains;
		this.cloudDetails = null;
		this.dockerAuth = null;
		this.definitionRaw = definitionRaw;
	}

	public JSONObject marshalJSON()  {
		JSONObject out = new JSONObject();
		out.put("domains", domains);
		out.put("cloudDetails", cloudDetails);
		out.put("dockerAuth",dockerAuth);
		out.put("definitionRaw", definitionRaw);
		return out;
	}

	public String getDefinitionRaw() {
		return definitionRaw;
	}
	// TODO: possibly add getters and setters if needed
}