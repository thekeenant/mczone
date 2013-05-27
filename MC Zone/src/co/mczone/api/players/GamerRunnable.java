package co.mczone.api.players;

import lombok.Getter;
import lombok.Setter;

public class GamerRunnable implements Runnable {
	@Getter @Setter	public Gamer gamer;
	@Getter @Setter boolean sync = false;
	
	@Override
	public void run() {
		
	}

	public GamerRunnable newInstance() {
		GamerRunnable r = new GamerRunnable();
		r.setSync(sync);
		return r;
	}
}
