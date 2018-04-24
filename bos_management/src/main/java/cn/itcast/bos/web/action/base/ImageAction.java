package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ImageAction extends BaseAction<Object> {

	private File imgFile;
	private String imgFileFileName;
	private String imgFileContentType;

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}

	@Action(value = "image_upload", results = { @Result(name = "success", type = "json") })
	public String upload() {
		System.out.println(imgFile);
		// 获取项目中的路径
		String realPath = ServletActionContext.getServletContext().getRealPath("/upload");
		UUID uuid = UUID.randomUUID();
		String filename = uuid + imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
		try {
			FileUtils.copyFile(imgFile, new File(realPath, filename));
			String url = ServletActionContext.getRequest().getContextPath();
			url = url + "/upload/" + filename;
			Map<String, Object> map = new HashMap<>();
			map.put("error", 0);
			map.put("url", url);
			ServletActionContext.getContext().getValueStack().push(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	private String path;

	public void setPath(String path) {
		this.path = path;
	}

	@Action(value = "image_manage", results = { @Result(name = "success", type = "json") })
	public String manage() {
		File currentPathFile;
		String rootPath;
		String rootUrl;
		System.out.println(path);
		// 获取存储图片的根路径
		if (path.length() != 0) {
			String rePath = "/" + path.substring(path.indexOf("upload"));

			rootPath = ServletActionContext.getServletContext().getRealPath(rePath) + "/";
			rootUrl = ServletActionContext.getRequest().getContextPath() + rePath + "/";
			currentPathFile = new File(rootPath);
		} else {
			System.out.println("------------------------");
			rootPath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";
			// 获取根url
			rootUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
			// 当前文件
			currentPathFile = new File(rootPath);
		}

		// 遍历目录里的所有信息
		List<Map<String, Object>> filelist = new ArrayList<Map<String, Object>>();

		// 文件类型
		String[] filetypes = new String[] { "gif", "jpg", "jpeg", "bmp", "png" };

		// 遍历根路径下的文件
		for (File file : currentPathFile.listFiles()) {
			String filename = file.getName();
			Map<String, Object> hash = new HashMap<>();
			if (file.isDirectory()) {
				hash.put("is_dir", true); // 标记为文件夹
				hash.put("has_file", file.listFiles() != null); // 标记文件夹里是否有文件
				hash.put("filesize", 0L);
				hash.put("is_photo", false);
				hash.put("filetype", "");
			} else if (file.isFile()) {
				// 是文件
				String fileExt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
				hash.put("is_dir", false);
				hash.put("has_file", false);
				hash.put("filesize", file.length());
				hash.put("is_photo", Arrays.asList(filetypes).contains(fileExt));
				hash.put("filetype", fileExt);
			}
			hash.put("filename", filename);
			hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
			filelist.add(hash);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", rootUrl);
		result.put("total_count", filelist.size());
		result.put("file_list", filelist);
		ServletActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}
