package com.jd.apocal.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.apocal.model.entity.Experiment;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperimentMapper extends BaseMapper<Experiment> {

  int selectCountByMap(@Param("params") Map<String, Object> params);

  List<Experiment> selectExpsByGroupId(@Param("groupId") String groupId);

}
