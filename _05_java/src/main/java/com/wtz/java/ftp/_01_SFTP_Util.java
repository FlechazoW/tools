package com.wtz.java.ftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class _01_SFTP_Util {

    private final static Logger log = LoggerFactory.getLogger(_01_SFTP_Util.class);

    /**
     * SFTP
     */
    public static final String SFTP = "sftp";
    /**
     * 通道
     */
    private ChannelSftp channel;
    /**
     * session
     */
    private Session session;

    /**
     * 规避多线程并发不断开问题
     */
    private static final ThreadLocal<_01_SFTP_Util> sftpLocal = new ThreadLocal<_01_SFTP_Util>();

    /**
     * 获取sftpchannel
     *
     * @param connectConfig 连接配置
     * @return
     * @throws Exception
     */
    private void init(_01_SFTP_ConnectConfig connectConfig) throws Exception {
        String host = connectConfig.getHost();
        int port = connectConfig.getPort();

        String userName = connectConfig.getUsername();

        //创建JSch对象
        JSch jsch = new JSch();

        //添加私钥(信任登录方式)
        if (StringUtils.isNotBlank(connectConfig.getPrivateKey())) {
            jsch.addIdentity(connectConfig.getPrivateKey());
        }

        session = jsch.getSession(userName, host, port);
        if (log.isInfoEnabled()) {
            log.info(" JSCH Session created,sftpHost = {}, sftpUserName={}", host, userName);
        }

        //设置密码
        if (StringUtils.isNotBlank(connectConfig.getPassword())) {
            session.setPassword(connectConfig.getPassword());
        }

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        //设置超时
        session.setTimeout(connectConfig.getTimeout());
        //建立连接
        session.connect();
        if (log.isInfoEnabled()) {
            log.info("JSCH Session connected.sftpHost = {}, sftpUserName={}", host, userName);
        }

        //打开SFTP通道
        channel = (ChannelSftp) session.openChannel(SFTP);
        //建立SFTP通道的连接
        channel.connect();
        if (log.isInfoEnabled()) {
            log.info("Connected successfully to sftpHost = {}, sftpUserName={}", host, userName);
        }
    }

    /**
     * 是否已连接
     *
     * @return
     */
    private boolean isConnected() {
        return null != channel && channel.isConnected();
    }

    /**
     * 获取本地线程存储的sftp客户端
     *
     * @return
     * @throws Exception
     */
    public static _01_SFTP_Util getSftpUtil(_01_SFTP_ConnectConfig connectConfig) throws Exception {
        _01_SFTP_Util sftpUtil = sftpLocal.get();
        if (null == sftpUtil || !sftpUtil.isConnected()) {
            sftpLocal.set(new _01_SFTP_Util(connectConfig));
        }
        return sftpLocal.get();
    }

    /**
     * 释放本地线程存储的sftp客户端
     */
    public static void release() {
        if (null != sftpLocal.get()) {
            sftpLocal.get().closeChannel();
            sftpLocal.set(null);
        }
    }

    /**
     * 构造函数
     * <p>
     * 非线程安全，故权限为私有
     * </p>
     *
     * @throws Exception
     */
    private _01_SFTP_Util(_01_SFTP_ConnectConfig connectConfig) throws Exception {
        super();
        init(connectConfig);
    }

    /**
     * 关闭通道
     *
     * @throws Exception
     */
    public void closeChannel() {
        if (null != channel) {
            try {
                channel.disconnect();
            } catch (Exception e) {
                log.error("关闭SFTP通道发生异常:", e);
            }
        }
        if (null != session) {
            try {
                session.disconnect();
            } catch (Exception e) {
                log.error("SFTP关闭 session异常:", e);
            }
        }
    }

    /**
     * 下载文件
     *
     * @param downDir 下载目录
     * @param src     源文件
     * @param dst     保存后的文件名称或目录
     * @throws Exception
     */
    public void downFile(String downDir, String src, String dst) throws Exception {
        channel.cd(downDir);
        channel.get(src, dst);
    }

    /**
     * 删除文件
     *
     * @param filePath 文件全路径
     * @throws SftpException
     */
    public void deleteFile(String filePath) throws SftpException {
        channel.rm(filePath);
    }

    @SuppressWarnings("unchecked")
    public List<String> listFiles(String dir) throws SftpException {
        Vector<ChannelSftp.LsEntry> files = channel.ls(dir);
        if (null != files) {
            List<String> fileNames = new ArrayList<String>();
            Iterator<ChannelSftp.LsEntry> iter = files.iterator();
            while (iter.hasNext()) {
                String fileName = iter.next().getFilename();
                if (StringUtils.equals(".", fileName) || StringUtils.equals("..", fileName)) {
                    continue;
                }
                fileNames.add(fileName);
            }
            return fileNames;
        }
        return null;
    }
}
