#!/bin/bash
##根据环境区分部署的IP数组值。
if [[ $env == 'test' && $CI_BUILD_REF_NAME == 'develop' ]]; then
   argv=(192.168.1.1 192.168.1.2)
elif [[ $env == 'prod' ]] && [[ $CI_BUILD_REF_NAME == 'master' || $CI_BUILD_REF_NAME =~ [v|V][0-9]+\.[0-9]+\.[0-9]+$ ]]; then
   argv=()
else
   echo 'Please enter environment parameters test or prod !'
   exit 1
fi
#遍历IP并部署：部署先将gitlab打的包放到机器的/tmp目录下，然后登陆目标机器将包放到部署目录，并删除/tmp下内容
for i in "${argv[@]}";do
    scp -r -o stricthostkeychecking=no publish/* cifiadmin@$i:/tmp/ && \
    ssh -o PasswordAuthentication=no -o StrictHostKeyChecking=no cifiadmin@$i "
    cd /app/www/pc ;
    rm -rf ./* ;
    cp -r /tmp/pcdist/* ./ ;
    ls -al ;
    rm -rf /tmp/pcdist/* ;
    exit 0 ;" || exit 1
done
echo 'deploy successful!'
