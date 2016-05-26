package com.shujushow.shiro.cluster.server.session.scheduler;

import com.shujushow.shiro.cluster.server.utils.SerializableUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.SystemPropertyUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xinshu on 2016/5/26.
 */
public class MySqlSessionValidationScheduler implements SessionValidationScheduler, Runnable{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MySqlSessionValidationScheduler.class);

    ValidatingSessionManager sessionManager;
    private ScheduledExecutorService service;
    private long interval = DefaultSessionManager.DEFAULT_SESSION_VALIDATION_INTERVAL;
    private boolean enabled = false;


    public MySqlSessionValidationScheduler(){
        super();
    }



    @Override
    public void run() {
        if(logger.isDebugEnabled()){
            logger.debug("Executing session validation..");
        }

        long startTme = System.currentTimeMillis();

        String sql = "select session from sessions limit ?,?";
        int start = 0;
        int size = 20;
        List<String> sessionList = jdbcTemplate.queryForList(sql, String.class, start, size);

        while(sessionList.size() > 0){
            for(String sessionStr: sessionList){
                try{
                    Session session = SerializableUtil.deserialize(sessionStr);
                    Method validateMethod= ReflectionUtils.findMethod(AbstractValidatingSessionManager.class, "validate", Session.class, SessionKey.class);
                    validateMethod.setAccessible(true);
                    ReflectionUtils.invokeMethod(validateMethod, sessionManager, session, new DefaultSessionKey(session.getId()));
                }catch(Exception exc){
                    // TODO
                }
            }

            start = start + size;
            sessionList = jdbcTemplate.queryForList(sql, String.class, start, size);
        }

        long stopTime = System.currentTimeMillis();
        if(logger.isDebugEnabled()){
            logger.debug("Session validation completed successfully in " + (stopTime- startTme) + " milliseconds");
        }
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void enableSessionValidation() {

        if(this.interval > 01){
            this.service = Executors.newSingleThreadScheduledExecutor(r->{
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return  thread;
            });

            this.service.scheduleAtFixedRate(this, interval, interval, TimeUnit.MILLISECONDS);
            this.enabled = true;
        }
    }

    @Override
    public void disableSessionValidation() {
        this.service.shutdown();;
        this.enabled = false;
    }

    public ValidatingSessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(ValidatingSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public ScheduledExecutorService getService() {
        return service;
    }

    public void setService(ScheduledExecutorService service) {
        this.service = service;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
