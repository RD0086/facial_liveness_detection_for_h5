#!/bin/sh

# Startup application.
# -----------------------------------------------------------------------------

#替换filename为需要启动的jar名称
filename=custom-demo.jar
#检查进程是否已经存在，避免重复启动
count=`ps -ef|grep $filename|grep -v grep|wc -l`
if [ $count -eq 0 ]
then
	echo "*** starting [$filename]... ***"
	#启动命令
	nohup java -jar $filename --spring.config.location=file:./application.yml > log.out 2>&1 &
	echo "*** [$filename] start success! ***" 
else
	echo "*** [$filename] already runing... ***"
fi

