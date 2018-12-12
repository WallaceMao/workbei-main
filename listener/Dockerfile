#FROM registry-vpc.cn-hangzhou.aliyuncs.com/workbei-public/tomcat:9.0.13-jre8
FROM registry.cn-hangzhou.aliyuncs.com/workbei-public/tomcat:9.0.13-jre8

LABEL name="workbei-main" \
       description="workbei main api" \
       maintainer="Wallace Mao" \
       org="workbei"

# set default time zone to +0800 (Shanghai)
ENV TIME_ZONE=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo $TIME_ZONE > /etc/timezone

ENV CATALINA_HOME /usr/local/tomcat
WORKDIR $CATALINA_HOME

ADD target/workbei-main-listener-*.war webapps/api.war

CMD ["catalina.sh", "run"]