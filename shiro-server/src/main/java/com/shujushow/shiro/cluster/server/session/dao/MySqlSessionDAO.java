package com.shujushow.shiro.cluster.server.session.dao;

import com.shujushow.shiro.cluster.server.utils.SerializableUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xinshu on 2016/5/26.
 */
public class MySqlSessionDAO extends CachingSessionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        String sql = "insert into sessions(id, session) values(?,?)";
        jdbcTemplate.update(sql, sessionId, SerializableUtil.serialize(session));
        return session.getId();
    }


    @Override
    protected void doUpdate(Session session) {
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }
        String sql = "update sessions set session=? where id=?";
        jdbcTemplate.update(sql, SerializableUtil.serialize(session), session.getId());

    }

    @Override
    protected void doDelete(Session session) {
        String sql = "delete from sessions where id=?";
        jdbcTemplate.update(sql, session.getId());
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String sql = "select session from sessions where id=?";
        List<String> sessionStrList = jdbcTemplate.queryForList(sql, String.class, sessionId);
        if (sessionStrList.size() == 0) {
            return null;
        }
        return SerializableUtil.deserialize(sessionStrList.get(0));
    }
}
