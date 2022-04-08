#!/bin/bash
#根据环境区分部署的IP数组值。环境区分是传入的env参数及选取的分支决定。
#替换argv值即可。
if [[ $env == 'test' && $CI_BUILD_REF_NAME == 'develop' ]]; then
   argv=(192.168.1.1 192.168.1.2)
elif [[ $env == 'prod' ]] && [[ $CI_BUILD_REF_NAME == 'master' || $CI_BUILD_REF_NAME =~ [v|V][0-9]+\.[0-9]+\.[0-9]+$ ]]; then
   argv=(生产IP数组)
else
   echo 'Please enter environment parameters test or prod !'
   exit 1
fi
echo ${argv[@]}
#遍历IP并部署：部署先将gitlab编译的包放到机器的/tmp目录下，然后登陆目标机器将包放到部署目录，并删除/tmp下内容。
for i in "${argv[@]}";do
    scp -o stricthostkeychecking=no $CI_PROJECT_DIR/publish/* cifiadmin@$i:/tmp/ && \
    ssh -o PasswordAuthentication=no -o StrictHostKeyChecking=no cifiadmin@$i "
    source /etc/profile ;
    ps aux |grep 'java'|grep '项目的jar或war包名'|grep -v 'grep' ;
    \cp /tmp/schedule-center-admin.jar /app/schedule/ ;
	/app/stop.sh || exit 1 ;
	/app/start.sh || exit 1 ;
    rm -f /tmp/schedule-center-admin.jar ; 
    ps aux |grep 'java'|grep '项目的jar或war包名'| grep -v 'grep' ;
    exit 0 ;" || exit 1
    echo "operate $i "
done
echo 'deploy successful!'
