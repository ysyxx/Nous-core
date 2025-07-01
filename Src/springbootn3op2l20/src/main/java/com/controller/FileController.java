package com.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files; // 新增导入
import java.nio.file.Path;   // 新增导入
import java.nio.file.Paths;  // 新增导入
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID; // 新增导入

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping; // 新增导入
import org.springframework.web.bind.annotation.GetMapping;  // 新增导入


import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ConfigEntity;
import com.entity.EIException; // 假设这是你的自定义异常
import com.service.ConfigService;
import com.utils.R;

/**
 * 上传文件映射表
 */
@RestController
@RequestMapping("file")
@SuppressWarnings({"unchecked","rawtypes"})
public class FileController{
	@Autowired
	private ConfigService configService;

	// 统一的文件上传目录定义
	// 请根据你的实际部署环境调整此路径。
	// 如果是生产环境，建议配置为服务器上的一个固定目录，而不是项目资源目录内部，
	// 以避免重新部署时文件丢失。
	// 示例：String baseUploadPath = "/opt/upload/"; 或者 "D:/upload/";
	// 这里保留你原始代码中 ResourceUtils 的尝试，但更推荐直接指定绝对路径或通过配置文件读取
	private String uploadFolder;

	public FileController() {
		try {
			// 尝试获取classpath路径下的static/upload目录
			// 注意：在打成jar包部署时，ResourceUtils.getURL("classpath:") 可能会指向jar包内部，
			// 导致无法在外部写入。
			// 因此，对于生产环境，强烈建议使用外部配置的绝对路径。
			File path = new File(ResourceUtils.getURL("classpath:static").getPath());
			if (!path.exists()) {
				path = new File(""); // 如果classpath:static不存在，则使用当前项目目录
			}
			File upload = new File(path.getAbsolutePath(), "/upload/");
			if (!upload.exists()) {
				upload.mkdirs(); // 创建upload目录
			}
			this.uploadFolder = upload.getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// Fallback to a default or throw an error if path cannot be resolved
			this.uploadFolder = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "upload";
			System.err.println("WARN: 无法解析classpath路径，使用默认 fallback 路径: " + this.uploadFolder);
			File fallbackDir = new File(this.uploadFolder);
			if (!fallbackDir.exists()) {
				fallbackDir.mkdirs();
			}
		}
	}


	/**
	 * 文件上传接口 (更通用的上传，可以处理图片、PDF等)
	 * POST /springbootn3op2l20/file/upload
	 * 请求方式改为 @PostMapping 更符合 RESTful 规范
	 */
	@PostMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file, @RequestParam(value = "type", required = false) String type) {
		if (file.isEmpty()) {
			return R.error("上传文件不能为空");
		}

		try {
			// 获取原始文件名和后缀
			String originalFilename = file.getOriginalFilename();
			String suffix = "";
			if (originalFilename != null && originalFilename.contains(".")) {
				suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
			}

			// 生成新的唯一文件名，防止重名覆盖
			String uuid = UUID.randomUUID().toString().replace("-", "");
			String newFileName = uuid + suffix; // 例如：a1b2c3d4.pdf

			Path targetPath = Paths.get(uploadFolder, newFileName);

			// 保存文件到指定目录
			Files.copy(file.getInputStream(), targetPath);

			// 如果有你原有的ConfigEntity逻辑，可以保留
			if(StringUtils.isNotBlank(type) && type.equals("1")) {
				ConfigEntity configEntity = configService.selectOne(new EntityWrapper<ConfigEntity>().eq("name", "faceFile"));
				if(configEntity==null) {
					configEntity = new ConfigEntity();
					configEntity.setName("faceFile");
					configEntity.setValue(newFileName);
				} else {
					configEntity.setValue(newFileName);
				}
				configService.insertOrUpdate(configEntity);
			}

			// 返回文件的相对路径，前端可以直接用这个路径拼接baseUrl来访问或下载
			// 例如：upload/a1b2c3d4.pdf
			String relativePath = "upload" + File.separator + newFileName; // 注意：这里返回的是相对路径

			return R.ok().put("file", relativePath); // 返回文件的相对路径
		} catch (IOException e) {
			e.printStackTrace();
			return R.error("文件上传失败: " + e.getMessage());
		} catch (Exception e) { // 捕获其他可能的异常
			e.printStackTrace();
			return R.error("文件处理异常: " + e.getMessage());
		}
	}

	/**
	 * 下载文件接口
	 * GET /springbootn3op2l20/file/download?fileName=xxx.pdf
	 * 请求方式改为 @GetMapping 更符合 RESTful 规范
	 */
	@IgnoreAuth
	@GetMapping("/download")
	public ResponseEntity<byte[]> download(@RequestParam String fileName) {
		try {
			// 确保文件路径是正确的
			File file = new File(uploadFolder + File.separator + fileName);

			if (!file.exists() || !file.isFile()) {
				// 如果文件不存在或不是一个文件，返回404
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			HttpHeaders headers = new HttpHeaders();

			// 设置 Content-Type：根据文件实际类型，可以确保浏览器正确处理（如PDF直接预览）
			// 如果无法探测，默认为 APPLICATION_OCTET_STREAM
			String contentType = Files.probeContentType(file.toPath());
			if (contentType == null) {
				contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
			}
			headers.setContentType(MediaType.parseMediaType(contentType));

			// 设置 Content-Disposition 为 attachment，强制浏览器下载，并设置下载文件名
			headers.setContentDispositionFormData("attachment", file.getName()); // 使用 file.getName() 获取实际文件名

			// 返回文件内容
			return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK); // 将CREATED改为OK更符合下载场景
		} catch (IOException e) {
			e.printStackTrace();
			// 文件读取或设置响应头时发生异常，返回500
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
