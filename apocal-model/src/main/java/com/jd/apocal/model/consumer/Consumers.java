package com.jd.apocal.model.consumer;

import com.jd.apocal.model.constant.Constant;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author DOU
 * @date 2019-09-16 14:34
 */

@Component
public class Consumers {

  @JmsListener(destination = Constant.MESSAGE_DESTINATION_IMAGE_BUILDER)
  public void buildImage() {

  }

  @JmsListener(destination = Constant.MESSAGE_DESTINATION_IMAGE_COPY)
  public void copyImage() {

  }

  @JmsListener(destination = Constant.MESSAGE_DESTINATION_IMAGE_DELETE)
  public void deleteImage() {

  }

  @JmsListener(destination = Constant.MESSAGE_DESTINATION_SERVICE_DEPLOYMENT)
  public void deployService() {

  }

}
