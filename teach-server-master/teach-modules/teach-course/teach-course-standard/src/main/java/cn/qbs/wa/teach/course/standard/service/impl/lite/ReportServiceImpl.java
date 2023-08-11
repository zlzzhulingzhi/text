package cn.qbs.wa.teach.course.standard.service.impl.lite;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.oss.utils.TencentCosUtil;
import cn.qbs.wa.teach.common.weixin.miniapp.service.MiniAppCodeService;
import cn.qbs.wa.teach.course.standard.entity.TWxReport;
import cn.qbs.wa.teach.course.standard.enums.HeadImageUrlEnum;
import cn.qbs.wa.teach.course.standard.enums.ImageSourceType;
import cn.qbs.wa.teach.course.standard.enums.QrCodeType;
import cn.qbs.wa.teach.course.standard.mapper.CourseMapper;
import cn.qbs.wa.teach.course.standard.pojo.course.CourseDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.report.CourseReportDto;
import cn.qbs.wa.teach.course.standard.pojo.report.ImageResourceDto;
import cn.qbs.wa.teach.course.standard.pojo.report.ReportQo;
import cn.qbs.wa.teach.course.standard.pojo.report.WxCodeRo;
import cn.qbs.wa.teach.course.standard.service.lite.ReportService;
import cn.qbs.wa.teach.course.standard.service.lite.TWxReportService;
import cn.qbs.wa.teach.course.standard.util.GraphicsUtil;
import cn.qbs.wa.teach.course.standard.util.ReportGenerateUtil;
import cn.qbs.wa.teach.organization.api.RemoteOrgService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.OrganizationInnerDetailResponseDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.SocialLoginInfoDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionMemberService;
import com.alibaba.fastjson.JSON;
import com.tencent.cloud.cos.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    private static String REPORT_LOGO_PATH = "report/image/logo.png";

    private static String REPORT_SHARE_LOGO_PATH = "report/image/shareLogo.png";

    @Resource
    private RemoteOrgService remoteOrgService;

    @Resource
    private MiniAppCodeService miniAppCodeService;

    @Resource
    private TencentCosUtil tencentCosUtil;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private RemoteUnionMemberService remoteUnionMemberService;

