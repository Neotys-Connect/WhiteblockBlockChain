package com.neotys.ethereumJ.common.utils.Whiteblock.data;


import org.json.JSONObject;

public class WhiteblockStatus {
	final String test;
	final String org;
	final String def;
	final String phase;
	final int stepsLeft;
	final String message;
	final boolean finished;

	public WhiteblockStatus(JSONObject obj) {
		this.test = obj.getString("test");
		this.org = obj.getString("org");
		this.def = obj.getString("def");
		this.phase = obj.getString("phase");
		this.stepsLeft = obj.getInt("stepsLeft");
		this.message = obj.getString("message");
		this.finished = obj.getBoolean("finished");
	}
}
