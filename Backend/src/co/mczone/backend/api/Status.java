package co.mczone.backend.api;

import lombok.Getter;

public enum Status {

	CLOSED(0),
	OPEN(1);
	
	@Getter int value;
	Status(int value) {
		this.value = value;
	}
	
	public static Status getByValue(int value) {
		if (value == 0)
			return Status.CLOSED;
		else
			return Status.OPEN;
	}
	
}
