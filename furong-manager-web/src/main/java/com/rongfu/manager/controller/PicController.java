package com.rongfu.manager.controller;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rongfu.common.PicUploadResult;

@Controller
@RequestMapping("pic")
public class PicController {

	@Value("${RONGFU_IMAGE_SERVER_URL}")
	private String RONGFU_IMAGE_SERVER_URL;

	private String[] TYPES = { ".jpg", ".png", ".jpeg", ".gif", ".tmp" };
	
	//1、把java对象转成json串;2、把json串转成java对象;3、json串直接解析
	private final ObjectMapper mapper = new ObjectMapper();

	// filePostName : "uploadFile", //上传的文件名
	// uploadJson : '/rest/pic/upload', //上传的路径
	// dir : "image" //上传的文件类型
	@RequestMapping(value = "upload", produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String upload(MultipartFile uploadFile) throws Exception {
		PicUploadResult result = new PicUploadResult();
		// 默认上传失败
		result.setError(1);
		// 验证通过标识
		boolean flag = false;
		// 验证图片的后缀是否正确
		for (String type : TYPES) {
			if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
				flag = true;
				break;
			}
		}
		// 后缀正确,验证图片内容
		if (flag) {
			// 重置验证标识
			flag = false;
			try {
				BufferedImage image = ImageIO.read(uploadFile.getInputStream());
				if (image != null) {
					// 标识验证通过
					flag = true;
					// 保存图片高与宽
					result.setWidth("" + image.getWidth());
					result.setHeight("" + image.getHeight());
				}
			} catch (Exception e) {
			}
		}

		// 图片验证成功
		if (flag) {
			// 1、创建tracker.conf配置文件，内容就是tracker服务的地址。
			// 配置文件内容：tracker_server=192.168.37.161:22122，
			// 然后加载配置文件(ClientGlobal.init方法加载)
			ClientGlobal.init(System.getProperty("user.dir") + "/src/main/resources/tracker.conf");
			// 2、创建一个TrackerClient对象。直接new一个。
			TrackerClient trackerClient = new TrackerClient();
			// 3、使用TrackerClient对象创建连接，getConnection获得一个TrackerServer对象。
			TrackerServer trackerServer = trackerClient.getConnection();
			// 4、创建一个StorageServer的引用，值为null，为接下来创建StorageClient使用
			StorageServer storageServer = null;
			// 5、创建一个StorageClient对象，直接new一个，需要两个参数TrackerServer对象、StorageServer的引用
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);
			// 6、使用StorageClient对象upload_file方法上传图片。
			// 后缀名
			String ext = StringUtils.substringAfterLast(uploadFile.getOriginalFilename(), ".");
			String[] paths = storageClient.upload_file(uploadFile.getBytes(), ext, null);
			// 7、返回数组。包含组名和图片的路径，打印结果。
			result.setUrl(this.RONGFU_IMAGE_SERVER_URL + paths[0] + "/" + paths[1]);
			// 设置图片上传成功
			result.setError(0);
		}
		String str = null;
		try {
			str = mapper.writeValueAsString(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
