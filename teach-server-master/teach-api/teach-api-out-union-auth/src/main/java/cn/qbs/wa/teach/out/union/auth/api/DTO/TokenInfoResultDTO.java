package cn.qbs.wa.teach.out.union.auth.api.DTO;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/9 8:40
 */
@Data
public class TokenInfoResultDTO {

    private Long userId;

    private String userName;

    private String userKey;
}
