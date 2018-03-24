#!/bin/sh
#############################################
## Global variables
#############################################
if [ -x /usr/bin/tput ]; then
  red=`tput setaf 1` # error
  green=`tput setaf 2` # nice
  yellow=`tput setaf 3` # warning
  blue=`tput setaf 4` # info
  purple=`tput setaf 5` # command
  teal=`tput setaf 6` # detail
  white=`tput setaf 7` #
  reset=`tput sgr0`
fi

dir="$(cd "$(dirname "$0")"; pwd -P)"

PROJECT_PATH=

#############################################
## Functions
#############################################

usage() {
  echo "${reset} Usage: $0 [options] [file1.java file2.java ...] ${reset}"
  echo "${blue} Options:   ${reset}"
  echo "${blue}          -p, --project-path =path       specifying code-style e.g. /wks/src/git-hooks-code-formatter ${reset}"
  echo "${blue}          -h, --help                     help ${reset}"
  exit 1
}

#############################################
## Check arguments
#############################################

if [[ $# -ne 1 ]]; then
    echo "${red} Error : invalid paramter. A project path is required to install the pre-commit script!" >&2
    echo
    usage
    exit 1
fi

for i in "$@"
  do
    case $i in
      -p=*|--project-path=*)  PROJECT_PATH="${i#*=}" ; shift 1;;
      -h|--help)              usage            ;;
      *)                      usage            ;;
    esac
done

if [[ $PROJECT_PATH == "" ]]; then
    echo "${red} Error : PROJECT_PATH can't be blank !" >&2
    exit 1
fi

destProjectDir=$PROJECT_PATH


#############################################
## Run
#############################################

cd $dir/tools/eclipse-java-formatter/
  mvn clean install
cd -

echo "${blue} remove existing version of code-formatter ${reset}"
echo rm -fr $destProjectDir/.git/hooks/tools
echo rm -fr $destProjectDir/.git/hooks/autoformat

if [ ! -z $destProjectDir/.git/hooks/tools ]; then
  mkdir -p $destProjectDir/.git/hooks/tools
fi

if [ ! -z $destProjectDir/.git/hooks/autoformat ]; then
  mkdir -p $destProjectDir/.git/hooks/autoformat
fi

echo "${blue} copying new version of code-formatter to destination project dir"
cp -R $dir/autoformat/* $destProjectDir/.git/hooks/autoformat
cp -R $dir/tools/* $destProjectDir/.git/hooks/tools
cp $dir/pre-commit $dir/.git/hooks

echo chmod a+x $dir/.git/hooks/tools/eclipse-java-formatter/ejf
echo chmod a+x $dir/.git/hooks/pre-commit
