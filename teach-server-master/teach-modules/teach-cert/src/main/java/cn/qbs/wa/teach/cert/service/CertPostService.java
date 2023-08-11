package cn.qbs.wa.teach.cert.service;

import cn.qbs.wa.teach.common.post.drawable.Poster;

import java.util.List;
import java.util.Map;

public interface CertPostService {

    Map<Long, String> addCertPost(List<Poster> posterList);
}
