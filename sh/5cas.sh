BASEDIR=$(cd $(dirname $0);pwd)
. common.rc
DST=$DIR/cas

CASCADING_HOME=/usr/local/cascading-1.2.5-hadoop-0.19.2+
export HADOOP_CLASSPATH="$CASCADING_HOME/*":"$CASCADING_HOME/lib/*"
echo $(date '+%Y-%m-%d %H:%M:%S') cascading start >> $LOGFILE
hadoop fs -rm $SRC
hadoop fs -put $SRC $SRC
rm -rf $DST
hadoop jar $BASEDIR/cascading2.jar example.hadoop2.cascading.Main $SRC $DST $TOP
res=$?
hadoop fs -get $DST $DST
echo $(date '+%Y-%m-%d %H:%M:%S') cascading end $res >> $LOGFILE
