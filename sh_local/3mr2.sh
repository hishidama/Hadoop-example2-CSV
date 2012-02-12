BASEDIR=$(cd $(dirname $0);pwd)
. common.rc

DST=$DIR/mr
echo $(date '+%Y-%m-%d %H:%M:%S') mr2 start >> $LOGFILE
hadoop fs -rmr $DST
hadoop jar $BASEDIR/../sh/hadoop2.jar example.hadoop2.mr.Main2 $SRC $DST $TOP
res=$?
echo $(date '+%Y-%m-%d %H:%M:%S') mr2 end $res >> $LOGFILE
