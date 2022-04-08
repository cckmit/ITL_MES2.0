#!/bin/bash
echo $CI_PROJECT_PATH_SLUG
#编译后将包拷贝至新建的publish目录下，gitlab上可以在对应节点下载包。
#mvn clean deploy -Dmaven.test.skip=true || exit 1
mvn clean package -Dmaven.test.skip=true || exit 1 && \ 
    jarpath=$(find . -name "schedule-center-admin*.jar") && \
    mkdir -p publish && \
    mv $jarpath publish/schedule-center-admin.jar && \
    echo 'build successful!'
