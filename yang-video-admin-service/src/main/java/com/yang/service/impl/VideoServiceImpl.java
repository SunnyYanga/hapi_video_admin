package com.yang.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yang.enums.BGMOperatorTypeEnum;
import com.yang.mapper.BgmMapper;
import com.yang.mapper.UsersReportMapperCustom;
import com.yang.mapper.VideosMapper;
import com.yang.pojo.Bgm;
import com.yang.pojo.BgmExample;
import com.yang.pojo.Videos;
import com.yang.pojo.vo.Reports;
import com.yang.service.VideoService;
import com.yang.util.ZKCurator;
import com.yang.utils.JsonUtils;
import com.yang.utils.PagedResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yg
 * @date 2020/8/17 16:11
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private BgmMapper bgmMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private ZKCurator zkCurator;

    @Autowired
    private UsersReportMapperCustom usersReportMapperCustom;

    @Autowired
    private VideosMapper videosMapper;

    @Override
    public void addBgm(Bgm bgm) {
        bgm.setId(sid.nextShort());
        bgmMapper.insert(bgm);
        Map<String, String> map = new HashMap<>();
        map.put("opertype", BGMOperatorTypeEnum.ADD.type);
        map.put("path", bgm.getPath());
        zkCurator.sendBgmOperator(bgm.getId(), JsonUtils.objectToJson(map));
    }

    @Override
    public PagedResult queryBgmList(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        BgmExample bgmExample = new BgmExample();
        List<Bgm> bgms = bgmMapper.selectByExample(bgmExample);

        PageInfo<Bgm> bgmPageInfo = new PageInfo<>(bgms);

        PagedResult result = new PagedResult();
        result.setTotal(bgmPageInfo.getPages());
        result.setRows(bgms);
        result.setPage(page);
        result.setRecords(result.getTotal());
        return result;
    }

    @Override
    public void deleteBgm(String bgmId) {
        Bgm bgm = bgmMapper.selectByPrimaryKey(bgmId);
        bgmMapper.deleteByPrimaryKey(bgmId);
        Map<String, String> map = new HashMap<>();
        map.put("opertype", BGMOperatorTypeEnum.DELETE.type);
        map.put("path", bgm.getPath());
        zkCurator.sendBgmOperator(bgmId, JsonUtils.objectToJson(map));
    }

    @Override
    public PagedResult queryReportList(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<Reports> reportsList = usersReportMapperCustom.selectAllVideoReport();

        PageInfo<Reports> pageList = new PageInfo<Reports>(reportsList);

        PagedResult grid = new PagedResult();
        grid.setTotal(pageList.getPages());
        grid.setRows(reportsList);
        grid.setPage(page);
        grid.setRecords(pageList.getTotal());

        return grid;

    }

    @Override
    public void updateVideoStatus(String videoId, Integer status) {
        Videos video = new Videos();
        video.setId(videoId);
        video.setStatus(status);
        videosMapper.updateByPrimaryKeySelective(video);
    }
}
