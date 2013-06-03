package co.mczone.nexus.api;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import co.mczone.nexus.enums.GameState;

public class Rotary {

	@Getter @Setter GameState state;
	@Getter @Setter int time;

	public Rotary() {
		this.state = GameState.STARTING;
	}
	
	public Map getCurrentMap() {
		return null;
	}
	
	
	
}
