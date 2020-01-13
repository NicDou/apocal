package com.jd.apocal.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.apocal.model.entity.Experiment;
import com.jd.apocal.model.entity.K8sJobStatus;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ExperimentService extends IService<Experiment> {

  int getCountByMap(Map<String, Object> map);

  K8sJobStatus getK8sJobStatusByJobName(String k8sJobName);

  List<Experiment> getExpsByGroupId(String groupId);

}
