package co.mczone.cmds;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.mczone.api.server.Hive;
import co.mczone.util.Chat;

public class RegisterCmd implements CommandExecutor {
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prevPass = null;
        ResultSet r = Hive.getInstance().getDatabase().query("SELECT password FROM players WHERE username='" + sender.getName() + "'");
        try {
			while (r.next()) {
				prevPass = r.getString("password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (prevPass == null) {
			// New registration
	        if (args.length!=1) {
	            Chat.player(sender, "&cIncorrect usage: /register <password>");
	            return true;
	        }
	        
	        
	        String pass = args[0];
	        if (pass.length() < 5) {
	        	Chat.player(sender, "&cYour password must be at least 5 characters!");
	        	return true;
	        }
	        
	        String md5 = md5("terrascape" + pass);
	        Hive.getInstance().getDatabase().update("UPDATE players SET password='" + md5 + "' WHERE username='" + sender.getName() + "'");
	        Chat.player(sender, "&aYou have successfully changed your password!");
	        Chat.player(sender, "&aVisit &bwww.mczone.co&a to login");
		}
		else {
			// Changing password
	        if (args.length!=2) {
	            Chat.player(sender, "&cUsage: /register <previous password> <new password>");
	            return true;
	        }
	        
	        
	        String oldPass = args[0];
	        
	        if (!md5("terrascape" + oldPass).equals(prevPass)) {
	        	Chat.player(sender, "&cIncorrect previous password!");
	        	return true;
	        }
	        
	        
	        String pass = args[1];
	        if (pass.length() < 5) {
	        	Chat.player(sender, "&cYour password must be at least 5 characters!");
	        	return true;
	        }
	        
	        String md5 = md5("terrascape" + pass);
	        Hive.getInstance().getDatabase().update("UPDATE players SET password='" + md5 + "' WHERE username='" + sender.getName() + "'");
	        Chat.player(sender, "&aYou have successfully changed your password!");
	        Chat.player(sender, "&aVisit &bwww.mczone.co&a to login");
		}
		
        return true;
    }
    
    public String md5(String input) {
        String result = input;
        if(input != null) {
            MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) {
                result = "0" + result;
            }
        }
        
        return result;
    }

}
