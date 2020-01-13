package com.jd.apocal.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.apocal.model.entity.ExpGroup;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpGroupMapper extends BaseMapper<ExpGroup> {

  int selectCountByMap(@Param("params") Map<String, Object> params);

}
