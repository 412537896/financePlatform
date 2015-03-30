package com.sunlights.op.service;

import com.sunlights.common.vo.PageVo;
import com.sunlights.op.vo.SettleAccountVo;

import java.util.List;

/**
 * <p>Project: operationplatform</p>
 * <p>Title: SettleAccountService.java</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2014 Sunlights.cc</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:jiaming.wang@sunlights.cc">wangJiaMing</a>
 */
public interface SettleAccountService {
    
    public List<SettleAccountVo> findSettleAccountVos(PageVo pageVo);
}
