package co.mczone.api.players;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import co.mczone.util.WorldUtil;

public class SignChangePacket {
	@Getter static List<SignChangePacket> list = new ArrayList<SignChangePacket>();
	@Getter Player player;
	@Getter Sign sign;
	@Getter String[] lines;
	@Getter int revertTime;
	@Getter Date sentTime;
	
	@Getter boolean reverted = false;
	
	public SignChangePacket(Player player, Sign sign, String[] lines, int revertTime) {
		this.player = player;
		this.sign = sign;
		this.lines = lines;
		this.revertTime = revertTime;
		this.sentTime = new Date();
		list.add(this);
	}
	
	public void send() {
		if (player == null || getSign() == null || lines == null)
			return;
		
		WorldUtil.sendSignChange(player, getSign(), lines);
	}
	
	public void revert() {
		reverted = true;
		WorldUtil.sendSignChange(player, getSign(), getSign().getLines());
	}
}
