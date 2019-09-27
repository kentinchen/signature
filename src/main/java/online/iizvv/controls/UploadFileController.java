package online.iizvv.controls;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import online.iizvv.core.config.Config;
import online.iizvv.core.pojo.Result;
import online.iizvv.utils.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * @author ：iizvv
 * @date ：Created in 2019-09-03 17:00
 * @description：TODO
 * @version: 1.0
 */
@RestController
@Api(tags = {"文件上传"})
@RequestMapping("/file")
public class UploadFileController {

    @Autowired
    private FileManager fileManager;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "/uploadImage", notes = "更新预览图")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "图片文件"),
    })
    @PostMapping("/uploadImage")
    public Result uploadImage(MultipartFile file) {
        Result result = new Result();
        if (file.getContentType().equalsIgnoreCase("image/png") ||
                file.getContentType().equalsIgnoreCase("image/jpeg")) {
            log.info("开始上传文件: " + file.getOriginalFilename() +
                    ", 文件类型: " + file.getContentType());
            try {
                String imgName = IdUtil.simpleUUID();
                BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
                if (bufferedImage!=null){
                    Integer width = bufferedImage.getWidth();
                    Integer height = bufferedImage.getHeight();
                    imgName+="("+width+"A"+height+")";
                }
                imgName+=".jpeg";
                File excelFile = File.createTempFile(IdUtil.simpleUUID(), ".ipa");
                file.transferTo(excelFile);
                fileManager.uploadFile(excelFile, imgName, false);
                result.setCode(1);
                result.setMsg("图片上传成功");
                result.setData(Config.aliMainHost + "/" + imgName);
                FileUtil.del(excelFile);
            } catch (IOException e) {
                result.setMsg("图片上传失败");
                e.printStackTrace();
            }
        }
        return result;
    }

}
