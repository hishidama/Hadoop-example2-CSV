BASEDIR=$(cd $(dirname $0);pwd)
. common.rc
DST=$DIR/mr

echo $(date '+%Y-%m-%d %H:%M:%S') mr2 start >> $LOGFILE
hadoop fs -rm $SRC
hadoop fs -put $SRC $SRC
hadoop fs -rmr $DST
rm -rf $DST
hadoop jar $BASEDIR/hadoop2.jar example.hadoop2.mr.Main2 $SRC $DST $TOP
res=$?
hadoop fs -get $DST $DST
echo $(date '+%Y-%m-%d %H:%M:%S') mr2 end $res >> $LOGFILE
