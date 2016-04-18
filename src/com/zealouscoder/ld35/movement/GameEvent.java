package com.zealouscoder.ld35.movement;

import java.util.Properties;

public class GameEvent {

	private String			type;
	private Properties	props;

	public GameEvent(String type, Properties props) {
		this.type = type;
		this.props = props;
	}
	
	public Properties getProps() {
		return props;
	}

	public String getType() {
		return type;
	}

}
