
const filterType = (uploadDocType) => {
    let newUploadDocType = {
      tooltip: '',
      Regular: ''
    };
    switch(uploadDocType) {
      case 'video':
        newUploadDocType.tooltip = '支持上传mp4、flv、ogg、webm格式，单次上传视频大小不能超过5G';
        newUploadDocType.tooltips = '支持上传mp4、flv、ogg、webm格式，单次上传视频不超过100个且大小不能超过5G';
        newUploadDocType.Regular = [ 'video/mpeg4', 'audio/mp4', 'video/webm', 'video/mp4', 'audio/ogg', 'video/x-flv', 'application/octet-stream', 'video/ogg'];
          break;
      case 'audio':
        newUploadDocType.tooltip = '支持mp3、WMA、WAV、AAC，单次上传音频大小不能超过5G';
        newUploadDocType.tooltips = '支持mp3、WMA、WAV、AAC，单次上传音频不超过100个且大小不能超过5G';
        newUploadDocType.Regular = [ 'audio/mpeg', 'video/x-ms-wma','audio/x-ms-wma', 'audio/x-wav', 'audio/x-aac' ];
          break;
      case 'picture':
        newUploadDocType.tooltip = '支持png、jpg、gif、svg，单次上传图片大小不能超过5G';
        newUploadDocType.tooltips = '支持png、jpg、gif、svg，单次上传图片不超过100个且大小不能超过5G';
        newUploadDocType.Regular = [ 'image/png', 'application/x-png', 'application/x-jpg', 'image/jpeg', 'image/gif', 'text/xml' ];
          break;
      case 'doc':
        newUploadDocType.tooltip = '支持word、ppt、excel、pdf、txt，单次上传文档大小不能超过5G';
        newUploadDocType.tooltips = '支持word、ppt、excel、pdf、txt，单次上传图片不超过100个且大小不能超过5G';
        newUploadDocType.Regular = [ 'text/plain', 'application/vnd.openxmlformats-officedocument.presentationml.presentation', 'application/msword', 'application/msword', 'application/x-ppt', 'application/vnd.ms-powerpoint', 'application/vnd.ms-excel', 'application/pdf', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' ];
          break;
      // case 'other':
      //   newUploadDocType.tooltip = '支持word、ppt、excel、pdf、png、jpg、gif、svg、mp3、WMA、WAV、AAC、mp4、rm、rmvb、flv、单次上传大小不能超过5G';
      //   newUploadDocType.Regular = ['video/mpeg4', 'audio/mp4', 'video/mp4', 'video/x-flv', 'audio/mpeg', 'audio/x-ms-wma', 'audio/x-ms-wma', 'audio/x-wav', 'audio/x-aac', 'image/png', 'application/x-png', 'application/x-jpg', 'image/jpeg', 'image/gif', 'text/xml','application/msword', 'application/msword', 'application/x-ppt', 'application/vnd.ms-powerpoint', 'application/vnd.ms-excel', 'application/pdf', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' ];
      //     break;
    }
    return newUploadDocType
  }
  

export default { filterType }