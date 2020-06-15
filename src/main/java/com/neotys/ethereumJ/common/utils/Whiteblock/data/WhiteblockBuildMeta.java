package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class WhiteblockBuildMeta {
	final List<String> domains;
	final JSONObject cloudDetails; // TODO: define these out so they can be used by our users
	final JSONObject dockerAuth; // TODO: define these out so they can be used by our users
	final String definitionRaw;
	final String folderPath;

	public WhiteblockBuildMeta(String definitionRaw,String path) {
		this.domains = null;
		this.cloudDetails = null;
		this.dockerAuth = null;
		this.definitionRaw = definitionRaw;
		File fpath=new File(path);
		folderPath=fpath.toPath().getParent().toString();
	}

	public List<String> getDomains() {
		return domains;
	}

	public JSONObject getCloudDetails() {
		return cloudDetails;
	}

	public JSONObject getDockerAuth() {
		return dockerAuth;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public WhiteblockBuildMeta(List<String> domains, String definitionRaw, String folderPath) {
		this.domains = domains;
		this.cloudDetails = null;
		this.dockerAuth = null;
		this.definitionRaw = definitionRaw;
		this.folderPath=folderPath;
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