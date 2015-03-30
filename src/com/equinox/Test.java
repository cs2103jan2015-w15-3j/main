package com.equinox;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {
	
	public static void main(String[] args){
	System.out.println(new File("").getAbsolutePath());
	
	Path relPath = Paths.get("");
	
	String absPathString = relPath.toAbsolutePath().toString();
	System.out.println(absPathString);
	File newFile = new File(absPathString);
		try{
			newFile.createNewFile();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
