BASEDIR=$(cd $(dirname $0);pwd)
. common.rc
DST=$DIR/azaout

echo $(date '+%Y-%m-%d %H:%M:%S') azarea start >> $LOGFILE
date '+%Y-%m-%d %H:%M:%S'
rm -rf $DST/*
hadoop fs -rmr $SRC_AZAREA $DST
hadoop fs -put $SRC $SRC_AZAREA/SalesEntity.txt
hadoop fs -put $BASEDIR/HhTable.txt $SRC_AZAREA/
date '+%Y-%m-%d %H:%M:%S'
hadoop jar $BASEDIR/AzareaSalesApp.jar -i=$SRC_AZAREA -o=$DST $TOP
res=$?
date '+%Y-%m-%d %H:%M:%S'
hadoop fs -get $DST/Result1Entity.txt $DST/out1
hadoop fs -get $DST/Result2Entity.txt $DST/out2
hadoop fs -get $DST/Result3Entity.txt $DST/out3
hadoop fs -get $DST/Result4Entity.txt $DST/out4
date '+%Y-%m-%d %H:%M:%S'
echo $(date '+%Y-%m-%d %H:%M:%S') azarea end $res >> $LOGFILE
