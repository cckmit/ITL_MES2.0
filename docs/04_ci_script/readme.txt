确保看懂readme，不懂的可以联系找SCM。
说明：
1. 按如下配置，将附件.gitlab-ci.yml原样放到代码根目录，按如下目录结构建目录，在对应目录下编写构建及部署脚本，提交到代码仓库即可调试。
2. 调试好后将.gitlab-ci.yml中注释的两行代码的“#”去掉。
3. 在对应测试或生产的应用服务器上跑附件脚本configkey.sh，用作部署时免密登录部署机器。
4. 脚本里的CI_PROJECT_PATH_SLUG及CI_BUILD_REF_NAME参数时项目的缺省环境参数名，不要变动，将参数值替换自己项目的参数值即可。
   举例：
   若项目仓库地址是CI_PROJECT_URL=http://gitlab.htah.com.cn/ark/backend/service
   则CI_PROJECT_PATH_SLUG=ark-backend-service。
   项目必须先自建develop分支，脚本里用变量CI_BUILD_REF_NAME可取到值“develop”。
   
   
步骤：
1.将附件配置文件.gitlab-ci.yml放到项目的代码根目录下，此文件定义构建及部署的pipelines流程。
2.创建CI目录及编写脚本。
  a.)在项目根目录下新建ci_script目录。
  b.)在ci_script目录下分别建“build-scripts”与“deploy-scripts”目录。
  c.)在“build-scripts”目录下写gobuild.sh作为构建索引脚本。索引脚本只需替换if else的判断参数值及具体调用脚本即可，其他地方不要动。
  d.)在“deploy-scripts”目录下写godeploy.sh作为构建索引脚本。索引脚本只需替换if else的判断参数值及具体调用脚本即可，其他地方不要动。
  e.)构建脚本替换自己项目的构建命令。
  f.)部署脚本替换argv的测试及生产环境的IP信息，及修改ssh登录到应用服务器的执行部署过程即可，如停服务、启动服务等。
     注意部署时都必须先将包拷贝到/tmp，然后登录机器将包cp到发布目录，再将/tmp下包删除。
3.按照模板编写脚本比较简单。模板只是建议，可以结合项目实际情况将参数剥离出来加以改造。  	