#!/bin/bash
echo '*****************************************************************'
echo '     publish_to_repository.sh                                 '
echo '                    by niuren.zhu                                 '
echo '                          2016.06.21                             '
echo '  说明：                                                          '
echo '    1. 首先确保compile_and_package.sh有效及可用。                 '
echo '    2. 配置apache-maven程序，找到conf/setting.xml。                 '
echo '    3. 在<servers>节点下添加（其中用户名与密码需要向管理员申请）          '
echo '            <server>                                             '
echo '              <id>nexus-3rd</id>                                 '
echo '              <username>用户名</username>                         '
echo '              <password>密码</password>                           '
echo '            </server>                                            '
echo '    4. 此脚本会调用打包脚本并上传打包结果到maven仓库。                   '
echo '*****************************************************************'

# *******设置参数变量*******
WORK_FOLDER=`pwd`
RELEASE_FOLDER=${WORK_FOLDER}/release
OPNAME=`date '+%Y%m%d_%H%M%S'`
LOGFILE=${WORK_FOLDER}/publish_to_repository_log_${OPNAME}.txt
GROUPID=club.ibcp
VERSION=1.0.0
PACKAGEING=jar
URL=http://ibas.club:8877/nexus/content/repositories/thirdparty/
REPOSITORYID=nexus-3rd

if [ ! -e ${WORK_FOLDER}/compile_and_package.sh ]
then
  echo --没有找到[compile_and_package.bat]
  exit 1
fi
cd ${WORK_FOLDER}/
./my_modules_package_jar.sh

echo --开始发布包到网址[${URL}]
for file in `ls -B $RELEASE_FOLDER`
do
  if [ -e ${RELEASE_FOLDER}/${file%.*}.jar ]
  then
    echo --发布[$file]
    mvn deploy:deploy-file -DgroupId=$GROUPID -DartifactId=${file%-*} -Dversion=$VERSION -Dpackaging=$PACKAGEING -Dfile=$RELEASE_FOLDER/${file} -Durl=$URL -DrepositoryId=$REPOSITORYID >>$LOGFILE
  fi
done

echo --发布完成，更多信息请查看[publish_to_repository_log_${OPNAME}.txt]
