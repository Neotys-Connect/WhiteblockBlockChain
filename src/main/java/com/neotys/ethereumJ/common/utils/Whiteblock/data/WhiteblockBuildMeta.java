package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import org.bouncycastle.util.encoders.Base64;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WhiteblockBuildMeta {
	private List<String> domains;
	final JSONObject cloudDetails; // TODO: define these out so they can be used by our users
	final JSONObject dockerAuth; // TODO: define these out so they can be used by our users
	final String definitionRaw;
	final String folderPath;

	public WhiteblockBuildMeta(String definitionRaw, String path) {
		this.domains = null;
		this.cloudDetails = null;
		this.dockerAuth = null;
		this.definitionRaw = definitionRaw;
		File fpath=new File(path);
		folderPath=fpath.toPath().getParent().toString();
	}

	public void addDomain(String domain) {
		if(domains == null) {
			domains = new ArrayList<>();
		}
		domains.add(domain);
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

	public JSONObject marshalJSON()  {
		JSONObject out = new JSONObject();
		if(domains != null) {
			out.put("domains", domains);
		}
		out.put("cloudDetails", cloudDetails);
		out.put("dockerAuth",dockerAuth);
		out.put("definitionRaw", new String(Base64.encode(definitionRaw.getBytes())));
		return out;
	}

	public String getDefinitionRaw() {
		return definitionRaw;
	}
}