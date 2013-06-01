package co.mczone.walls.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Files {
	public static void regen() {
		Chat.log("Deleting old world...");
		Bukkit.getServer().unloadWorld("world", false);
		rmdir(new File("world"));
		Random rand = new Random();
		int count = 0;
		for (String f : new File("backup").list())
			if (f.contains("world")) count += 1;
			
		Integer result = rand.nextInt(count);
		Chat.log("New world chosen: \"world" + (result+1) + "\"");
		try {
			copy(new File("backup","world" + (result+1)), new File("world"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String format(String string) {
	    String s = string;
	    for (ChatColor color : ChatColor.values()) {
	        s = s.replaceAll("(?i)<" + color.name() + ">", "" + color);
	    }
	    return s;
	}

	public static String read(File f) {
		String text="";
		int read,N=1024*1024;
		char[] buffer=new char[N];
		try {
			FileReader fr=new FileReader(f);
			BufferedReader br =new BufferedReader(fr);
			while(true) {
				read=br.read(buffer,0,N);
				text+=new String(buffer,0,read);
				if(read<N) break;
			}
			br.close();
			return text;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static void rmdir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				rmdir(new File(dir, children[i]));
			}
		}
		dir.delete();
	}
	
	public static void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void copy(File src, File dest) throws IOException {
		if (!src.exists()) {
			throw new IOException("[Walls]  Can not find source: " + src.getAbsolutePath()+".");
		} else if (!src.canRead()) {
			throw new IOException("[Walls]  No right to source: " + src.getAbsolutePath()+".");
		}
		if (src.isDirectory()) 	{
			if (!dest.exists()) { 
				if (!dest.mkdirs()) {
					throw new IOException("[Walls]  Could not create direcotry: " + dest.getAbsolutePath() + ".");
				}
			}
			String list[] = src.list();
			for (int i = 0; i < list.length; i++)
			{
				File dest1 = new File(dest, list[i]);
				File src1 = new File(src, list[i]);
				copy(src1 , dest1);
			}
		} else {
			FileInputStream fin = null;
			FileOutputStream fout = null;
			byte[] buffer = new byte[4096]; 
			int bytesRead;
			try {
				fin =  new FileInputStream(src);
				fout = new FileOutputStream (dest);
				while ((bytesRead = fin.read(buffer)) >= 0) {
					fout.write(buffer,0,bytesRead);
				}
			} catch (IOException e) {
				IOException wrapper = new IOException("[Walls]  Unable to copy file: " + src.getAbsolutePath() + "to" + dest.getAbsolutePath()+".");
				wrapper.initCause(e);
				wrapper.setStackTrace(e.getStackTrace());
				throw wrapper;
			} finally {
				if (fin != null) { fin.close(); }
				if (fout != null) { fout.close(); }
			}
		}
	}
}
