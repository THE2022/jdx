FROM registry.cn-hangzhou.aliyuncs.com/yiidii-hub/nginx-openjdk:v1
MAINTAINER yd <3087233411@qq.com>
# envs
ENV JDX_DIR=/jdx

# 工作目录
WORKDIR ${JDX_DIR}

# 后端
ADD ./back/target/jdx.jar ${JDX_DIR}/app.jar
ADD ./back/docker/ ${JDX_DIR}/

# 前端
RUN rm /etc/nginx/conf.d/default.conf
ADD default.conf /etc/nginx/conf.d/
ADD ./front/dist/ /usr/share/nginx/html/

# 其他操作
RUN mkdir -p ${JDX_DIR}/logs/console

# 入口文件
ENTRYPOINT ["sh", "docker-entrypoint.sh"]
	