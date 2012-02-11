. common.rc
echo $(date '+%Y-%m-%d %H:%M:%S') afw start >> $LOGFILE
$ASAKUSA_HOME/yaess/bin/yaess-batch.sh Example2AfwBatch -A SRC_FILE=$SRC_FILE -A LIMIT=$TOP
res=$?
echo $(date '+%Y-%m-%d %H:%M:%S') afw end $res >> $LOGFILE
