BASEDIR=$(cd $(dirname $0);pwd)
. common.rc

echo $(date '+%Y-%m-%d %H:%M:%S') pig start >> $LOGFILE
pig -x local -param SRC=$SRC -param DST=$DIR/pig -param HH=hh_table.txt -param TOP=$TOP -f $BASEDIR/../pig/execute.txt
res=$?
echo $(date '+%Y-%m-%d %H:%M:%S') pig end $res >> $LOGFILE
