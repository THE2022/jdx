FROM yiidii/nginx-jre8:v1
MAINTAINER yd <3087233411@qq.com>

# 后端
ADD ./back/target/jdx.jar /app/app.jar
# 前端
ADD ./nginx.conf /etc/nginx/nginx.conf
ADD ./front/dist/ /usr/share/nginx/html/
# RUN tar xzvf /usr/share/nginx/html/dist.tar.gz -C /usr/share/nginx/html/

RUN echo "nohup nginx &" >> /opt/entrypoint.sh
RUN echo "java -jar /app/app.jar" >> /opt/entrypoint.sh

ENTRYPOINT ["sh", "/opt/entrypoint.sh"]
	