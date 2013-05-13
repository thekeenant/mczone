package co.mczone.pets.api;

import lombok.Getter;

public enum Age {
	BABY(0),
	ADULT(1);
	
	@Getter int id;
	private Age(int id) {
		this.id = id;
	}
}
