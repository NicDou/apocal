package com.jd.apocal.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.apocal.portal.mapper.SysZuulRouteMapper;
import com.jd.apocal.portal.model.entity.SysZuulRoute;
import com.jd.apocal.portal.service.SysZuulRouteService;
import com.jd.common.constants.CommonConstant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SysZuulRouteServiceImpl extends
    ServiceImpl<SysZuulRouteMapper, SysZuulRoute> implements SysZuulRouteService {

  @Autowired
  private RedisTemplate redisTemplate;
//    @Autowired
//    private RabbitTemplate rabbitTemplate;

  /**
   * 立即生效配置
   *
   * @return
   */
  @Override
  public Boolean applyZuulRoute() {
    QueryWrapper wrapper = new QueryWrapper();
    wrapper.eq(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
    List<SysZuulRoute> routeList = this.list(wrapper);
    redisTemplate.opsForValue().set(CommonConstant.ROUTE_KEY, routeList);
    //rabbitTemplate.convertAndSend(MqQueueConstant.ROUTE_CONFIG_CHANGE, 1);
    return Boolean.TRUE;
  }
}
