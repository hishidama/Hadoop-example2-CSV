BASEDIR=$(cd $(dirname $0);pwd)
. common.rc

echo $(date '+%Y-%m-%d %H:%M:%S') azarea start >> $LOGFILE
rm -rf $DIR/azareaout
hadoop jar $BASEDIR/Azarea2App.jar -i=$SRC_AZAREA -o=$DIR/azareaout $TOP
res=$?
echo $(date '+%Y-%m-%d %H:%M:%S') azarea end $res >> $LOGFILE
