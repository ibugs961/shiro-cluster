package com.shujushow.shiro.cluster.server.dao;

import com.shujushow.shiro.cluster.server.JunitConfig;
import com.shujushow.shiro.cluster.server.entity.App;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by xinshu on 2016/5/26.
 */
public class AppDaoImplTest extends JunitConfig {

    @Autowired AppDaoImpl appDao;

    @Test
    public void testFindAll() throws Exception {
        List<App> all = appDao.findAll();

        Assert.notEmpty(all);

    }
}