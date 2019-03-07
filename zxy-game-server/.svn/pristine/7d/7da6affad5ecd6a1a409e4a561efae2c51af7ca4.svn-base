#!/bin/bash
# Author:Frank
# CreateTime:2014-01-13
# Description:重命名OSS文件名

Date=$(date +%Y%m%d_%H%M%S)

fileList=`ls oss/ |grep .log$`
iCounter=1
for file in $fileList
do
	iCounter=`expr $iCounter + 1`
done

nextNum=0
iFile=0
for file in $fileList
do
        FileName=`basename $file`
        nextNum=`expr $nextNum + 1`
        if [ $nextNum -eq $iCounter ]; then
                echo "Skip lastest!" > /dev/null
        else
                mv oss/$FileName oss/$FileName.$Date
                if [ $? -eq 0 ]; then
                        iFile=`expr $iFile + 1`
                fi
    fi
done