#!/bin/sh

# Shutdown application.
# -----------------------------------------------------------------------------

#替换filename为需要启动的jar名称
filename=custom-demo.jar

#检查进程是否已经存在
count=`ps -ef|grep $filename|grep -v grep|wc -l`
if [ $count -eq 0 ]
then
        echo "*** [$filename] Not Runing... ***"
else
        echo "*** Shutting down [$filename]... ***"
        #停止命令
	pid=`ps -ef|grep $filename|grep -v grep| awk '{print $2}'`
	echo $pid
	kill -9 $pid
        echo "*** [$filename] Shutdown Success! ***"
fi

