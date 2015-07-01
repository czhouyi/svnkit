package com.ycz.guo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNFileRevision;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * projectName: guo<br>
 * desc: TODO<br>
 * date: 2014年12月16日 下午3:04:42<br>
 * @author 开发者真实姓名[Andy]
 */
public class SvnSearch {
	
	String url = "http://192.168.11.11:81/chinadaas";
	String name = "yichuanzhou";
	String password = "yichuanzhou";
	public static String ROOT = "/chinadaas";
	public static String GSINFO = "/chinadaas/09DEV/09Project/trunk/gsinfo";
	SVNRepository repository = null;
	
	public SvnSearch() {
		init();
	}
	
	public SVNRepository getRepository() {
		return repository;
	}

	private void setup() {
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();
	}
	
	private void init() {
		setup();
		
    	SVNURL repositoryURL = null;
    	try {
    		repositoryURL = SVNURL.parseURIEncoded(url);
    		repository = SVNRepositoryFactory.create(repositoryURL);
    	} catch (SVNException e) {
    		System.err.println("创建版本库实例时失败，版本库的URL是 '" + url + "': " + e.getMessage());
    		System.exit(1);
    	}
    	ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
    	repository.setAuthenticationManager(authManager);
	}
	
	public long getLatestRevision() throws SVNException {
		return repository.getLatestRevision();
	}
	
	public Collection<SVNFileRevision> getAllFileRevisions(String path) throws SVNException {
		return repository.getFileRevisions(path, null, 0, getLatestRevision());
	}
	
	public String searchFile(String path, String fileName) throws SVNException {
		path = path.replaceFirst(ROOT, "");
		Collection<SVNDirEntry> entires = repository.getDir(path, -1, null, (Collection<SVNDirEntry>)null);
		for(Iterator<SVNDirEntry> it = entires.iterator();it.hasNext();) {
			SVNDirEntry entry = it.next();
			
			if(entry.getKind() == SVNNodeKind.DIR) {
				String result = searchFile(entry.getURL().getPath(), fileName);
				if(result != null && result.length() > 0) {
					return result;
				}
			} else if(entry.getKind() == SVNNodeKind.FILE) {
				if(fileName.equals(entry.getName())) {
					return entry.getURL().getPath();
				}
			}
		}
		return null;
	}
	
	public void findMeta(List<SvnMeta> metas, String keyword) {
		for (int i = 0, n = metas.size(); i < n; i++) {
			SvnMeta meta = metas.get(i);
			meta.setHaskey(find(meta, keyword));
		}
	}
	
	public boolean find(SvnMeta sm, String keyword) {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			repository.getFile(sm.getPath(), sm.getRevision(), null, out);
			return out.toString().contains(keyword);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public SearchResult search(String fileName, String keyword) throws Exception {
		String rs = searchFile(GSINFO, fileName);
    	
    	Collection<SVNFileRevision> revisions = getAllFileRevisions(rs.replaceFirst(ROOT, ""));
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	List<SvnMeta> metas = new ArrayList<SvnMeta>();
    	for (SVNFileRevision sfr : revisions) {
    		SvnMeta meta = new SvnMeta();
    		meta.setRevision(sfr.getRevision());
    		meta.setPath(sfr.getPath());
    		meta.setLog(getString(sfr.getRevisionProperties().get("svn:log")));
    		meta.setAuthor(getString(sfr.getRevisionProperties().get("svn:author")));
    		meta.setDate(sdf.parse(getString(sfr.getRevisionProperties().get("svn:date")).substring(0, 19).replace("T", " ")));
    		metas.add(meta);
    	}
    	
    	findMeta(metas, keyword);
    	
    	SearchResult sr = new SearchResult();
    	sr.setPath(rs);
    	sr.setKeyword(keyword);
    	sr.setMetas(metas);
    	
    	return sr;
	}
	
	public void close() throws SVNException {
		repository.closeSession();
	}
	
	public static String getString(Object obj) {
		return obj == null ? "" : obj.toString();
	}
}

