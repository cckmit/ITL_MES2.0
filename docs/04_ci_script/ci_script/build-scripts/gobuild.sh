#!/bin/bash
source /etc/profile
echo $CI_PROJECT_PATH_SLUG
#如果未在gitlab的pipiline传递环境参数env，则默认传递test，选取develop分支，这样会部署到测试环境
if [[ $env == '' ]]; then
export   env='test'
export   CI_BUILD_REF_NAME='develop'
fi
#以参数CI_PROJECT_PATH_SLUG判断具体使用哪个脚本部署
#若项目仓库地址是CI_PROJECT_URL=http://gitlab.htah.com.cn/ark/backend/service
#则CI_PROJECT_PATH_SLUG=ark-backend-service。
#项目必须先自建develop分支，脚本里用变量CI_BUILD_REF_NAME可取到值“develop”。
SCRIPTS_DIR=$(dirname "$0")
if [ "$CI_PROJECT_PATH_SLUG" == 'ark-frontend-mobile' ];then
   sh $SCRIPTS_DIR/mobile.sh || exit 1
elif [ "$CI_PROJECT_PATH_SLUG" == 'ark-frontend-pc' ];then
   sh $SCRIPTS_DIR/pc.sh || exit 1
elif [ "$CI_PROJECT_PATH_SLUG" == 'ark-backend-sc-server' ];then
   sh $SCRIPTS_DIR/sc-server.sh || exit 1
else
  echo "This project is not connected to an automated build!"
fi

#Publish the production package to the release server
pwd
if [[ $env == 'prod' ]] && [[ $CI_BUILD_REF_NAME == 'master' ]];then  
   dat=$(date +"%Y-%m-%d")
   releaseDir="/etc/gitlab-runner/release/${dat}/${CI_PROJECT_PATH}"
   mkdir -p ${releaseDir} || echo "Failed to publish directory copy "
   cp -r  publish/* ${releaseDir} || echo "Failed to publish directory copy "
   echo "Publish the production package to the release server Sucessful"
fi
