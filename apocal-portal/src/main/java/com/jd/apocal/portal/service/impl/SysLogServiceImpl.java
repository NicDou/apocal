package com.jd.apocal.portal.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.apocal.portal.mapper.SysLogMapper;
import com.jd.apocal.portal.model.entity.SysLog;
import com.jd.apocal.portal.service.SysLogService;
import com.jd.common.constants.CommonConstant;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

  @Override
  public Boolean updateByLogId(Long id) {
    Assert.isNull(id, "日志ID为空");

    SysLog sysLog = new SysLog();
    sysLog.setId(id);
    sysLog.setDelFlag(CommonConstant.STATUS_DEL);
    sysLog.setUpdateTime(new Date());
    return updateById(sysLog);
  }
}
