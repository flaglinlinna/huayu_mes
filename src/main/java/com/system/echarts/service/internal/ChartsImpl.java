package com.system.echarts.service.internal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.system.echarts.service.ChartsService;


@Service(value = "chartsService")
@Transactional(propagation = Propagation.REQUIRED)
public class ChartsImpl implements ChartsService {

}
