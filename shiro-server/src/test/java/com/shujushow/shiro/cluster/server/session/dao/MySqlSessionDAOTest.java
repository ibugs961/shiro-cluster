package com.shujushow.shiro.cluster.server.session.dao;

import com.shujushow.shiro.cluster.server.JunitConfig;
import org.apache.shiro.session.Session;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Created by xinshu on 2016/5/26.
 */
public class MySqlSessionDAOTest extends JunitConfig{

    @Autowired
    MySqlSessionDAO mySqlSessionDAO;

    @Test
    public void testDoReadSession() throws Exception {
        Serializable sessionId = "73151be0-6b49-4e00-8ea4-6ef7b68a5396";
        Session session = mySqlSessionDAO.doReadSession(sessionId);

        Assert.assertNotNull(session);
    }
}