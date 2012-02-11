BASEDIR=$(cd $(dirname $0);pwd)
. common.rc

CASCADING_HOME=/usr/local/cascading-1.2.5-hadoop-0.19.2+
export HADOOP_CLASSPATH="$CASCADING_HOME/*":"$CASCADING_HOME/lib/*"
echo $(date '+%Y-%m-%d %H:%M:%S') cascading start >> $LOGFILE
hadoop jar $BASEDIR/../sh/cascading2.jar example.hadoop2.cascading.Main $SRC $DIR/cas $TOP
res=$?
echo $(date '+%Y-%m-%d %H:%M:%S') cascading end $res >> $LOGFILE
