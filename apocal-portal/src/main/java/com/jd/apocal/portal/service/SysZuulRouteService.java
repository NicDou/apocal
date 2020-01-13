package com.jd.apocal.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.apocal.portal.model.entity.SysZuulRoute;


public interface SysZuulRouteService extends IService<SysZuulRoute> {

  /**
   * 立即生效配置
   *
   * @return
   */
  Boolean applyZuulRoute();
}
