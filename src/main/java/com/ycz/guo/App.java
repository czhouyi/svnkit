package com.ycz.guo;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	SvnSearch svnsearch = new SvnSearch();

    	String fileName = "DESUtils.java";
    	String keyword = "public static String decrypt(String key, String message)";
    	SearchResult sr = svnsearch.search(fileName, keyword);
    	svnsearch.close();
    	
    	System.out.println("file : " + sr.getPath());
    	System.out.println("keyword : " + sr.getKeyword());
    	for(SvnMeta meta : sr.getMetas()) {
    		System.out.printf("%s-%s-%s-%s-%s\n", meta.getRevision(), 
    				meta.isHaskey()?"yes":"no", meta.getAuthor(), meta.getDate(), meta.getLog());
    	}
    }
}
