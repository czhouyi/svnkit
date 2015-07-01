package com.ycz.guo;

import java.util.Date;

/**
 * projectName: guo<br>
 * desc: TODO<br>
 * date: 2014年12月16日 下午2:32:54<br>
 * @author 开发者真实姓名[Andy]
 */
public class SvnMeta {
	
	private long revision;
	private String path;
	private String log;
	private String author;
	private Date date;
	private boolean haskey;
	
	public long getRevision() {
		return revision;
	}
	public void setRevision(long revision) {
		this.revision = revision;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isHaskey() {
		return haskey;
	}
	public void setHaskey(boolean haskey) {
		this.haskey = haskey;
	}
	
}

