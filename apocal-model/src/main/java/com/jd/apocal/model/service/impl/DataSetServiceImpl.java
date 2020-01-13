package com.jd.apocal.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.apocal.model.entity.DataSet;
import com.jd.apocal.model.mapper.DataSetMapper;
import com.jd.apocal.model.service.DataSetService;
import org.springframework.stereotype.Service;


@Service
public class DataSetServiceImpl extends ServiceImpl<DataSetMapper, DataSet> implements
    DataSetService {

}
