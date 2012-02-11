BASEDIR=$(cd $(dirname $0);pwd)
. common.rc
DST=$DIR/pig

echo $(date '+%Y-%m-%d %H:%M:%S') pig start >> $LOGFILE
rm -rf $DST
pig -param SRC=$SRC -f $BASEDIR/pig/put.txt
pig -param SRC=$SRC -param DST=$DST -param HH=hh_table.txt -param TOP=$TOP -f $BASEDIR/pig/execute.txt
res=$?
pig -param DST=$DST -f $BASEDIR/pig/get.txt
echo $(date '+%Y-%m-%d %H:%M:%S') pig end $res >> $LOGFILE
