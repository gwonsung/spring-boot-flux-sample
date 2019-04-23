package com.sempio.wks.reactive.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

	public static byte[] getSHA256(String input){
		String res = "";
		byte[] sha256Byte = null;
		try{
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			
			sha256.update(input.getBytes());
			sha256Byte = sha256.digest();
		}
		finally{
			return sha256Byte;
		}
	}
	
	public static String getSHA256Str(String str){
		String SHA = ""; 
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			SHA = sb.toString();
			
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			SHA = null; 
		}
		return SHA;
	}
}
