package com.ycz.guo;

import java.util.List;

/**
 * projectName: guo<br>
 * desc: TODO<br>
 * date: 2014年12月16日 下午3:09:14<br>
 * @author 开发者真实姓名[Andy]
 */
public class SearchResult {
	
	private String path;
	private String keyword;
	private List<SvnMeta> metas;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public List<SvnMeta> getMetas() {
		return metas;
	}
	public void setMetas(List<SvnMeta> metas) {
		this.metas = metas;
	}
}

