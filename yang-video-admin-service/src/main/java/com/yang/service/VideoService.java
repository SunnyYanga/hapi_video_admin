package com.yang.service;

import com.yang.pojo.Bgm;
import com.yang.utils.PagedResult;

/**
 * @author yg
 * @date 2020/8/17 16:10
 */
public interface VideoService {

    void addBgm(Bgm bgm);

    PagedResult queryBgmList(Integer page, Integer pageSize);

    void deleteBgm(String bgmId);

    PagedResult queryReportList(Integer page, Integer pageSize);

    void updateVideoStatus(String videoId, Integer status);
}