/*    @Resource
    private MicroPageService microPageService;*/

    @Resource
    private TWxReportService tWxReportService;

    @Override
    public WxCodeRo createReport(ReportQo qo) {

        WxCodeRo wxCodeRo=new WxCodeRo();
        QrCodeType qrCodeType = QrCodeType.getType(qo.getType());
        if(qrCodeType == null){
            new ServiceException("海报信息参数错误");
        }
        IdRequest idRequest=new IdRequest();
        idRequest.setId(qo.getMemberId());
        UniMemberDTO uniMemberDTO=remoteUnionMemberService.info(idRequest).getRemoteData();
        if (uniMemberDTO == null){
            new ServiceException("未查到学生信息");
        }else {
            if (uniMemberDTO.getRealName() == null){
                uniMemberDTO.setRealName(AesUtil.decode(uniMemberDTO.getPhone()));
            }
            if (StringUtils.isEmpty(uniMemberDTO.getHeadImgUrl())){
                uniMemberDTO.setHeadImgUrl(HeadImageUrlEnum.HEAD_IMAGE_URL_ENUM.getCode());
            }
        }
        CourseDetailResponse reportCourse = courseMapper.selectDetailById(qo.getId());
        idRequest.setId(reportCourse.getOrgId());
        OrganizationInnerDetailResponseDTO organizationInnerDetailResponseDTO=remoteOrgService.detail(idRequest).getRemoteData();
        String logo=organizationInnerDetailResponseDTO.getPcLogoUrl();
        try{
            String scene=qo.getScene();
            String md5="";
            TWxReport tWxReport=new TWxReport();
            if(!Integer.valueOf(QrCodeType.MICRO.getType()).equals(qo.getType())){
                tWxReport.setCourseId(qo.getId());
                String jsonStr=JSON.toJSONString(reportCourse)+"type"+qo.getType()+"nickName"+
                        uniMemberDTO.getRealName()+"headImageUrl"+uniMemberDTO.getHeadImgUrl()+"logo"+logo;
                md5=MD5.stringToMD5(jsonStr);
                tWxReport.setMd5(md5);
                tWxReport.setMemberId(qo.getMemberId());
                TWxReport wxReports=tWxReportService.page(tWxReport);
                if (wxReports != null){
                    wxCodeRo.setUrl(wxReports.getUrl());;
                    return wxCodeRo;
                }
                File miniAppCodeFile = this.miniAppCodeService.createMiniAppCode(qo.getPage(), scene);
                String qrCodeUrl = tencentCosUtil.putObject(miniAppCodeFile, "/train/wxQrcode/miniAppCode/");
                miniAppCodeFile.delete();
                BufferedImage image = null;
                image = ReportGenerateUtil.createCourseReport(getCourseReportData(qo.getId(),uniMemberDTO,qrCodeUrl, reportCourse.getOrgId()));
                if(image != null){
                    File file = File.createTempFile("temp", ".png" );
                    GraphicsUtil.write(image,file.getPath());
                    String url = tencentCosUtil.putObject(file, "/train/wxQrcode/miniAppCode/");
                    tWxReport.setOrgId(reportCourse.getOrgId());
                    tWxReport.setUrl(url);
                    tWxReportService.add(tWxReport);
                    file.deleteOnExit();
                    wxCodeRo.setUrl(url);
                    return wxCodeRo;
                }
            }else {
                /*MicroPageDetailResponse microPageDetailResponse = microPageService.detail(qo.getId());
                String jsonStr=JSON.toJSONString(microPageDetailResponse);
                md5=MD5.stringToMD5(jsonStr+"nickName"+ studentDTO.getNickName()+"headImageUrl"+studentDTO.getHeadImgUrl()+"logo"+logo);
                wxReport.setMd5(md5);
                wxReport.setMemberId(qo.getMemberId());
                WxReport wxReports=wxReportService.page(wxReport);
                if (wxReports != null){
                    wxCodeRo.setUrl(wxReports.getUrl());
                    return wxCodeRo;
                }
                File miniAppCodeFile = this.miniAppCodeService.createMiniAppCode(qo.getPage(), scene);
                String qrCodeUrl = tencentCosUtil.putObject(miniAppCodeFile, "/wxQrcode/miniAppCode");
                miniAppCodeFile.delete();
                BufferedImage image = null;
                image = ReportGenerateUtil.createMicroReport(getMicroReportData(qo.getId(),studentDTO,qrCodeUrl,qo.getShopId()));
                if(image != null){
                    File file = File.createTempFile("temp", ".png" );
                    GraphicsUtil.write(image,file.getPath());
                    String url = tencentCosUtil.putObject(file, "/wxQrcode/miniAppCode");
                    wxReport.setShopId(qo.getShopId());
                    wxReport.setUrl(url);
                    wxReportService.add(wxReport);
                    file.deleteOnExit();
                    wxCodeRo.setUrl(url);
                    return wxCodeRo;
                }*/return wxCodeRo;
            }
        }catch (Exception e){
            log.error("海报生成失败",e);
            if(StringUtils.isNotEmpty(e.getMessage())){
                throw new ServiceException(e.getMessage());
            }
        }

        throw new ServiceException("分享海报失败");
    }

    private CourseReportDto getCourseReportData(Long courseId, UniMemberDTO uniMemberDTO, String qrCodeUrl, Long orgId){
        IdRequest request=new IdRequest();
        request.setId(courseId);
        CourseDetailResponse reportCourse = courseMapper.selectDetailById(courseId);
        if (reportCourse==null){
            new ServiceException("未查到课程信息");
        }
        CourseReportDto dto = new CourseReportDto();
        dto.setCover(new ImageResourceDto(ImageSourceType.URL,reportCourse.getCoverUrl()));
        dto.setAvatar(new ImageResourceDto(ImageSourceType.URL,uniMemberDTO.getHeadImgUrl()));
        dto.setQrCode(new ImageResourceDto(ImageSourceType.URL,qrCodeUrl));
        IdRequest idRequest=new IdRequest();
        idRequest.setId(orgId);
        OrganizationInnerDetailResponseDTO organizationInnerDetailResponseDTO=remoteOrgService.detail(idRequest).getRemoteData();
        if(organizationInnerDetailResponseDTO.getPcLogoUrl()!=null && organizationInnerDetailResponseDTO.getPcLogoUrl()!=""){
            dto.setLogo(new ImageResourceDto(ImageSourceType.URL,organizationInnerDetailResponseDTO.getPcLogoUrl()));
        }else {
            dto.setLogo(new ImageResourceDto(ImageSourceType.INPUT,REPORT_LOGO_PATH));
        }
        dto.setShareLogo(new ImageResourceDto(ImageSourceType.INPUT,REPORT_LOGO_PATH));

        dto.setNickName(uniMemberDTO.getRealName());
        dto.setCourseName(reportCourse.getCourseName());
        //dto.setGoodsPrice(Constants.DB_FAIL.toString());
        return dto;
    }

    /*private MicroReportDto getMicroReportData(Long microId, StudentDTO studentDTO, String qrCodeUrl,Long shopId)  {

        MicroPageDetailResponse microPageDetailResponse = microPageService.detail(microId);

        MicroReportDto dto = new MicroReportDto();
        if(StringUtils.isNotEmpty(microPageDetailResponse.getShareCoverUrl()) && microPageDetailResponse.getShareCoverUrl().contains("bg_src")){
            ShareCoverDto shareCover = JSONObject.parseObject(microPageDetailResponse.getShareCoverUrl(),ShareCoverDto.class);
            ReportUnitDto.ImageUnitDto avatar = new ReportUnitDto.ImageUnitDto();
            avatar.setResource(new ImageResourceDto(ImageSourceType.URL,studentDTO.getHeadImgUrl()));
            avatar.setX(shareCover.getHeadX());
            avatar.setY(shareCover.getHeadY());

            ReportUnitDto.ImageUnitDto qrCode = new ReportUnitDto.ImageUnitDto();
            qrCode.setResource(new ImageResourceDto(ImageSourceType.URL,qrCodeUrl));
            qrCode.setX(shareCover.getQrX());
            qrCode.setY(shareCover.getQrY());

            ReportUnitDto.TextUnitDto nickName = new ReportUnitDto.TextUnitDto();
            nickName.setText(studentDTO.getNickName());
            nickName.setX(shareCover.getNameX());
            nickName.setY(shareCover.getNameY());

            dto.setCover(new ImageResourceDto(ImageSourceType.URL,shareCover.getBgSrc()));
            dto.setAvatar(avatar);
            dto.setQrCode(qrCode);
            dto.setNickName(nickName);
            dto.setShared(true);
        }else{
            if(StringUtils.isEmpty(microPageDetailResponse.getCoverUrl())){
                throw new ServiceException("海报封面不存在");
            }

            ReportUnitDto.ImageUnitDto avatar = new ReportUnitDto.ImageUnitDto();
            avatar.setResource(new ImageResourceDto(ImageSourceType.URL,studentDTO.getHeadImgUrl()));

            ReportUnitDto.ImageUnitDto qrCode = new ReportUnitDto.ImageUnitDto();
            qrCode.setResource(new ImageResourceDto(ImageSourceType.URL,qrCodeUrl));

            ReportUnitDto.TextUnitDto nickName = new ReportUnitDto.TextUnitDto();
            nickName.setText(studentDTO.getNickName());

            dto.setCover(new ImageResourceDto(ImageSourceType.URL, microPageDetailResponse.getCoverUrl()));

            ReportUnitDto.ImageUnitDto logo = new ReportUnitDto.ImageUnitDto();
            List<Long> shopIdList=new ArrayList<>();
            shopIdList.add(shopId);
            ShopListRequestDTO shopListRequestDTO=new ShopListRequestDTO();
            shopListRequestDTO.setShopIdList(shopIdList);
            List<ShopListResultDTO> shopListResultDTOS=remoteShopService.list(shopListRequestDTO).getData();
            if (shopListResultDTOS.get(Constants.DB_FAIL).getPcLogoUrl()!=null && shopListResultDTOS.get(Constants.DB_FAIL).getPcLogoUrl()!=""){
                logo.setResource(new ImageResourceDto(ImageSourceType.URL,shopListResultDTOS.get(Constants.DB_FAIL).getPcLogoUrl()));
            }else {
                logo.setResource(new ImageResourceDto(ImageSourceType.INPUT,REPORT_SHARE_LOGO_PATH));
            }

            dto.setAvatar(avatar);
            dto.setQrCode(qrCode);
            dto.setNickName(nickName);
            dto.setMicroName(microPageDetailResponse.getName());
            dto.setLogo(logo);
        }

        return dto;
    }*/
}
