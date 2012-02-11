BASEDIR=$(cd $(dirname $0);pwd)
. common.rc

echo $(date '+%Y-%m-%d %H:%M:%S') hive start >> $LOGFILE
hive -f $BASEDIR/../hive/local/execute.txt
res=$?
echo $(date '+%Y-%m-%d %H:%M:%S') hive end $res >> $LOGFILE
