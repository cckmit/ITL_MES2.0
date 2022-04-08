#!/bin/bash
echo $CI_PROJECT_PATH_SLUG
#编译后将包拷贝至新建的publish/pcdist目录下，当前目录就是代码根目录。
#因为pc与mobile会部署到同一套机器，部署时需要先将包拷贝到部署机器的/tmp下，未免冲突，所以这里多建了一层目录，mobile对应也有mobiledist目录
rm -rf package-lock.json
npm install --registry=http://10.129.32.7:4873/ ||  exit 1
   npm run build || exit 1 && \
   mkdir -p publish/pcdist && \
   cp -r dist/*  publish/pcdist/ || exit 1
echo "build sucessful!"
