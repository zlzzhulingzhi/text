package cn.qbs.wa.teach.common.live.service;

import cn.qbs.wa.teach.common.live.pojo.LiveAddressResponse;
import cn.qbs.wa.teach.common.live.pojo.LiveRequest;

public interface BaseLiveService {

    LiveAddressResponse getLiveAddress(LiveRequest request);
}
