package cn.qbs.wa.teach.course.live.api.factory;

import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.live.api.RemoteLiveService;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.live.LiveListDTO;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.live.LivePageDTO;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.live.LiveResultDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteLiveFallbackFactory implements FallbackFactory<RemoteLiveService> {

    @Override
    public RemoteLiveService create(Throwable cause) {
      return new RemoteLiveService() {

          @Override
          public R<PageResultComDTO<LiveResultDTO>> page(LivePageDTO request) {
              return null;
          }

          @Override
          public R<List<LiveResultDTO>> list(LiveListDTO request) {
              return null;
          }

          @Override
          public R<PageResultComDTO<LiveResultDTO>> preparePage(LivePageDTO request) {
              return null;
          }

          @Override
          public R<PageResultComDTO<LiveResultDTO>> preparePageV2(LivePageDTO request) {
              return null;
          }

          @Override
          public R<List<LiveResultDTO>> shopGetList(LiveListDTO request) {
              return null;
          }
      };
    }
}
