package cn.qbs.wa.teach.course.api.pojo.DTO.component;

import lombok.Data;

/**
 * @author yjx
 */
@Data
public class CourseComponentDTO {

    private Long lessonId;

    private Long componentId;

    private String componentName;

    private String componentTypeCode;

    private Integer sort;

    private Long resourceFileId;

    private String resourceFilePath;

    private String resourceFileName;

    private String resourceFileType;

    private Long resourceFileSize;

    private Integer resourceFileDuration;
}
