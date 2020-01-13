package com.jd.apocal.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.apocal.model.entity.ModelSet;
import com.jd.apocal.model.mapper.ModelSetMapper;
import com.jd.apocal.model.service.ModelSetService;
import org.springframework.stereotype.Service;

@Service
public class ModelSetServiceImpl extends ServiceImpl<ModelSetMapper, ModelSet> implements
    ModelSetService {

}
