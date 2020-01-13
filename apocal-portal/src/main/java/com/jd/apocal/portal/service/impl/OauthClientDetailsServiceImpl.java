package com.jd.apocal.portal.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.apocal.portal.mapper.OauthClientDetailsMapper;
import com.jd.apocal.portal.model.entity.OauthClientDetails;
import com.jd.apocal.portal.service.OauthClientDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class OauthClientDetailsServiceImpl extends
    ServiceImpl<OauthClientDetailsMapper, OauthClientDetails> implements OauthClientDetailsService {

  private static final PasswordEncoder ENCODER = PasswordEncoderFactories
      .createDelegatingPasswordEncoder();

  /**
   * <p>
   * 修改client
   * </p>
   *
   * @param clientDetails 实体对象
   */
  @Override
  public boolean saveOrUpdate(OauthClientDetails clientDetails) {
    clientDetails.setClientSecret(ENCODER.encode(clientDetails.getClientSecret()));
    return super.saveOrUpdate(clientDetails);
  }


  /**
   * <p>
   * 插入client
   * </p>
   *
   * @param clientDetails 实体对象
   */
  @Override
  public boolean save(OauthClientDetails clientDetails) {
    clientDetails.setClientSecret(ENCODER.encode(clientDetails.getClientSecret()));
    return super.save(clientDetails);
  }

}
